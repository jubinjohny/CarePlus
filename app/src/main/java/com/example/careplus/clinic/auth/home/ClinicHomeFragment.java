package com.example.careplus.clinic.auth.home;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClinicHomeFragment extends Fragment {
    FragmentClinicHomeBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClinicHomeBinding.inflate(inflater, container, false);

        binding.addDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewDoctorsFragment nextFrag = new AddNewDoctorsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.manageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClinicProfile nextFrag = new ClinicProfile();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.viewTimeslots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvailableTimeSlotFragment nextFrag = new AvailableTimeSlotFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.scheduleDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleDoctorsFragment nextFrag = new ScheduleDoctorsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.manageBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManageBookingsFragment nextFrag = new ManageBookingsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getEmail() != null ){
            realTimeDb = FirebaseDatabase.getInstance();
            dbRef = realTimeDb.getReference("PseudoAppointments");
            checkNewRequest(dbRef);
        }
        this.setRecyclerView();
        return binding.getRoot();
    }
    public void checkNewRequest(DatabaseReference dbRef) {
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
                } else {
                    for(DataSnapshot doc : snapshot.getChildren()) {
                        if (doc.child("clinicEmail").getValue().toString().contentEquals(user.getEmail())) {
                            NewAppointmentRequest entry = new NewAppointmentRequest(doc.child("patientEmail").getValue().toString(),
                                    doc.child("patientName").getValue().toString(),
                                    doc.child("appointmentDate").getValue().toString(),
                                    doc.child("appointmentTime").getValue().toString(),
                                    doc.child("insuranceProvider").getValue().toString(), doc.getKey(), R.drawable.patient2);
                            upcomingAppointments.add(entry);
                        }
                    }
                }
                UpcomingAppointmentsViewAdapter adapter = new UpcomingAppointmentsViewAdapter(getContext(), upcomingAppointments);
                binding.upcomingView.setAdapter(adapter);
                binding.upcomingView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fetching failed", Toast.LENGTH_SHORT).show();
            }
        };
        dbRef.addValueEventListener(requestListener);
    }
}