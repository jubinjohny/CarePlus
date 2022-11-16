package com.example.careplus.doctor.home;

import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentManageScheduleBinding;
import com.example.careplus.localStorage.DoctorScheduleEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ManageScheduleFragment extends Fragment implements View.OnClickListener{
    FragmentManageScheduleBinding binding;
    FirebaseFirestore db;
    TextView tv, startBtn, endBtn;
    List<String> availability;
    String doctorId, doctorName;
    FirebaseDatabase realTimeDB;
    DatabaseReference dbRef;
    String startTime, endTime, parentBtn;
    private Calendar calendar;
    private ArrayList<DoctorScheduleEntry> doctorSchedule = new ArrayList<DoctorScheduleEntry>();
    Map<String, Object> dailyData= new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentManageScheduleBinding.inflate(inflater,container, false);
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        binding.nextAvailabilityTitle.setText("Next Availability Starting "+ nextMonday.toString());
        binding.monday.setOnClickListener(this);
        binding.tuesday.setOnClickListener(this);
        binding.wednesday.setOnClickListener(this);
        binding.thursday.setOnClickListener(this);
        binding.friday.setOnClickListener(this);
        binding.saturday.setOnClickListener(this);
        binding.sunday.setOnClickListener(this);
        binding.updateAvailability.setOnClickListener(this);
        availability = new ArrayList<>();
        db.collection("Doctors").whereEqualTo("email", doctor.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                doctorId = doc.getId();
                                doctorName = doc.get("firstName").toString() + " " + doc.get("lastName").toString();
                            }
                        }
                    }
                });
       return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        realTimeDB = FirebaseDatabase.getInstance();
        dbRef = realTimeDB.getReference("DoctorsNextSchedule");
        switch (view.getId()) {
            case R.id.update_availability:
                Map<String, Object> scheduleData= new HashMap<>();
                scheduleData.put(doctorName+"", dailyData);
                dbRef.updateChildren(scheduleData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Schedule Updated", Toast.LENGTH_SHORT).show();
                            binding.monday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.tuesday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.wednesday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.thursday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.friday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.saturday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.sunday.setBackgroundResource(R.drawable.rounded_white_buttons);
                        }
                    }
                });
                break;
            default:
                tv = view.findViewById(view.getId());
                if(availability.contains(tv.getText().toString())) {
                    tv.setBackgroundResource(R.drawable.rounded_white_buttons);
                    availability.remove(tv.getText().toString());

                    if(tv.getText().toString().contentEquals("Monday")) {
                        binding.mondayEnd.setVisibility(View.GONE);
                        binding.mondayStart.setVisibility(View.GONE);
                    } else if(tv.getText().toString().contentEquals("Tuesday")){
                        binding.tuesdayEnd.setVisibility(View.GONE);
                        binding.tuesdayStart.setVisibility(View.GONE);
                    } else if(tv.getText().toString().contentEquals("Wednesday")){
                        binding.wednesdayEnd.setVisibility(View.GONE);
                        binding.wednesdayStart.setVisibility(View.GONE);
                    } else if(tv.getText().toString().contentEquals("Thursday")){
                        binding.thursdayEnd.setVisibility(View.GONE);
                        binding.thursdayStart.setVisibility(View.GONE);
                    } else if(tv.getText().toString().contentEquals("Friday")){
                        binding.fridayEnd.setVisibility(View.GONE);
                        binding.fridayStart.setVisibility(View.GONE);
                    } else if(tv.getText().toString().contentEquals("Saturday")){
                        binding.saturdayEnd.setVisibility(View.GONE);
                        binding.saturdayStart.setVisibility(View.GONE);
                    } else if(tv.getText().toString().contentEquals("Sunday")){
                        binding.sundayEnd.setVisibility(View.GONE);
                        binding.sundayStart.setVisibility(View.GONE);
                    }
                } else {
                    if(tv.getText().toString().contentEquals("Monday")) {
                        selectTime(binding.mondayStart.getId(), binding.mondayEnd.getId(), tv.getText().toString());
                        binding.mondayEnd.setVisibility(View.VISIBLE);
                        binding.mondayStart.setVisibility(View.VISIBLE);
                    } else if(tv.getText().toString().contentEquals("Tuesday")){
                        selectTime(binding.tuesdayStart.getId(), binding.tuesdayEnd.getId(),tv.getText().toString());
                        binding.tuesdayEnd.setVisibility(View.VISIBLE);
                        binding.tuesdayStart.setVisibility(View.VISIBLE);
                    } else if(tv.getText().toString().contentEquals("Wednesday")){
                        selectTime(binding.wednesdayStart.getId(), binding.wednesdayEnd.getId(), tv.getText().toString());
                        binding.wednesdayEnd.setVisibility(View.VISIBLE);
                        binding.wednesdayStart.setVisibility(View.VISIBLE);
                    } else if(tv.getText().toString().contentEquals("Thursday")){
                        selectTime(binding.thursdayStart.getId(), binding.thursdayEnd.getId(), tv.getText().toString());
                        binding.thursdayEnd.setVisibility(View.VISIBLE);
                        binding.thursdayStart.setVisibility(View.VISIBLE);
                    } else if(tv.getText().toString().contentEquals("Friday")){
                        selectTime(binding.fridayStart.getId(), binding.fridayEnd.getId(), tv.getText().toString());
                        binding.fridayEnd.setVisibility(View.VISIBLE);
                        binding.fridayStart.setVisibility(View.VISIBLE);
                    } else if(tv.getText().toString().contentEquals("Saturday")){
                        selectTime(binding.saturdayStart.getId(), binding.saturdayEnd.getId(), tv.getText().toString());
                        binding.saturdayEnd.setVisibility(View.VISIBLE);
                        binding.saturdayStart.setVisibility(View.VISIBLE);
                    } else if(tv.getText().toString().contentEquals("Sunday")){
                        selectTime(binding.sundayEnd.getId(), binding.sundayStart.getId(),tv.getText().toString());
                        binding.sundayEnd.setVisibility(View.VISIBLE);
                        binding.sundayStart.setVisibility(View.VISIBLE);
                    }
                    tv.setBackgroundResource(R.drawable.rounded_green_buttons);
                    availability.add(tv.getText().toString());
                }
                break;
        }
    }

    public void selectTime(int viewStart, int viewEnd, String parent) {
        calendar = Calendar.getInstance();
        int hour = 8;
        int min = 0;
        startBtn = binding.getRoot().findViewById(viewStart);
        endBtn = binding.getRoot().findViewById(viewEnd);
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        TimePickerDialog timerStart = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hr, int mi) {
                        if(hr < 10) {
                            if(mi < 10) {
                                startTime = "0"+hr + ":" + "0"+mi;
                            } else {
                                startTime =  "0"+hr + ":" +mi;
                            }
                        } else {
                            if(mi < 10) {
                                startTime = hr + ":" + "0"+mi;
                            } else {
                                startTime =  hr + ":" +mi;
                            }
                        }
                        startBtn.setText(startTime);
                        TimePickerDialog timerEnd = new TimePickerDialog(getActivity(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hr, int mi) {
                                        if(hr < 10) {
                                            if(mi < 10) {
                                                endTime = "0"+hr + ":" + "0"+mi;
                                            } else {
                                                endTime =  "0"+hr + ":" +mi;
                                            }
                                        } else {
                                            if(mi < 10) {
                                                endTime = hr + ":" + "0"+mi;
                                            } else {
                                                endTime =  hr + ":" +mi;
                                            }
                                        }
                                        endBtn.setText(endTime);
                                    }
                                }, hour, min, false);
                        timerEnd.show();
                    }
                }, hour, min, false);
        timerStart.show();

        DoctorScheduleEntry newEntry = new DoctorScheduleEntry(startTime, endTime);
        doctorSchedule.add(newEntry);
        if(dailyData.containsKey(parent)) {
            dailyData.replace(parent, doctorSchedule);
        } else {
            dailyData.put(parent, doctorSchedule);
        }
    }
}