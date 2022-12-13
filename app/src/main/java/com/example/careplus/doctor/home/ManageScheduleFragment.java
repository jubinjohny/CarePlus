package com.example.careplus.doctor.home;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.adapters.UpcomingAppointmentsViewAdapter;
import com.example.careplus.adapters.UpcomingScheduleViewAdapter;
import com.example.careplus.databinding.FragmentManageScheduleBinding;
import com.example.careplus.localStorage.DoctorScheduleEntry;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.example.careplus.localStorage.UpcomingScheduleData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
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
        realTimeDB = FirebaseDatabase.getInstance();
        dbRef = realTimeDB.getReference("DoctorsNextSchedule");
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
        binding.mondayStart.setOnClickListener(this);
        binding.mondayEnd.setOnClickListener(this);
        binding.tuesdayStart.setOnClickListener(this);
        binding.tuesdayEnd.setOnClickListener(this);
        binding.wednesdayStart.setOnClickListener(this);
        binding.wednesdayEnd.setOnClickListener(this);
        binding.thursdayStart.setOnClickListener(this);
        binding.thursdayEnd.setOnClickListener(this);
        binding.fridayStart.setOnClickListener(this);
        binding.fridayEnd.setOnClickListener(this);
        binding.saturdayStart.setOnClickListener(this);
        binding.saturdayEnd.setOnClickListener(this);
        binding.sundayStart.setOnClickListener(this);
        binding.sundayEnd.setOnClickListener(this);
        availability = new ArrayList<>();

        db.collection("Doctors").whereEqualTo("email", doctor.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                doctorId = doc.getId();
                                doctorName = doc.get("firstName").toString() + " " + doc.get("lastName").toString();
                                if(doc.getBoolean("alertBox") == null ){
                                    AlertDialog.Builder scheduleAlert = new AlertDialog.Builder(getActivity());
                                    scheduleAlert.setTitle("Schedule Update Note");
                                    scheduleAlert.setIcon(R.drawable.ic_calendar);
                                    scheduleAlert.setMessage("The schedule updated here will be repeated to upcoming 4 weeks");
                                    scheduleAlert.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    scheduleAlert.setNegativeButton("Don't Show Again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            db.collection("Doctors").document(doc.getId())
                                                    .update("alertBox",  true);
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    scheduleAlert.show();

                                }
                            }
                            setRecyclerView();
                        }
                    }
                });
       return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> scheduleData= new HashMap<>();
        switch (view.getId()) {
            case R.id.update_availability:
                scheduleData.put(doctorName+"", dailyData);
                dbRef.updateChildren(scheduleData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Schedule Updated", Toast.LENGTH_SHORT).show();
                            if(binding.mondayStart.getVisibility() == View.VISIBLE) {
                                binding.mondayStart.setVisibility(View.GONE);
                                binding.mondayEnd.setVisibility(View.GONE);
                                binding.monday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            }
                            if(binding.tuesdayStart.getVisibility() == View.VISIBLE) {
                                binding.tuesdayStart.setVisibility(View.GONE);
                                binding.tuesdayEnd.setVisibility(View.GONE);
                                binding.tuesday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            }
                            if(binding.wednesdayStart.getVisibility() == View.VISIBLE) {
                                binding.wednesdayStart.setVisibility(View.GONE);
                                binding.wednesdayEnd.setVisibility(View.GONE);
                                binding.wednesday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            }
                            if(binding.thursdayStart.getVisibility() == View.VISIBLE) {
                                binding.thursdayStart.setVisibility(View.GONE);
                                binding.thursdayEnd.setVisibility(View.GONE);
                                binding.thursday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            }
                            if(binding.fridayStart.getVisibility() == View.VISIBLE) {
                                binding.fridayStart.setVisibility(View.GONE);
                                binding.fridayEnd.setVisibility(View.GONE);
                                binding.friday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            }
                            if(binding.saturdayStart.getVisibility() == View.VISIBLE) {
                                binding.saturdayStart.setVisibility(View.GONE);
                                binding.saturdayEnd.setVisibility(View.GONE);
                                binding.saturday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            }
                            if(binding.sundayStart.getVisibility() == View.VISIBLE) {
                                binding.sundayStart.setVisibility(View.GONE);
                                binding.sundayStart.setVisibility(View.GONE);
                                binding.sunday.setBackgroundResource(R.drawable.rounded_white_buttons);
                            }
                        }
                    }
                });
                break;
            default:
                switch(view.getId()) {
                    case R.id.mondayStart:
                    case R.id.mondayEnd:
                        tv = (TextView) binding.monday;
                        availability.remove(tv.getText().toString());
                        dailyData.remove("Monday");
                        selectTime(binding.mondayStart.getId(), binding.mondayEnd.getId(), tv.getText().toString());
                        break;
                    case R.id.tuesdayStart:
                    case R.id.tuesdayEnd:
                        tv = (TextView) binding.tuesday;
                        availability.remove(tv.getText().toString());
                        dailyData.remove("Tuesday");
                        selectTime(binding.tuesdayStart.getId(), binding.tuesdayEnd.getId(), tv.getText().toString());
                        break;
                    case R.id.wednesdayStart:
                    case R.id.wednesdayEnd:
                        tv = (TextView) binding.wednesday;
                        availability.remove(tv.getText().toString());
                        dailyData.remove("Wednesday");
                        selectTime(binding.wednesdayStart.getId(), binding.wednesdayEnd.getId(), tv.getText().toString());
                        break;
                    case R.id.thursdayStart:
                    case R.id.thursdayEnd:
                        tv = (TextView) binding.thursday;
                        availability.remove(tv.getText().toString());
                        dailyData.remove("Thursday");
                        selectTime(binding.thursdayStart.getId(), binding.thursdayEnd.getId(), tv.getText().toString());
                        break;
                    case R.id.fridayStart:
                    case R.id.fridayEnd:
                        tv = (TextView) binding.friday;
                        availability.remove(tv.getText().toString());
                        dailyData.remove("Friday");
                        selectTime(binding.fridayStart.getId(), binding.fridayEnd.getId(), tv.getText().toString());
                        break;
                    case R.id.saturdayStart:
                    case R.id.saturdayEnd:
                        tv = (TextView) binding.saturday;
                        availability.remove(tv.getText().toString());
                        dailyData.remove("Saturday");
                        selectTime(binding.saturdayStart.getId(), binding.saturdayEnd.getId(), tv.getText().toString());
                        break;
                    case R.id.sundayStart:
                    case R.id.sundayEnd:
                        tv = (TextView) binding.sunday;
                        availability.remove(tv.getText().toString());
                        dailyData.remove("Sunday");
                        selectTime(binding.sundayStart.getId(), binding.sundayEnd.getId(), tv.getText().toString());
                        break;
                    default:
                        tv = view.findViewById(view.getId());
                        if(availability.contains(tv.getText().toString()) ) {
                            tv.setBackgroundResource(R.drawable.rounded_white_buttons);
                            availability.remove(tv.getText().toString());
                            if(tv.getText().toString().contentEquals("Monday")) {
                                dailyData.remove("Monday");
                                binding.mondayEnd.setText("");
                                binding.mondayStart.setText("");
                                binding.mondayEnd.setVisibility(View.GONE);
                                binding.mondayStart.setVisibility(View.GONE);
                            } else if(tv.getText().toString().contentEquals("Tuesday")){
                                dailyData.remove("Tuesday");
                                binding.tuesdayEnd.setText("");
                                binding.tuesdayStart.setText("");
                                binding.tuesdayEnd.setVisibility(View.GONE);
                                binding.tuesdayStart.setVisibility(View.GONE);
                            } else if(tv.getText().toString().contentEquals("Wednesday")){
                                dailyData.remove("Wednesday");
                                binding.wednesdayEnd.setText("");
                                binding.wednesdayStart.setText("");
                                binding.wednesdayEnd.setVisibility(View.GONE);
                                binding.wednesdayStart.setVisibility(View.GONE);
                            } else if(tv.getText().toString().contentEquals("Thursday")){
                                dailyData.remove("Thursday");
                                binding.thursdayEnd.setText("");
                                binding.thursdayStart.setText("");
                                binding.thursdayEnd.setVisibility(View.GONE);
                                binding.thursdayStart.setVisibility(View.GONE);
                            } else if(tv.getText().toString().contentEquals("Friday")){
                                dailyData.remove("Friday");
                                binding.fridayEnd.setText("");
                                binding.fridayStart.setText("");
                                binding.fridayEnd.setVisibility(View.GONE);
                                binding.fridayStart.setVisibility(View.GONE);
                            } else if(tv.getText().toString().contentEquals("Saturday")){
                                dailyData.remove("Saturday");
                                binding.saturdayEnd.setText("");
                                binding.saturdayStart.setText("");
                                binding.saturdayEnd.setVisibility(View.GONE);
                                binding.saturdayStart.setVisibility(View.GONE);
                            } else if(tv.getText().toString().contentEquals("Sunday")){
                                dailyData.remove("Sunday");
                                binding.sundayEnd.setText("");
                                binding.sundayStart.setText("");
                                binding.sundayEnd.setVisibility(View.GONE);
                                binding.sundayStart.setVisibility(View.GONE);
                            }
                        }
                        else {
                            if(tv.getText().toString().contentEquals("Monday")) {
                                binding.mondayEnd.setVisibility(View.VISIBLE);
                                binding.mondayStart.setVisibility(View.VISIBLE);
                                selectTime(binding.mondayStart.getId(), binding.mondayEnd.getId(), tv.getText().toString());
                            } else if(tv.getText().toString().contentEquals("Tuesday")){
                                binding.tuesdayEnd.setVisibility(View.VISIBLE);
                                binding.tuesdayStart.setVisibility(View.VISIBLE);
                                selectTime(binding.tuesdayStart.getId(), binding.tuesdayEnd.getId(),tv.getText().toString());
                            } else if(tv.getText().toString().contentEquals("Wednesday")){
                                binding.wednesdayEnd.setVisibility(View.VISIBLE);
                                binding.wednesdayStart.setVisibility(View.VISIBLE);
                                selectTime(binding.wednesdayStart.getId(), binding.wednesdayEnd.getId(), tv.getText().toString());
                            } else if(tv.getText().toString().contentEquals("Thursday")){
                                binding.thursdayEnd.setVisibility(View.VISIBLE);
                                binding.thursdayStart.setVisibility(View.VISIBLE);
                                selectTime(binding.thursdayStart.getId(), binding.thursdayEnd.getId(), tv.getText().toString());
                            } else if(tv.getText().toString().contentEquals("Friday")){
                                binding.fridayEnd.setVisibility(View.VISIBLE);
                                binding.fridayStart.setVisibility(View.VISIBLE);
                                selectTime(binding.fridayStart.getId(), binding.fridayEnd.getId(), tv.getText().toString());
                            } else if(tv.getText().toString().contentEquals("Saturday")){
                                binding.saturdayEnd.setVisibility(View.VISIBLE);
                                binding.saturdayStart.setVisibility(View.VISIBLE);
                                selectTime(binding.saturdayStart.getId(), binding.saturdayEnd.getId(), tv.getText().toString());
                            } else if(tv.getText().toString().contentEquals("Sunday")){
                                binding.sundayEnd.setVisibility(View.VISIBLE);
                                binding.sundayStart.setVisibility(View.VISIBLE);
                                selectTime(binding.sundayEnd.getId(), binding.sundayStart.getId(),tv.getText().toString());
                            }
                            tv.setBackgroundResource(R.drawable.rounded_green_buttons);
                            availability.add(tv.getText().toString());
                        }
                        break;
                }
                break;
        }
    }

    public void selectTime(int viewStart, int viewEnd, String parent) {
        calendar = Calendar.getInstance();
        int hour = 8;
        int min = 0;
        final int[] startHour = {0};
        final int[] startMin = {0};
        final int[] endHour = {0};
        final int[] endMin = {0};
        startBtn = binding.getRoot().findViewById(viewStart);
        endBtn = binding.getRoot().findViewById(viewEnd);
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
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
                        endHour[0] = hr;
                        endMin[0] = mi;
                        Map<String, ArrayList<String>> timeslots = new HashMap<>();
                        ArrayList<String> times = new ArrayList<>();
                        for(int i = startHour[0]; i <= endHour[0]; i++) {
                            if(i == endHour[0] && endMin[0] == 0) {
                                break;
                            } else {
                                if(i < 10) {
                                    if(startMin[0] < 10) {
                                        times.add("0" + i + ":" + "0" + startMin[0]);
                                    } else {
                                        times.add("0" + i + ":" + startMin[0]);
                                    }
                                } else {
                                    if(startMin[0] < 10) {
                                        times.add(i + ":" + "0" + startMin[0]);
                                    } else {
                                        times.add(i + ":" + startMin[0]);
                                    }
                                }
                            }
                        }
                        timeslots.put("week1", times);
                        timeslots.put("week2", times);
                        timeslots.put("week3", times);
                        timeslots.put("week4", times);
                        DoctorScheduleEntry newEntry = new DoctorScheduleEntry(startTime, endTime, timeslots);
                        doctorSchedule.add(newEntry);
                        dailyData.put(parent, newEntry);
                        doctorSchedule.clear();
                    }
                }, hour, min, false);
        timerEnd.setMessage("Choose End Time");
        timerEnd.show();
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
                        startHour[0] = hr;
                        startMin[0] = mi;
                    }
                }, hour, min, false);
        timerStart.setMessage("Choose Start Time");
        timerStart.show();
    }

    public void setRecyclerView() {
        ArrayList<UpcomingScheduleData> upcomingSchedule = new ArrayList<>();
        ValueEventListener requestListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0) {
                    binding.noSchedule.setVisibility(View.VISIBLE);
                    upcomingSchedule.clear();
                } else {
                    upcomingSchedule.clear();
                    for(DataSnapshot doc : snapshot.getChildren()) {
                        if(doc.getKey().contentEquals(doctorName)) {
                            for (DataSnapshot data : doc.getChildren()) {
                                LocalDate date = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                                switch(data.getKey()) {
                                    case "Tuesday":
                                        date = date.plusDays(1);
                                        break;
                                    case "Wednesday":
                                        date = date.plusDays(2);
                                        break;
                                    case "Thursday":
                                        date = date.plusDays(3);
                                        break;
                                    case "Friday":
                                        date = date.plusDays(4);
                                        break;
                                    case "Saturday":
                                        date = date.plusDays(5);
                                        break;
                                    case "Sunday":
                                        date = date.plusDays(6);
                                        break;
                                    default:
                                        date = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                                }
                                UpcomingScheduleData entry = new UpcomingScheduleData(data.getKey(),
                                        date.toString(), data.child("startTime").getValue().toString(), data.child("endTime").getValue().toString());
                                upcomingSchedule.add(entry);
                            }
                        } else {
                            continue;
                        }
                    }
                }
                if(upcomingSchedule.size() > 0) {
                    UpcomingScheduleViewAdapter adapter = new UpcomingScheduleViewAdapter(getContext(), upcomingSchedule);
                    binding.upcomingView.setAdapter(adapter);
                    binding.upcomingView.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    binding.noSchedule.setVisibility(View.VISIBLE);
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