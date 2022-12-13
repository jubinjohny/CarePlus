package com.example.careplus.doctor.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
        binding.getRoot().getBackground().setAlpha(90);
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
                                                        Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    } else {
                                        binding.doctorPassword.setText("");
                                        binding.doctorEmail.setText("");
                                        Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    binding.doctorPassword.setText("");
                                    binding.doctorEmail.setText("");
                                    Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
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
        binding.forgotCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialogue);
                EditText getEmail = (EditText) dialog.findViewById(R.id.email);
                Button okBtn = (Button) dialog.findViewById(R.id.btn_ok);
                Button cancelBtn = (Button) dialog.findViewById(R.id.btn_cancel);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(getEmail.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(),"Incorrect Credentials" , Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(true);
                dialog.show();
            }
        });binding.showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showPassword.setVisibility(View.GONE);
                binding.hidePassword.setVisibility(View.VISIBLE);
                binding.doctorPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        binding.hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showPassword.setVisibility(View.VISIBLE);
                binding.hidePassword.setVisibility(View.GONE);
                binding.doctorPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        this.startAnimations();
        return binding.getRoot();
    }

    public void startAnimations() {
        binding.usernameLayout.setAlpha(0f);
        binding.usernameLayout.setAlpha(0f);
        binding.usernameLayout.setTranslationY(50);
        binding.usernameLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.passwordLayout.setAlpha(0f);
        binding.passwordLayout.setAlpha(0f);
        binding.passwordLayout.setTranslationY(50);
        binding.passwordLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.doctorLogin.setAlpha(0f);
        binding.doctorLogin.setAlpha(0f);
        binding.doctorLogin.setTranslationY(50);
        binding.doctorLogin.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.forgotCredentials.setAlpha(0f);
        binding.forgotCredentials.setAlpha(0f);
        binding.forgotCredentials.setTranslationY(50);
        binding.forgotCredentials.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.doctorRegister.setAlpha(0f);
        binding.doctorRegister.setAlpha(0f);
        binding.doctorRegister.setTranslationY(50);
        binding.doctorRegister.animate().alpha(1f).translationYBy(-50).setDuration(1000);
    }
}