package com.example.careplus.clinic.auth.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.careplus.adapters.AppointmentsViewAdapter;
import com.example.careplus.adapters.UpcomingAppointmentsViewAdapter;
import com.example.careplus.databinding.FragmentClinicHomeBinding;
import com.example.careplus.localStorage.Appointments;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ClinicHomeFragment extends Fragment {
    FragmentClinicHomeBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef, cancelRef;
    FirebaseAuth auth;
    boolean check;
    ArrayList<NewAppointmentRequest> cancelledAppointments = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClinicHomeBinding.inflate(inflater, container, false);
        realTimeDb = FirebaseDatabase.getInstance();
        cancelRef = realTimeDb.getReference("CancelledAppointmentsClinics");
        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0) {
                    binding.noBooking.setVisibility(View.VISIBLE);
                    cancelledAppointments.clear();
                    return;
                } else {
                    for(DataSnapshot doc : snapshot.getChildren()) {
                        if (doc.child("clinicEmail").getValue().toString().contentEquals(user.getEmail())) {
                            AlertDialog.Builder cancelled = new AlertDialog.Builder(getContext());
                            cancelled.setTitle("Appointment Cancelled");
                            cancelled.setMessage("The appointment for " + doc.child("doctorName").getValue().toString() +
                                    " on " + doc.child("appointmentDate").getValue().toString() + " at " + doc.child("appointmentTime").getValue().toString() +
                                    " was cancelled");
                            cancelled.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    snapshot.getRef().removeValue();
                                }
                            });
                            cancelled.show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fetching failed", Toast.LENGTH_SHORT).show();
            }
        };
        cancelRef.addValueEventListener(requestListener);
        binding.addDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewDoctorsFragment nextFrag = new AddNewDoctorsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });

        binding.manageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClinicProfile nextFrag = new ClinicProfile();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.viewTimeslots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvailableTimeSlotFragment nextFrag = new AvailableTimeSlotFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.scheduleDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleDoctorsFragment nextFrag = new ScheduleDoctorsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.manageBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManageBookingsFragment nextFrag = new ManageBookingsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        this.startAnimations();
        if(user != null) {
            db.collection("Clinics").whereEqualTo("email", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                binding.welcomeText.setText("Welcome "+ doc.getData().get("name").toString().toUpperCase(Locale.ROOT));
                            }
                        }
                    }
                });
            this.checkNewRequest();
        }
        this.setRecyclerView();
        return binding.getRoot();
    }
    public void checkNewRequest() {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("PseudoAppointments");
        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot doc : snapshot.getChildren()) {
                    if(doc.child("clinicEmail").getValue().toString().contentEquals(user.getEmail())) {
                        binding.newRequest.setVisibility(View.VISIBLE);
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

    public void setRecyclerView() {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("ConfirmedAppointments");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<NewAppointmentRequest> upcomingAppointments = new ArrayList<>();
        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0) {
                    binding.noBooking.setVisibility(View.VISIBLE);
                    upcomingAppointments.clear();
                    return;
                } else {
                    for(DataSnapshot doc : snapshot.getChildren()) {
                        if (doc.child("clinicEmail").getValue().toString().contentEquals(user.getEmail())) {
                            LocalDate date = LocalDate.now();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date appointmentDate = null;
                            try {
                                appointmentDate = sdf.parse(doc.child("appointmentDate").getValue().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(new Date().before(appointmentDate)) {
                                NewAppointmentRequest entry = new NewAppointmentRequest(doc.child("patientEmail").getValue().toString(),
                                        doc.child("patientName").getValue().toString(),
                                        doc.child("appointmentDate").getValue().toString(),
                                        doc.child("appointmentTime").getValue().toString(),
                                        doc.child("insuranceProvider").getValue().toString(), doc.getKey(), R.drawable.patient2, doc.child("clinicName").getValue().toString(),
                                        "Doctor : " + doc.child("doctorName").getValue().toString());
                                upcomingAppointments.add(entry);
                            }
                        }
                    }
                }
                if(upcomingAppointments.size() > 0) {
                    binding.noBooking.setVisibility(View.GONE);
                    UpcomingAppointmentsViewAdapter adapter = new UpcomingAppointmentsViewAdapter(getContext(), upcomingAppointments, "Clinics");
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
        binding.bookingLayout.setAlpha(0f);
        binding.bookingLayout.setAlpha(0f);
        binding.bookingLayout.setTranslationY(50);
        binding.bookingLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.viewScheduleLayout.setAlpha(0f);
        binding.viewScheduleLayout.setAlpha(0f);
        binding.viewScheduleLayout.setTranslationY(50);
        binding.viewScheduleLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.scheduleDoctorsLayout.setAlpha(0f);
        binding.scheduleDoctorsLayout.setAlpha(0f);
        binding.scheduleDoctorsLayout.setTranslationY(50);
        binding.scheduleDoctorsLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.upcomingScheduleLayout.setAlpha(0f);
        binding.upcomingScheduleLayout.setAlpha(0f);
        binding.upcomingScheduleLayout.setTranslationY(50);
        binding.upcomingScheduleLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.manageProfileLayout.setAlpha(0f);
        binding.manageProfileLayout.setAlpha(0f);
        binding.manageProfileLayout.setTranslationY(50);
        binding.manageProfileLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.addDoctorsLayout.setAlpha(0f);
        binding.addDoctorsLayout.setAlpha(0f);
        binding.addDoctorsLayout.setTranslationY(50);
        binding.addDoctorsLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.upcomingView.setAlpha(0f);
        binding.upcomingView.setAlpha(0f);
        binding.upcomingView.setTranslationY(50);
        binding.upcomingView.animate().alpha(1f).translationYBy(-50).setDuration(1000);
    }
}