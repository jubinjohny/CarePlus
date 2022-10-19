package com.example.careplus.patient.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentPatientRegisterLastBinding;
import com.example.careplus.localStorage.NewPatient;
import com.example.careplus.localStorage.SQLiteDBHelper;
import com.example.careplus.localStorage.SecurityQuestions;
import com.example.careplus.patient.home.PatientHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;


public class PatientRegisterFragmentLast extends Fragment {

    FragmentPatientRegisterLastBinding binding;
    public final String TAG = "this";
    private FirebaseFirestore dataBase;
    String[] question_array_1;
    private FirebaseAuth patientAuth;
    SQLiteDBHelper DB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentPatientRegisterLastBinding.inflate(inflater, container, false);
        DB = new SQLiteDBHelper(getContext());
        dataBase = FirebaseFirestore.getInstance();
        patientAuth = FirebaseAuth.getInstance();
        question_array_1 = getResources().getStringArray(R.array.security_questions);
        binding.securityQ1Patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder secQuestions = new AlertDialog.Builder(getActivity());
                secQuestions.setTitle("Select Question");
                secQuestions.setItems(question_array_1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selection) {
                        dialogInterface.dismiss();
                        binding.securityQ1Patient.setText(question_array_1[selection]);
                    }
                });
                secQuestions.show();
            }
        });
        binding.securityQ2Patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder secQuestions = new AlertDialog.Builder(getActivity());
                secQuestions.setTitle("Select Question");
                secQuestions.setItems(question_array_1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selection) {
                        dialogInterface.dismiss();
                        binding.securityQ2Patient.setText(question_array_1[selection]);
                    }
                });
                secQuestions.show();
            }
        });
        binding.securityQ3Patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder secQuestions = new AlertDialog.Builder(getActivity());
                secQuestions.setTitle("Select Question");
                secQuestions.setItems(question_array_1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selection) {
                        dialogInterface.dismiss();
                        binding.securityQ3Patient.setText(question_array_1[selection]);
                    }
                });
                secQuestions.show();
            }
        });

        binding.userNextLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Q1 = binding.securityQ1Patient.getText().toString();
                String Q2 = binding.securityQ2Patient.getText().toString();
                String Q3 = binding.securityQ3Patient.getText().toString();
                String A1 = binding.securityA1Patient.getText().toString();
                String A2 = binding.securityA2Patient.getText().toString();
                String A3 = binding.securityA3Patient.getText().toString();
                if (A1.isEmpty()) {
                    binding.securityA1Patient.setError("Need an answer");
                    binding.securityA1Patient.requestFocus();
                    return;
                }
                if (A2.isEmpty()) {
                    binding.securityA2Patient.setError("Need an answer");
                    binding.securityA2Patient.requestFocus();
                    return;
                }
                if (A3.isEmpty()) {
                    binding.securityA3Patient.setError("Need an answer");
                    binding.securityA3Patient.requestFocus();
                    return;
                }
                if (Q1.isEmpty()) {
                    binding.securityQ1Patient.setError("Select a question");
                    binding.securityQ1Patient.requestFocus();
                    return;
                } else if (Q1 == Q2 || Q1 == Q2) {
                    binding.securityQ1Patient.setError("Questions should be different");
                    binding.securityQ1Patient.requestFocus();
                    return;
                }
                if (Q2.isEmpty()) {
                    binding.securityQ2Patient.setError("Select a question");
                    binding.securityQ2Patient.requestFocus();
                    return;
                }else if (Q2 == Q1 || Q2 == Q3) {
                    binding.securityQ2Patient.setError("Questions should be different");
                    binding.securityQ2Patient.requestFocus();
                    return;
                }
                if (Q3.isEmpty()) {
                    binding.securityQ3Patient.setError("Select a question");
                    binding.securityQ3Patient.requestFocus();
                    return;
                }else if (Q3 == Q1 || Q3 == Q2) {
                    binding.securityQ3Patient.setError("Questions should be different");
                    binding.securityQ3Patient.requestFocus();
                    return;
                }
                Cursor patient = DB.getAllData();
                if(patient.getCount() == 0) {
                    Toast.makeText(getActivity(), "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(patient.moveToLast()) {
                    NewPatient newPatient = new NewPatient(patient.getString(0), patient.getString(1), patient.getString(2), patient.getString(3),
                            patient.getString(4), patient.getString(5), patient.getString(6), patient.getString(7), patient.getString(8),
                            patient.getString(9), patient.getString(10), patient.getString(11), patient.getString(12));
                    SecurityQuestions secQues = new SecurityQuestions(Q1, Q2, Q3, A1, A2, A3);
                    dataBase.collection("Users").document(DB.getPatientID())
                            .set(newPatient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dataBase.collection("SecurityQuestions").document(DB.getPatientID())
                                            .set(secQues).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();
                                                    patientAuth.createUserWithEmailAndPassword(DB.getPatientEmail(), DB.getPatientPassword())
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
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Data addition failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
//                                    Boolean deleteUserFromLocal = DB.deletePatientData(patientID);
                                    Intent i = new Intent(getActivity(), PatientHomeActivity.class);
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
        });
        return binding.getRoot();
    }
}