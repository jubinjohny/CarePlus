package com.example.careplus.clinic.auth.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.adapters.DoctorListViewAdapter;
import com.example.careplus.databinding.FragmentScheduleDoctorsBinding;
import com.example.careplus.localStorage.SQLiteDBScheduleDoctors;
import com.example.careplus.localStorage.ScheduleDoctorsData;
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

public class ScheduleDoctorsFragment extends Fragment implements View.OnClickListener {
    FragmentScheduleDoctorsBinding binding;
    FirebaseFirestore db, dbDoc;
    FirebaseUser user;
    ArrayList<String> doctorsList;
    ArrayList<String> availabilityArray;
    ArrayList<String> docs;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRefClinic, dbRefDoctor;
    String clinicId, clinicName, clinicEmail;
    SQLiteDBScheduleDoctors DB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScheduleDoctorsBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        realTimeDb = FirebaseDatabase.getInstance();
        clinicEmail = user.getEmail();
        db = FirebaseFirestore.getInstance();
        dbDoc = FirebaseFirestore.getInstance();
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        if(doc.getData().get("approvedList").toString() != "" ) {
                            doctorsList = (ArrayList<String>) doc.getData().get("approvedList");
                        } else {
                            doctorsList = new ArrayList<>();
                        }
                        clinicId = doc.getData().get("clinicID").toString();
                        clinicName = doc.getData().get("name").toString();
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
                                    db.collection("Clinics").document(doc.getId())
                                            .update("alertBox",  true);
                                    dialogInterface.dismiss();
                                }
                            });
                            scheduleAlert.show();

                        }
                    }
                }
                availabilityArray = new ArrayList<>();
                docs = new ArrayList<>();
                for(String doctor : doctorsList) {
                    db.collection("Doctors").whereEqualTo("email",doctor).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot doc : task.getResult()) {
                                    availabilityArray.add(doc.getData().get("availability").toString());
                                    String name = doc.getData().get("firstName").toString() + " " + doc.getData().get("lastName").toString();
                                    docs.add(name);
                                }
                            }
                        }
                    });
                }
            }
        });
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        binding.scheduleDate.setText("Schedule starts from " + nextMonday.toString());
        binding.confirmSchedule.setOnClickListener(this);
        binding.monday.setOnClickListener(this);
        binding.tuesday.setOnClickListener(this);
        binding.wednesday.setOnClickListener(this);
        binding.thursday.setOnClickListener(this);
        binding.friday.setOnClickListener(this);
        binding.saturday.setOnClickListener(this);
        binding.sunday.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        dbRefClinic = realTimeDb.getReference("NextClinicSchedules");
        AlertDialog.Builder doctorOptions = new AlertDialog.Builder(getActivity());
        doctorOptions.setTitle("Select Doctor");
        switch(view.getId()) {
            case R.id.confirm_schedule:
                binding.monday.setBackgroundResource(R.drawable.rounded_white_buttons);
                binding.tuesday.setBackgroundResource(R.drawable.rounded_white_buttons);
                binding.wednesday.setBackgroundResource(R.drawable.rounded_white_buttons);
                binding.thursday.setBackgroundResource(R.drawable.rounded_white_buttons);
                binding.friday.setBackgroundResource(R.drawable.rounded_white_buttons);
                binding.saturday.setBackgroundResource(R.drawable.rounded_white_buttons);
                binding.sunday.setBackgroundResource(R.drawable.rounded_white_buttons);
                Toast.makeText(getContext(), "Schedule Saved", Toast.LENGTH_SHORT).show();
                break;
            default:
                getDoctorsList(view.getId(), view);
                break;
        }
    }

    public void getDoctorsList( int destination, View v) {
        TextView destTV = (TextView) v.findViewById(destination);
        dbRefDoctor = realTimeDb.getReference("DoctorsNextSchedule");
        AlertDialog.Builder doctorOptions = new AlertDialog.Builder(getActivity());
        doctorOptions.setTitle("Select Doctor");
        ArrayList<ScheduleDoctorsData> doctors = new ArrayList<ScheduleDoctorsData>();
        ArrayList<ScheduleDoctorsData> schedule = new ArrayList<ScheduleDoctorsData>();
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialogue_layout, null);
        TextView noDoctors = (TextView) dialogView.findViewById(R.id.no_doctors);
        RecyclerView alertView = (RecyclerView) dialogView.findViewById(R.id.doctors_view);
        ValueEventListener doctorListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot doc : snapshot.getChildren()) {
                    if(docs.contains(doc.getKey()) && doc.getValue().toString().contains(destTV.getText())) {
                        ScheduleDoctorsData entry = new ScheduleDoctorsData(doc.getKey(), doc.child(destTV.getText().toString()).child("startTime").getValue().toString(),
                                doc.child(destTV.getText().toString()).child("endTime").getValue().toString(), R.drawable.doctor, R.drawable.ic_not_check, clinicName, destTV.getText().toString());
                        doctors.add(entry);
                    }
                }
                if(doctors.isEmpty()) {
                    noDoctors.setVisibility(View.VISIBLE);
                } else {
                    noDoctors.setVisibility(View.GONE);
                    DoctorListViewAdapter adapter = new DoctorListViewAdapter(getContext(), doctors, schedule, destination, v);
                    alertView.setAdapter(adapter);
                    alertView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        dbRefDoctor.addValueEventListener(doctorListener);
        doctorOptions.setView(dialogView);
        doctorOptions.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        doctorOptions.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                destTV.setBackgroundResource(R.drawable.rounded_white_buttons);
            }
        });
        doctorOptions.setCancelable(true);
        doctorOptions.show();
    }
}