package com.example.careplus.doctor.home;

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
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentManageScheduleBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageScheduleFragment extends Fragment implements View.OnClickListener{
    FragmentManageScheduleBinding binding;
    FirebaseFirestore db;
    TextView tv;
    List<String> availability;
    String doctorId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentManageScheduleBinding.inflate(inflater,container, false);
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        binding.nextAvailabilityTitle.setText("Next Availability Starting "+ nextMonday.toString());
        binding.monAN.setOnClickListener(this);
        binding.monFN.setOnClickListener(this);
        binding.tueFN.setOnClickListener(this);
        binding.tueAN.setOnClickListener(this);
        binding.wedFN.setOnClickListener(this);
        binding.wedAN.setOnClickListener(this);
        binding.thuFN.setOnClickListener(this);
        binding.thuAN.setOnClickListener(this);
        binding.friFN.setOnClickListener(this);
        binding.friAN.setOnClickListener(this);
        binding.satFN.setOnClickListener(this);
        binding.satAN.setOnClickListener(this);
        binding.sunFN.setOnClickListener(this);
        binding.sunAN.setOnClickListener(this);
        binding.updateAvailability.setOnClickListener(this);
        availability = new ArrayList<>();

        db.collection("Doctors").whereEqualTo("email", doctor.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                doctorId = doc.getId();
                            }
                        }
                    }
                });
       return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_availability:
                Map<String, Object> scheduleData= new HashMap<>();
                scheduleData.put("availability", availability);
                db.collection("Doctors").document(doctorId).update(scheduleData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Schedule Updated", Toast.LENGTH_SHORT).show();
                            binding.monFN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.monAN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.tueFN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.tueAN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.wedAN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.wedFN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.thuFN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.thuAN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.friFN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.friAN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.satFN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.satAN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.sunFN.setBackgroundResource(R.drawable.rounded_white_buttons);
                            binding.sunAN.setBackgroundResource(R.drawable.rounded_white_buttons);
                        }
                    }
                });
                break;
            default:
                tv = view.findViewById(view.getId());
                if(availability.contains(tv.getText().toString())) {
                    tv.setBackgroundResource(R.drawable.rounded_white_buttons);
                    availability.remove(tv.getText().toString());
                } else {
                    tv.setBackgroundResource(R.drawable.rounded_green_buttons);
                    availability.add(tv.getText().toString());
                }
                break;
        }
    }
}