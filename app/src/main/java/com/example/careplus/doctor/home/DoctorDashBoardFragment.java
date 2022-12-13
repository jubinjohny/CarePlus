package com.example.careplus.doctor.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.adapters.UpcomingAppointmentsViewAdapter;
import com.example.careplus.databinding.FragmentDoctorDashBoardBinding;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.example.careplus.patient.home.PatientDashBoardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class DoctorDashBoardFragment extends Fragment {
    FragmentDoctorDashBoardBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    String doctorName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorDashBoardBinding.inflate(inflater, container, false);
        binding.manageSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManageScheduleFragment newFrag = new ManageScheduleFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.doctor_fragment_container, newFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.requestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewRequestFragment nextFrag = new NewRequestFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.doctor_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorProfileFragment nextFrag = new DoctorProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.doctor_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.viewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDoctorSchedule nextFrag = new ViewDoctorSchedule();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.doctor_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if(user != null ){
            db.collection("Doctors").whereEqualTo("email", user.getEmail()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot doc : task.getResult()) {
                                    binding.welcomeText.setText("Welcome "+ doc.getData().get("firstName").toString().toUpperCase(Locale.ROOT) + " " + doc.getData().get("lastName").toString().toUpperCase(Locale.ROOT));
                                }
                            }
                        }
                    });
            checkNewRequest();
        }
        this.startAnimations();
        this.setRecyclerView();
        return binding.getRoot();
    }

    public void checkNewRequest() {
        db = FirebaseFirestore.getInstance();
        db.collection("Doctors").whereEqualTo("email", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for ( QueryDocumentSnapshot doc : task.getResult())  {
                                Log.d("Here===", doc.getData().get("requests").toString());
                                if(doc.getData().get("requests").toString() == "[]" || doc.getData().get("requests").toString() == "") {
                                    binding.newRequest.setVisibility(View.GONE);
                                } else {
                                    binding.newRequest.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
    }

    public void setRecyclerView() {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("ConfirmedAppointments");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<NewAppointmentRequest> upcomingAppointments = new ArrayList<>();
        db.collection("Doctors").whereEqualTo("email", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                doctorName = doc.getData().get("firstName") + " " + doc.getData().get("lastName");
                            }
                            ValueEventListener requestListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getChildrenCount() == 0) {
                                        binding.noBooking.setVisibility(View.VISIBLE);
                                        upcomingAppointments.clear();
                                        return;
                                    } else {
                                        for(DataSnapshot doc : snapshot.getChildren()) {
                                            if (doc.child("doctorName").getValue().toString().contentEquals(doctorName)) {
                                                int check = Integer.parseInt(doc.child("appointmentDate").getValue().toString().substring(doc.child("appointmentDate").getValue().toString().length() - 2));
                                                LocalDate date = LocalDate.now();
                                                if(check >= date.getDayOfMonth()) {
                                                    NewAppointmentRequest entry = new NewAppointmentRequest(doc.child("patientEmail").getValue().toString(),
                                                            doc.child("patientName").getValue().toString(),
                                                            doc.child("appointmentDate").getValue().toString(),
                                                            doc.child("appointmentTime").getValue().toString(),
                                                            doc.child("insuranceProvider").getValue().toString(), doc.getKey(), R.drawable.patient2, doc.child("clinicName").getValue().toString(),
                                                            "Clinic : " + doc.child("clinicName").getValue().toString());
                                                    upcomingAppointments.add(entry);
                                                }
                                            }
                                        }
                                        if(upcomingAppointments.size() > 0) {
                                            binding.noBooking.setVisibility(View.GONE);
                                            UpcomingAppointmentsViewAdapter adapter = new UpcomingAppointmentsViewAdapter(getContext(), upcomingAppointments, "Doctors");
                                            binding.upcomingView.setAdapter(adapter);
                                            binding.upcomingView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        } else {
                                            binding.noBooking.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getActivity(), "Fetching failed", Toast.LENGTH_SHORT).show();
                                }
                            };
                            dbRef.addValueEventListener(requestListener);
                        }
                    }
                });
    }

    public void startAnimations() {
        binding.manageAvailabilityLogout.setAlpha(0f);
        binding.manageAvailabilityLogout.setAlpha(0f);
        binding.manageAvailabilityLogout.setTranslationY(50);
        binding.manageAvailabilityLogout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.viewScheduleLayout.setAlpha(0f);
        binding.viewScheduleLayout.setAlpha(0f);
        binding.viewScheduleLayout.setTranslationY(50);
        binding.viewScheduleLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.newRequestLayout.setAlpha(0f);
        binding.newRequestLayout.setAlpha(0f);
        binding.newRequestLayout.setTranslationY(50);
        binding.newRequestLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.upcomingBookingsLayout.setAlpha(0f);
        binding.upcomingBookingsLayout.setAlpha(0f);
        binding.upcomingBookingsLayout.setTranslationY(50);
        binding.upcomingBookingsLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.updateProfileLayout.setAlpha(0f);
        binding.updateProfileLayout.setAlpha(0f);
        binding.updateProfileLayout.setTranslationY(50);
        binding.updateProfileLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.upcomingView.setAlpha(0f);
        binding.upcomingView.setAlpha(0f);
        binding.upcomingView.setTranslationY(50);
        binding.upcomingView.animate().alpha(1f).translationYBy(-50).setDuration(1000);
    }
}