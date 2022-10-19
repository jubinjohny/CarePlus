package com.example.careplus.patient.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.careplus.databinding.FragmentPatientRegisterFourthBinding;
import com.example.careplus.localStorage.SQLiteDBHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientRegisterFragmentFourth extends Fragment {
    FragmentPatientRegisterFourthBinding binding;
    SQLiteDBHelper DB;
    FirebaseFirestore db;
    String[] options;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentPatientRegisterFourthBinding.inflate(inflater,container, false);
        DB = new SQLiteDBHelper(getContext());
        db = FirebaseFirestore.getInstance();

        CollectionReference docRef = db.collection("InsuranceProviders");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    options = new String[task.getResult().size()];
                    int i = 0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        options[i] = doc.getId().toString();
                        i++;
                    }
                }
            }
        });
        binding.insuranceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder insuranceOptions = new AlertDialog.Builder(getActivity());
                insuranceOptions.setTitle("Select Insurance Provider");
                if(options.length > 0) {
                    insuranceOptions.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.insuranceProvider.setText(options[i]);
                        }
                    });
                    insuranceOptions.show();
                } else {
                    Toast.makeText(getActivity(), "No list available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.userNextFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = binding.passwordPatient.getText().toString().trim();
                String passwordConf = binding.passwordPatientConfirm.getText().toString().trim();
                String insuranceProvider = binding.insuranceProvider.getText().toString().trim();
                if(password.isEmpty()) {
                    binding.passwordPatient.setError("password is required");
                    binding.passwordPatient.requestFocus();
                    return;
                }
                if(passwordConf.isEmpty()) {
                    binding.passwordPatientConfirm.setError("Confirm your password");
                    binding.passwordPatientConfirm.requestFocus();
                    return;
                } else if (passwordConf.contentEquals(password) == false) {
                    binding.passwordPatientConfirm.setError("password mismatch");
                    binding.passwordPatientConfirm.requestFocus();
                    return;
                }
                String patientID = DB.getPatientID();
                Boolean updatePatientDetails = DB.updatePatientFourth(patientID,insuranceProvider, password);
                if(updatePatientDetails == true) {
                    PatientRegisterFragmentLast nextFrag = new PatientRegisterFragmentLast();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.patient_login_fragment_container, nextFrag, "New Frag")
                            .addToBackStack("fourth").commit();
                } else {
                    Toast.makeText(getActivity(), "Data not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}