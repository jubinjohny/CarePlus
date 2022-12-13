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
import com.example.careplus.adapters.ScheduleViewAdapter;
import com.example.careplus.databinding.FragmentManageBookingsBinding;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageBookingsFragment extends Fragment {
    FragmentManageBookingsBinding binding;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentManageBookingsBinding.inflate(inflater, container, false);
       this.setRecyclerView();
       return binding.getRoot();
    }

    public void setRecyclerView() {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("PseudoAppointments");
        user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<NewAppointmentRequest> appointmentsRequests = new ArrayList<>();
        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0) {
                    binding.noBooking.setVisibility(View.VISIBLE);;
                    appointmentsRequests.clear();
                } else {
                    appointmentsRequests.clear();
                    for(DataSnapshot doc : snapshot.getChildren()) {
                        if (doc.child("clinicEmail").getValue().toString().contentEquals(user.getEmail())) {
                            NewAppointmentRequest entry = new NewAppointmentRequest(doc.child("patientEmail").getValue().toString(),
                                    doc.child("patientName").getValue().toString(),
                                    doc.child("appointmentDate").getValue().toString(),
                                    doc.child("appointmentTime").getValue().toString(),
                                    doc.child("insuranceProvider").getValue().toString(), doc.getKey(), R.drawable.patient2,
                                    doc.child("clinicName").getValue().toString(), doc.child("doctorName").getValue().toString(),
                                    doc.child("day").getValue().toString());
                            appointmentsRequests.add(entry);
                        }
                    }
                }
                if(appointmentsRequests.size() > 0) {
                    binding.noBooking.setVisibility(View.GONE);
                    AppointmentsViewAdapter adapter = new AppointmentsViewAdapter(getContext(), appointmentsRequests);
                    binding.bookingsView.setAdapter(adapter);
                    binding.bookingsView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    binding.noBooking.setVisibility(View.VISIBLE);
                    AppointmentsViewAdapter adapter = new AppointmentsViewAdapter(getContext(), appointmentsRequests);
                    binding.bookingsView.setAdapter(adapter);
                    binding.bookingsView.setLayoutManager(new LinearLayoutManager(getContext()));
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