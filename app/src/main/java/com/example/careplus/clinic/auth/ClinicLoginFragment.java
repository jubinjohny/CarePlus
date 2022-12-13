package com.example.careplus.clinic.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
                                                    Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                } else {
                                    binding.clinicEmail.setText("");
                                    binding.clinicPassword.setText("");
                                    Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                binding.clinicEmail.setText("");
                                binding.clinicPassword.setText("");
                                Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
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
        });
        binding.showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showPassword.setVisibility(View.GONE);
                binding.hidePassword.setVisibility(View.VISIBLE);
                binding.clinicPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        binding.hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showPassword.setVisibility(View.VISIBLE);
                binding.hidePassword.setVisibility(View.GONE);
                binding.clinicPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

        binding.clinicLogin.setAlpha(0f);
        binding.clinicLogin.setAlpha(0f);
        binding.clinicLogin.setTranslationY(50);
        binding.clinicLogin.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.forgotCredentials.setAlpha(0f);
        binding.forgotCredentials.setAlpha(0f);
        binding.forgotCredentials.setTranslationY(50);
        binding.forgotCredentials.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.clinicRegister.setAlpha(0f);
        binding.clinicRegister.setAlpha(0f);
        binding.clinicRegister.setTranslationY(50);
        binding.clinicRegister.animate().alpha(1f).translationYBy(-50).setDuration(1000);
    }
}