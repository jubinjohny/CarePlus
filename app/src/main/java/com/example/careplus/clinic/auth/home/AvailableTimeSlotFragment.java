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
import com.example.careplus.databinding.FragmentAvailableTimeSlotBinding;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.example.careplus.localStorage.ScheduleViewCardData;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AvailableTimeSlotFragment extends Fragment {
    FragmentAvailableTimeSlotBinding binding;
    FirebaseUser user;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    String clinicName;
    FirebaseFirestore db;
    String an, fn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAvailableTimeSlotBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.setRecyclerView();
        return binding.getRoot();
    }

    public void setRecyclerView() {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("NextSchedule");
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        String[] days = new String[7];
        days[0] = "monday";
        days[1] = "tuesday";
        days[2] = "wednesday";
        days[3] = "thursday";
        days[4] = "friday";
        days[5] = "saturday";
        days[6] = "sunday";
        ArrayList<ScheduleViewCardData> scheduleList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            clinicName = doc.getData().get("name").toString();
                            ValueEventListener requestListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getChildrenCount() == 0) {
                                        binding.noSchedule.setVisibility(View.VISIBLE);;
                                        scheduleList.clear();
                                    } else {
                                        scheduleList.clear();
                                        for(DataSnapshot doc : snapshot.getChildren()) {
                                            if (doc.getKey().contentEquals(clinicName)) {
                                                for(String each : days) {
                                                    if(!doc.child(each+"FN").getValue().toString().isEmpty()) {
                                                        an = doc.child(each+"FN").getValue().toString();
                                                    } else { an = "No Schedule";}
                                                    if(!doc.child(each+"AN").getValue().toString().isEmpty()) {
                                                        fn = doc.child(each+"AN").getValue().toString();
                                                    } else { fn = "No Schedule";}
                                                    ScheduleViewCardData entry = new ScheduleViewCardData(
                                                            each.toUpperCase(Locale.ROOT), nextMonday.toString(), fn, an );
                                                    scheduleList.add(entry);
                                                }
                                            }
                                        }
                                    }
                                    ScheduleViewAdapter adapter = new ScheduleViewAdapter(getContext(), scheduleList);
                                    binding.scheduleView.setAdapter(adapter);
                                    binding.scheduleView.setLayoutManager(new LinearLayoutManager(getContext()));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getActivity(), "Fetching failed", Toast.LENGTH_SHORT).show();
                                }
                            };
                            dbRef.addValueEventListener(requestListener);
                        }
                    }
                }
            });
    }
}