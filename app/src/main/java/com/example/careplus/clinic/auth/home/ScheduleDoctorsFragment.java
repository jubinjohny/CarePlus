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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ScheduleDoctorsFragment extends Fragment implements View.OnClickListener {
    FragmentScheduleDoctorsBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    ArrayList<String> doctorsList;
    ArrayList<String> availabilityArray;
    ArrayList<String> docs;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    String clinicId, clinicName, clinicEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScheduleDoctorsBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        clinicEmail = user.getEmail();
        db = FirebaseFirestore.getInstance();
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        doctorsList = (ArrayList<String>) doc.getData().get("approvedList");
                        clinicId = doc.getData().get("clinicID").toString();
                        clinicName = doc.getData().get("name").toString();
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
        binding.confirmSchedule.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("NextClinicSchedules");
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
            case R.id.confirm_schedule:
                Map<String, Object> nextSchedule = new HashMap<>();
                nextSchedule.put("mondayFN", binding.mondayFn.getText().toString());
                nextSchedule.put("mondayAN", binding.mondayAn.getText().toString());
                nextSchedule.put("tuesdayFN", binding.tuesdayFn.getText().toString());
                nextSchedule.put("tuesdayAN", binding.tuesdayAn.getText().toString());
                nextSchedule.put("wednesdayFN", binding.wednesdayFn.getText().toString());
                nextSchedule.put("wednesdayAN", binding.wednesdayAn.getText().toString());
                nextSchedule.put("thursdayFN", binding.thursdayFn.getText().toString());
                nextSchedule.put("thursdayAN", binding.thursdayAn.getText().toString());
                nextSchedule.put("fridayFN", binding.fridayFn.getText().toString());
                nextSchedule.put("fridayAN", binding.fridayAn.getText().toString());
                nextSchedule.put("saturdayFN", binding.saturdayFn.getText().toString());
                nextSchedule.put("saturdayAN", binding.saturdayAn.getText().toString());
                nextSchedule.put("sundayFN", binding.sundayFn.getText().toString());
                nextSchedule.put("sundayAN", binding.sundayAn.getText().toString());
                Map<String, Object> fin = new HashMap<>();
                fin.put(clinicName, nextSchedule);
                Log.d("Test", fin.toString());
                dbRef.updateChildren(fin).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.mondayFn.setText("FN");
                        binding.mondayAn.setText("AN");

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