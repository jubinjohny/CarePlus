package com.example.careplus.clinic.auth;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.clinic.auth.home.ClinicHomeActivity;
import com.example.careplus.databinding.FragmentClinicRegistrationLastBinding;
import com.example.careplus.localStorage.NewClinic;
import com.example.careplus.localStorage.NewPatient;
import com.example.careplus.localStorage.SQLiteDBHelperClinic;
import com.example.careplus.patient.home.PatientHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClinicRegistrationFragmentLast extends Fragment {
    FragmentClinicRegistrationLastBinding binding;
    SQLiteDBHelperClinic DB;
    private FirebaseFirestore dataBase;
    private FirebaseAuth clinicAuth;
    private static final String TAG = "test";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentClinicRegistrationLastBinding.inflate(inflater,container,false);
       DB = new SQLiteDBHelperClinic(getContext());
        dataBase = FirebaseFirestore.getInstance();
        clinicAuth = FirebaseAuth.getInstance();
       binding.completeButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String password = binding.clinicPassword.getText().toString();
               String passwordConf = binding.clinicPasswordConfirm.getText().toString();
               if(password.isEmpty()) {
                   binding.clinicPassword.setError("password is required");
                   binding.clinicPassword.requestFocus();
                   return;
               }
               if(passwordConf.isEmpty()) {
                   binding.clinicPasswordConfirm.setError("Confirm your password");
                   binding.clinicPasswordConfirm.requestFocus();
                   return;
               } else if (passwordConf.contentEquals(password) == false) {
                   binding.clinicPasswordConfirm.setError("password mismatch");
                   binding.clinicPasswordConfirm.requestFocus();
                   return;
               }
               String clinicID = DB.getClinicID();
               Boolean updateClinicDetails = DB.updateClinicLast(clinicID, password);
               if(updateClinicDetails == true) {
                   Cursor clinic = DB.getAllData();
                   if(clinic.getCount() == 0) {
                       Toast.makeText(getActivity(), "No Entry Exists", Toast.LENGTH_SHORT).show();
                       return;
                   }
                   if(clinic.moveToLast()) {
                       NewClinic newClinic = new NewClinic(clinic.getString(0), clinic.getString(1), clinic.getString(2), clinic.getString(3),
                               clinic.getString(4), clinic.getString(5), clinic.getString(6), clinic.getString(7), clinic.getString(8),
                               clinic.getInt(9), clinic.getInt(10), clinic.getInt(13),clinic.getInt(14),clinic.getInt(11),clinic.getInt(12),
                               clinic.getInt(15),clinic.getInt(16),clinic.getString(17),clinic.getString(18),clinic.getString(19), clinic.getString(20),
                               clinic.getString(21), clinic.getString(22));
                       Log.d("Clinic", newClinic.toString());
                       dataBase.collection("Clinics").document(DB.getClinicID())
                               .set(newClinic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Log.d(TAG, DB.getClinicEmail() + DB.getClinicPassword());
                                       clinicAuth.createUserWithEmailAndPassword(DB.getClinicEmail(), DB.getClinicPassword())
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
                                       Intent i = new Intent(getActivity(), ClinicHomeActivity.class);
                                       startActivity(i);
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(getActivity(), "Data adding failed", Toast.LENGTH_LONG).show();
                                   }
                               });
                   }
               } else {
                   Toast.makeText(getActivity(), "Data not updated", Toast.LENGTH_SHORT).show();
               }
       }
    });
        return binding.getRoot();
    }
}