package com.example.careplus.patient.auth;

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
import com.example.careplus.databinding.FragmentLoginPatientBinding;
import com.example.careplus.patient.home.PatientHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PatientLoginFragment extends Fragment {
    FragmentLoginPatientBinding binding;
    FirebaseAuth patientAuth;
    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginPatientBinding.inflate(inflater, container,false);
        binding.getRoot().getBackground().setAlpha(90);
        patientAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding.userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.userEmail.getText().toString().isEmpty()) {
                    binding.userEmail.setError("Username is empty");
                    binding.userEmail.requestFocus();
                    return;
                }
                if(binding.userPassword.getText().toString().isEmpty()) {
                    binding.userPassword.setError("Password is empty");
                    binding.userPassword.requestFocus();
                    return;
                }
                db.collection("Users").whereEqualTo("email", binding.userEmail.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().size() > 0) {
                                patientAuth.signInWithEmailAndPassword(binding.userEmail.getText().toString(), binding.userPassword.getText().toString())
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            binding.userEmail.setText("");
                                            binding.userPassword.setText("");
                                            Intent intent = new Intent(getActivity(), PatientHomeActivity.class);
                                            startActivity(intent);
                                        } else {
                                            binding.userEmail.setText("");
                                            binding.userPassword.setText("");
                                            Toast.makeText(getActivity(), "No user found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                binding.userEmail.setText("");
                                binding.userPassword.setText("");
                                Toast.makeText(getActivity(), "No user found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            binding.userEmail.setText("");
                            binding.userPassword.setText("");
                            Toast.makeText(getActivity(), "No user found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatientRegisterFragmentFirst registerFragment = new PatientRegisterFragmentFirst();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.patient_login_fragment_container, registerFragment, "New Frag")
                        .commit();
            }
        });
        return binding.getRoot();
    }
}