package com.example.careplus.clinic.auth.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentScheduleDoctorsBinding;
import com.example.careplus.localStorage.DaySchedule;
import com.example.careplus.localStorage.NextSchedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Locale;

public class ScheduleDoctorsFragment extends Fragment implements View.OnClickListener {
    FragmentScheduleDoctorsBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    ArrayList<String> doctorsList;
    ArrayList<String> availabilityArray;
    ArrayList<String> docs;
    String clinicId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScheduleDoctorsBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        doctorsList = (ArrayList<String>) doc.getData().get("approvedList");
                        clinicId = doc.getData().get("clinicID").toString();
                    }
                }
                Log.d("Test :", doctorsList.toString());
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
                                Log.d("Avaial", availabilityArray.get(0).toString());
                            }
                        }
                    });
                }
            }
        });
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        binding.scheduleDate.setText(nextMonday.toString());
        binding.mondayFn.setOnClickListener(this);
        binding.mondayAn.setOnClickListener(this);
        binding.tuesdayFn.setOnClickListener(this);
        binding.tuesdayAn.setOnClickListener(this);
        binding.wednesdayAn.setOnClickListener(this);
        binding.wednesdayFn.setOnClickListener(this);
        binding.thursdayAn.setOnClickListener(this);
        binding.thursdayFn.setOnClickListener(this);
        binding.fridayFn.setOnClickListener(this);
        binding.fridayAn.setOnClickListener(this);
        binding.saturdayAn.setOnClickListener(this);
        binding.saturdayFn.setOnClickListener(this);
        binding.sundayFn.setOnClickListener(this);
        binding.sundayAn.setOnClickListener(this);
        binding.updateAvailability.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        db = FirebaseFirestore.getInstance();
        AlertDialog.Builder doctorOptions = new AlertDialog.Builder(getActivity());
        doctorOptions.setTitle("Select Doctor");
        switch(view.getId()) {
            case R.id.monday_an:
              getDoctorsList("Monday AN",R.id.monday_an, view);
                break;
            case R.id.monday_fn:
                getDoctorsList("Monday FN",R.id.monday_fn, view);
                break;
            case R.id.tuesday_an:
                getDoctorsList("Tuesday AN",R.id.tuesday_an, view);
                break;
            case R.id.tuesday_fn:
                getDoctorsList("Tuesday FN",R.id.tuesday_fn, view);
                break;
            case R.id.wednesday_an:
                getDoctorsList("Wednesday AN",R.id.wednesday_an, view);
                break;
            case R.id.wednesday_fn:
                getDoctorsList("Wednesday FN",R.id.wednesday_fn, view);
                break;
            case R.id.thursday_fn:
                getDoctorsList("Thursday FN",R.id.thursday_fn, view);
                break;
            case R.id.thursday_an:
                getDoctorsList("Thursday AN",R.id.thursday_an, view);
                break;
            case R.id.friday_an:
                getDoctorsList("Friday AN",R.id.friday_an, view);
                break;
            case R.id.friday_fn:
                getDoctorsList("Friday FN",R.id.friday_fn, view);
                break;
            case R.id.saturday_fn:
                getDoctorsList("Saturday FN",R.id.saturday_fn, view);
                break;
            case R.id.saturday_an:
                getDoctorsList("Saturday AN",R.id.saturday_an, view);
                break;
            case R.id.sunday_fn:
                getDoctorsList("Sunday FN",R.id.sunday_fn, view);
                break;
            case R.id.sunday_an:
                getDoctorsList("Sunday AN",R.id.sunday_an, view);
                break;
            case R.id.update_availability:
                DaySchedule mondaySchedule = new DaySchedule(binding.mondayFn.getText().toString() == "FN" ? "" : binding.mondayFn.getText().toString(), binding.mondayAn.getText().toString() == "AN" ? "" : binding.mondayAn.getText().toString());
                DaySchedule tuesdaySchedule = new DaySchedule(binding.tuesdayFn.getText().toString() == "FN" ? "" : binding.tuesdayFn.getText().toString(), binding.tuesdayAn.getText().toString() == "AN" ? "" :  binding.tuesdayAn.getText().toString());
                DaySchedule wednesdaySchedule = new DaySchedule(binding.wednesdayFn.getText().toString() == "FN" ? "" : binding.wednesdayFn.getText().toString(), binding.wednesdayAn.getText().toString() == "AN" ? "" : binding.wednesdayAn.getText().toString());
                DaySchedule thursdaySchedule = new DaySchedule(binding.thursdayFn.getText().toString() == "FN" ? "" : binding.thursdayFn.getText().toString(), binding.thursdayAn.getText().toString() == "AN" ? "": binding.thursdayAn.getText().toString());
                DaySchedule fridaySchedule = new DaySchedule(binding.fridayFn.getText().toString() == "FN" ? "" : binding.fridayFn.getText().toString(), binding.fridayAn.getText().toString() == "AN" ? "" : binding.fridayAn.getText().toString());
                DaySchedule saturdaySchedule = new DaySchedule(binding.saturdayFn.getText().toString() == "FN" ? "" : binding.saturdayFn.getText().toString(), binding.saturdayAn.getText().toString() == "AN" ? "" : binding.saturdayAn.getText().toString());
                DaySchedule sundaySchedule = new DaySchedule(binding.sundayFn.getText().toString() == "FN" ? "" : binding.sundayFn.getText().toString(), binding.sundayAn.getText().toString() == "AN" ? "" : binding.sundayAn.getText().toString());
                NextSchedule nextSchedule = new NextSchedule(mondaySchedule, tuesdaySchedule, wednesdaySchedule, thursdaySchedule, fridaySchedule, saturdaySchedule, sundaySchedule);
                db.collection("Clinics").document(clinicId).update("nextSchedule", nextSchedule).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            binding.mondayFn.setText("FN");
                            binding.mondayAn.setText("AN");
                            binding.tuesdayAn.setText("AN");
                            binding.tuesdayFn.setText("FN");
                            binding.wednesdayAn.setText("AN");
                            binding.wednesdayFn.setText("FN");
                            binding.thursdayFn.setText("FN");
                            binding.thursdayAn.setText("AN");
                            binding.fridayFn.setText("FN");
                            binding.fridayAn.setText("AN");
                            binding.saturdayFn.setText("FN");
                            binding.saturdayAn.setText("AN");
                            binding.sundayFn.setText("FN");
                            binding.sundayAn.setText("AN");
                        }else {
                            Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    public void getDoctorsList(String shift, int destination, View v) {
        TextView destTV = (TextView) v.findViewById(destination);
        AlertDialog.Builder doctorOptions = new AlertDialog.Builder(getActivity());
        doctorOptions.setTitle("Select Doctor");
        ArrayList<String> doctors = new ArrayList<>();
        int i = 0;
        for(String doctor : doctorsList) {
            if(availabilityArray.get(i).contains(shift)) {
                doctors.add(docs.get(i));
            }
            i++;
        }
        if(doctors.size() > 0) {
            doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    destTV.setText(doctors.get(i));
                }
            });
            doctorOptions.setNegativeButton("Close", null);
        } else {
            doctorOptions.setMessage("No Doctors Available");
            doctorOptions.setNegativeButton("OK", null);
        }
        doctorOptions.show();
    }
}