package com.example.careplus.patient.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.careplus.MainActivity;
import com.example.careplus.R;
import com.example.careplus.databinding.FragmentPatientProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PatientProfileFragment extends Fragment {
    FragmentPatientProfileBinding binding;
    FirebaseFirestore db;
    String userId, fname, lname, dob, contact, insurance;
    String[] options;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding = FragmentPatientProfileBinding.inflate(inflater,container,false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Users").whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                userId = doc.getId();
                                fname = doc.getData().get("firstName").toString();
                                lname = doc.getData().get("lastName").toString();
                                dob = doc.getData().get("dob").toString();
                                contact = doc.getData().get("email").toString();
                                insurance = doc.getData().get("insuranceProvider").toString();
                                binding.firstName.setText(fname);
                                binding.lastName.setText(lname);
                                binding.dob.setText(dob);
                                binding.contactInfo.setText(contact);
                                if(doc.getData().get("insuranceProvider").toString() != "") {
                                    binding.insurance.setText(insurance);
                                } else {
                                    binding.insurance.setText("Add Insurance Provider");
                                }
                            }
                        }else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("InsuranceProviders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    options = new String[task.getResult().size()];
                    int i = 0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        options[i] = doc.getId().toString();
                        i++;
                    }
                } else {
                    Toast.makeText(getActivity(), "Insurance Providers empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder insuranceOptions = new AlertDialog.Builder(getActivity());
                insuranceOptions.setTitle("Select Insurance Provider");
                if(options.length > 0) {
                    insuranceOptions.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.insurance.setText(options[i]);
                        }
                    });
                    insuranceOptions.show();
                } else {
                    Toast.makeText(getActivity(), "No list available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.
                        binding.dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.firstName.isEnabled()) {
                    Map<String, Object> updateData= new HashMap<>();
                    updateData.put("firstname", binding.firstName.getText().toString());
                    updateData.put("lastName", binding.lastName.getText().toString());
                    updateData.put("email", binding.contactInfo.getText().toString());
                    updateData.put("dob", binding.dob.getText().toString());
                    updateData.put("insuranceProvider", binding.insurance.getText().toString());

                    db.collection("Users").document(userId)
                            .update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    binding.firstName.setEnabled(false);
                                    binding.lastName.setEnabled(false);
                                    binding.dob.setEnabled(false);
                                    binding.contactInfo.setEnabled(false);
                                    binding.insurance.setEnabled(false);
                                    binding.update.setText("Update Profile");
                                }
                            });
                } else {
                    binding.firstName.setEnabled(true);
                    binding.lastName.setEnabled(true);
                    binding.dob.setEnabled(true);
                    binding.contactInfo.setEnabled(true);
                    binding.insurance.setEnabled(true);
                    binding.update.setText("Complete Update");
                }
            }
        });
      return binding.getRoot();
    }
}