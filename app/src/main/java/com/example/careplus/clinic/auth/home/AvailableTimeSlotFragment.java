package com.example.careplus.clinic.auth.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.careplus.adapters.ScheduleViewAdapter;
import com.example.careplus.databinding.FragmentAvailableTimeSlotBinding;
import com.example.careplus.localStorage.ScheduleViewCardData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvailableTimeSlotFragment extends Fragment {
    FragmentAvailableTimeSlotBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAvailableTimeSlotBinding.inflate(inflater, container, false);
        this.setRecyclerView();
        return binding.getRoot();
    }

    public void setRecyclerView() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<ScheduleViewCardData> scheduleList = new ArrayList<>();
                        LocalDate currentDay = LocalDate.now();
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                Map<String, List<String>> map = (Map) doc.getData().get("currentSchedule");
                                if(map.isEmpty()) {
                                    binding.noSchedule.setVisibility(View.VISIBLE);
                                    return;
                                }
                                do{
                                    if(map.get(currentDay.getDayOfWeek().toString()).get(0) != null) {
                                        ScheduleViewCardData abc = new ScheduleViewCardData(currentDay.getDayOfWeek().toString(), currentDay.toString(), map.get(currentDay.getDayOfWeek().toString()).get(0), map.get(currentDay.getDayOfWeek().toString()).get(1));
                                        scheduleList.add(abc);
                                        currentDay = currentDay.plusDays(1);
                                    }
                                }while(currentDay.getDayOfWeek().toString() != "MONDAY");
                            }
                        }
                        ScheduleViewAdapter adapter = new ScheduleViewAdapter(getContext(), scheduleList);
                        binding.scheduleView.setAdapter(adapter);
                        binding.scheduleView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                });
    }
}