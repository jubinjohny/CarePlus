package com.example.careplus.patient.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.adapters.AppointmentsViewAdapter;
import com.example.careplus.adapters.PatientAppointmentsViewAdapter;
import com.example.careplus.databinding.FragmentManagePatientBookingsBinding;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagePatientBookingsFragment extends Fragment {
    FragmentManagePatientBookingsBinding binding;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentManagePatientBookingsBinding.inflate(inflater, container, false);
       this.setRecyclerView();
       return binding.getRoot();
    }

    private void setRecyclerView() {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("ConfirmedAppointments");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<NewAppointmentRequest> appointments = new ArrayList<>();
        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0) {
                    binding.noBooking.setVisibility(View.VISIBLE);;
                    appointments.clear();
                } else {
                    appointments.clear();
                    for(DataSnapshot doc : snapshot.getChildren()) {
                        if (doc.child("patientEmail").getValue().toString().contentEquals(user.getEmail())) {
                            NewAppointmentRequest entry = new NewAppointmentRequest(doc.child("clinicEmail").getValue().toString(),
                                    doc.child("clinicName").getValue().toString(),
                                    doc.child("appointmentDate").getValue().toString(),
                                    doc.child("appointmentTime").getValue().toString(),
                                    doc.child("insuranceProvider").getValue().toString(), doc.getKey(), R.drawable.patient2,
                                    doc.child("doctorName").getValue().toString());
                            appointments.add(entry);
                        }
                    }
                }
                if(appointments.size() > 0) {
                    binding.noBooking.setVisibility(View.GONE);
                    PatientAppointmentsViewAdapter adapter = new PatientAppointmentsViewAdapter(getContext(), appointments);
                    binding.bookingsView.setAdapter(adapter);
                    binding.bookingsView.setLayoutManager(new LinearLayoutManager(getContext()));
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


}