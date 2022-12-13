package com.example.careplus.clinic.auth.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.careplus.adapters.AppointmentsViewAdapter;
import com.example.careplus.adapters.ClinicScheduleViewAdapter;
import com.example.careplus.adapters.DoctorListViewAdapter;
import com.example.careplus.adapters.ScheduleViewAdapter;
import com.example.careplus.databinding.FragmentAvailableTimeSlotBinding;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.example.careplus.localStorage.ScheduleDoctorsData;
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

import org.w3c.dom.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AvailableTimeSlotFragment extends Fragment implements View.OnClickListener {
    FragmentAvailableTimeSlotBinding binding;
    FirebaseUser user;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    String clinicName;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAvailableTimeSlotBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                clinicName = doc.getData().get("name").toString();
                            }
                        }
                    }
                });
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
        TextView dest = (TextView) view.findViewById(view.getId());
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("NextSchedule").child(clinicName);
        AlertDialog.Builder doctorOptions = new AlertDialog.Builder(getActivity());
        doctorOptions.setTitle("Schedule for " + dest.getText().toString());
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_dialogue_layout, null);
        ArrayList<ScheduleDoctorsData> doctors = new ArrayList<ScheduleDoctorsData>();
        RecyclerView alertView = (RecyclerView) dialogView.findViewById(R.id.doctors_view);
        ValueEventListener doctorListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot doc : snapshot.getChildren()) {
                    if(doc.getKey().contentEquals(dest.getText().toString())) {
                        for(DataSnapshot each : doc.getChildren()) {
                            ScheduleDoctorsData entry = new ScheduleDoctorsData(each.child("doctorName").getValue().toString(), each.child("startTime").getValue().toString(),
                                    each.child("endTime").getValue().toString(), R.drawable.doctor);
                            doctors.add(entry);
                        }
                    }
                }
                if(doctors.size() > 0) {
                    ClinicScheduleViewAdapter adapter = new ClinicScheduleViewAdapter(getContext(), doctors);
                    alertView.setAdapter(adapter);
                    alertView.setLayoutManager(new LinearLayoutManager(getContext()));
                    doctorOptions.setView(dialogView);
                    doctorOptions.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    doctorOptions.setCancelable(true);
                    doctorOptions.show();
                } else {
                    Toast.makeText(getActivity(), "No Doctors Scheduled", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        dbRef.addValueEventListener(doctorListener);
    }
}