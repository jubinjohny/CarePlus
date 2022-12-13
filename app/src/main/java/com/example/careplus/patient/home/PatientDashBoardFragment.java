package com.example.careplus.patient.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.adapters.UpcomingAppointmentsViewAdapter;
import com.example.careplus.databinding.FragmentPatientDashBoardBinding;
import com.example.careplus.localStorage.NewAppointmentRequest;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class PatientDashBoardFragment extends Fragment {
    FragmentPatientDashBoardBinding binding;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    FirebaseUser user;
    FirebaseFirestore db;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.4F);
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentPatientDashBoardBinding.inflate(inflater, container,false);
        binding.searchClinics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                SearchClinicsFragment newFrag = new SearchClinicsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.patient_fragment_container, newFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientProfileFragment nextFrag = new PatientProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.patient_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.transferData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferDataFragment nextFrag = new TransferDataFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.patient_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.manageBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManagePatientBookingsFragment nextFrag = new ManagePatientBookingsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.patient_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Patients").whereEqualTo("email", user.getEmail()).get()
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
        this.startAnimations();
        this.setRecyclerView();
        return binding.getRoot();
    }

    public void setRecyclerView() {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("ConfirmedAppointments");
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<NewAppointmentRequest> upcomingAppointments = new ArrayList<>();
        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0) {
                    binding.noBooking.setVisibility(View.VISIBLE);
                    upcomingAppointments.clear();
                } else {
                    for(DataSnapshot doc : snapshot.getChildren()) {
                        if (doc.child("patientEmail").getValue().toString().contentEquals(user.getEmail())) {
                            int check = Integer.parseInt(doc.child("appointmentDate").getValue().toString().substring(doc.child("appointmentDate").getValue().toString().length() - 2));
                            LocalDate date = LocalDate.now();
                            if(check >= date.getDayOfMonth()) {
                                NewAppointmentRequest entry = new NewAppointmentRequest(doc.child("clinicEmail").getValue().toString(),
                                        doc.child("clinicName").getValue().toString(),
                                        doc.child("appointmentDate").getValue().toString(),
                                        doc.child("appointmentTime").getValue().toString(),
                                        doc.child("insuranceProvider").getValue().toString(), doc.getKey(), R.drawable.patient2, doc.child("doctorName").getValue().toString());
                                upcomingAppointments.add(entry);
                            }
                        }
                    }
                }
                if(upcomingAppointments.size() > 0 ) {
                    binding.noBooking.setVisibility(View.GONE);
                    UpcomingAppointmentsViewAdapter adapter = new UpcomingAppointmentsViewAdapter(getContext(), upcomingAppointments, "Patients");
                    binding.upcomingView.setAdapter(adapter);
                    binding.upcomingView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    binding.noBooking.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fetching failed", Toast.LENGTH_SHORT).show();
            }
        };
        dbRef.addValueEventListener(requestListener);
    }

    public void startAnimations() {
        binding.searchClinicsLayout.setAlpha(0f);
        binding.searchClinicsLayout.setAlpha(0f);
        binding.searchClinicsLayout.setTranslationY(50);
        binding.searchClinicsLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.manageBookingsLayout.setAlpha(0f);
        binding.manageBookingsLayout.setAlpha(0f);
        binding.manageBookingsLayout.setTranslationY(50);
        binding.manageBookingsLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.transferDataLayout.setAlpha(0f);
        binding.transferDataLayout.setAlpha(0f);
        binding.transferDataLayout.setTranslationY(50);
        binding.transferDataLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

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