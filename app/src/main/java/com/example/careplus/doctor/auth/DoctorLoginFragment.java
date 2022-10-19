package com.example.careplus.doctor.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentDoctorLoginBinding;
import com.example.careplus.doctor.home.DoctorHomeActivity;
import com.example.careplus.patient.home.PatientHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DoctorLoginFragment extends Fragment {
    FragmentDoctorLoginBinding binding;
    FirebaseAuth doctorAuth;
    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDoctorLoginBinding.inflate(inflater, container, false);
        doctorAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding.doctorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.doctorEmail.getText().toString().isEmpty()) {
                    binding.doctorEmail.setError("Username is empty");
                    binding.doctorEmail.requestFocus();
                    return;
                }
                if(binding.doctorPassword.getText().toString().isEmpty()) {
                    binding.doctorPassword.setError("Password is empty");
                    binding.doctorPassword.requestFocus();
                    return;
                }
                db.collection("Doctors").whereEqualTo("email", binding.doctorEmail.getText().toString())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().size() > 0) {
                                        doctorAuth.signInWithEmailAndPassword(binding.doctorEmail.getText().toString(), binding.doctorPassword.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()) {
                                                        binding.doctorPassword.setText("");
                                                        binding.doctorEmail.setText("");
                                                        Intent intent = new Intent(getActivity(), DoctorHomeActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(getActivity(), "No user found", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    } else {
                                        binding.doctorPassword.setText("");
                                        binding.doctorEmail.setText("");
                                        Toast.makeText(getActivity(), "No user found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    binding.doctorPassword.setText("");
                                    binding.doctorEmail.setText("");
                                    Toast.makeText(getActivity(), "User Not Found", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });
        binding.doctorRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoctorRegisterFragmentFirst registerFragment = new DoctorRegisterFragmentFirst();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.doctor_login_fragment_container, registerFragment, "New Frag")
                        .commit();
            }
        });
        return binding.getRoot();
    }
}