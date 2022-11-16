package com.example.careplus.clinic.auth;

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
import com.example.careplus.clinic.auth.home.ClinicHomeActivity;
import com.example.careplus.databinding.FragmentClinicLoginBinding;
import com.example.careplus.doctor.home.DoctorHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ClinicLoginFragment extends Fragment {
    FragmentClinicLoginBinding binding;
    FirebaseAuth clinicAuth;
    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClinicLoginBinding.inflate(inflater, container,false);
        clinicAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding.getRoot().getBackground().setAlpha(90);
        binding.clinicLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.clinicEmail.getText().toString().isEmpty()) {
                    binding.clinicEmail.setError("Username is empty");
                    binding.clinicEmail.requestFocus();
                    return;
                }
                if(binding.clinicPassword.getText().toString().isEmpty()) {
                    binding.clinicPassword.setError("Password is empty");
                    binding.clinicPassword.requestFocus();
                    return;
                }
                db.collection("Clinics").whereEqualTo("email", binding.clinicEmail.getText().toString())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                if(task.getResult().size() > 0) {
                                    clinicAuth.signInWithEmailAndPassword(binding.clinicEmail.getText().toString(), binding.clinicPassword.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()) {
                                                    binding.clinicEmail.setText("");
                                                    binding.clinicPassword.setText("");
                                                    Intent intent = new Intent(getActivity(), ClinicHomeActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    binding.clinicEmail.setText("");
                                                    binding.clinicPassword.setText("");
                                                    Toast.makeText(getActivity(), "No user found", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                } else {
                                    binding.clinicEmail.setText("");
                                    binding.clinicPassword.setText("");
                                    Toast.makeText(getActivity(), "No user found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                binding.clinicEmail.setText("");
                                binding.clinicPassword.setText("");
                                Toast.makeText(getActivity(), "No user found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
        binding.clinicRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClinicRegisterFragmentFirst registerFragment = new ClinicRegisterFragmentFirst();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_login_fragment_container, registerFragment, "New Frag")
                        .commit();
            }
        });
        return binding.getRoot();
    }
}