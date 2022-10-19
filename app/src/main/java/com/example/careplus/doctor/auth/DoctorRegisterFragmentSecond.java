package com.example.careplus.doctor.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.careplus.databinding.FragmentDoctorRegisterSecondBinding;
import com.example.careplus.doctor.home.DoctorHomeActivity;
import com.example.careplus.localStorage.NewClinic;
import com.example.careplus.localStorage.NewDoctor;
import com.example.careplus.localStorage.SQLiteDBHelperDoctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorRegisterFragmentSecond extends Fragment {
    FragmentDoctorRegisterSecondBinding binding;
    SQLiteDBHelperDoctor DB;
    private FirebaseFirestore dataBase;
    private FirebaseAuth doctorAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorRegisterSecondBinding.inflate(inflater, container, false);
        DB = new SQLiteDBHelperDoctor(getContext());
        dataBase = FirebaseFirestore.getInstance();
        doctorAuth = FirebaseAuth.getInstance();
        binding.doctorDoctorSpecialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder specializationTypes = new AlertDialog.Builder(getActivity());
                specializationTypes.setTitle("Select Type");
                String[] options = {"Physiotherapy", "Optometry", "Dentistry"};
                specializationTypes.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int selection) {
                        dialogInterface.dismiss();
                        switch(selection) {
                            case 0:
                                binding.doctorDoctorSpecialization.setText("Physiotherapy");
                                break;
                            case 1:
                                binding.doctorDoctorSpecialization.setText("Optometry");
                                break;
                            case 2:
                                binding.doctorDoctorSpecialization.setText("Dentistry");
                                break;
                            default:
                                break;
                        }
                    }
                });
                specializationTypes.show();
            }
        });
        binding.registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String specialization = binding.doctorDoctorSpecialization.getText().toString();
                String password = binding.doctorPassword.getText().toString();
                String passwordConfirm = binding.doctorPasswordConfirm.getText().toString();

                if(specialization.isEmpty()) {
                    binding.doctorDoctorSpecialization.setError("First Name is Required");
                    binding.doctorDoctorSpecialization.requestFocus();
                    return;
                }
                if(password.isEmpty()) {
                    binding.doctorPassword.setError("password is required");
                    binding.doctorPassword.requestFocus();
                    return;
                }
                if(passwordConfirm.isEmpty()) {
                    binding.doctorPasswordConfirm.setError("Confirm your password");
                    binding.doctorPasswordConfirm.requestFocus();
                    return;
                } else if (passwordConfirm.contentEquals(password) == false) {
                    binding.doctorPasswordConfirm.setError("password mismatch");
                    binding.doctorPasswordConfirm.requestFocus();
                    return;
                }
                String doctorID = DB.getDoctorID();
                Boolean updateDoctorData = DB.updateDoctorData(doctorID, specialization, password, "");
                if(updateDoctorData == true) {
                    Cursor doctor = DB.getAllData();
                    if(doctor.getCount() == 0) {
                        Toast.makeText(getActivity(), "No Entry Exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(doctor.moveToLast()) {
                        NewDoctor newDoctor = new NewDoctor(doctor.getString(0), doctor.getString(1), doctor.getString(2), doctor.getString(3),
                                doctor.getString(4), doctor.getString(5), doctor.getString(6));
                        dataBase.collection("Doctors").document(DB.getDoctorID())
                                .set(newDoctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        doctorAuth.createUserWithEmailAndPassword(DB.getDoctorEmail(), DB.getDoctorPassword())
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                    }
                                                });
                                        Intent i = new Intent(getActivity(), DoctorHomeActivity.class);
                                        startActivity(i);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Data adding failed", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }
            }
        });
        return binding.getRoot();
    }
}