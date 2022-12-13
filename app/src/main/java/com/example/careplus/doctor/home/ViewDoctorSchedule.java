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
import com.example.careplus.adapters.DoctorScheduleViewAdapter;
import com.example.careplus.adapters.UpcomingAppointmentsViewAdapter;
import com.example.careplus.databinding.FragmentViewDoctorScheduleBinding;
import com.example.careplus.localStorage.DoctorViewSchedule;
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

public class ViewDoctorSchedule extends Fragment {
    FragmentViewDoctorScheduleBinding binding;
    FirebaseUser user;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    FirebaseFirestore db;
    String doctorName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewDoctorScheduleBinding.inflate(inflater, container, false);

        this.setRecyclerView();
        return binding.getRoot();
    }
    public void setRecyclerView() {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("NextSchedule");
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ArrayList<DoctorViewSchedule> upcomingSchedule = new ArrayList<>();
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
                                    if (snapshot.getChildrenCount() == 0) {
                                        binding.noSchedule.setVisibility(View.VISIBLE);
                                        upcomingSchedule.clear();
                                        return;
                                    } else {
                                        upcomingSchedule.clear();
                                        for (DataSnapshot doc : snapshot.getChildren()) {
                                            for (DataSnapshot day : doc.getChildren()) {
                                                for (DataSnapshot each : day.getChildren()) {
                                                    if(each.child("doctorName").getValue().toString().contentEquals(doctorName)) {
                                                    DoctorViewSchedule entry = new DoctorViewSchedule(day.getKey(), each.child("startTime").getValue().toString(),
                                                            each.child("endTime").getValue().toString(), doc.getKey());
                                                    upcomingSchedule.add(entry);
                                                  }
                                                }
//                                            }
                                            }
                                            if (upcomingSchedule.size() > 0) {
                                                binding.noSchedule.setVisibility(View.GONE);
                                                DoctorScheduleViewAdapter adapter = new DoctorScheduleViewAdapter(getContext(), upcomingSchedule);
                                                binding.scheduleView.setAdapter(adapter);
                                                binding.scheduleView.setLayoutManager(new LinearLayoutManager(getContext()));
                                            } else {
                                                binding.noSchedule.setVisibility(View.VISIBLE);
                                            }
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
}