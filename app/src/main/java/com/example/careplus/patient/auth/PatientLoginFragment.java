package com.example.careplus.patient.auth;

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
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
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
                db.collection("Patients").whereEqualTo("email", binding.userEmail.getText().toString())
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
                                            Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                binding.userEmail.setText("");
                                binding.userPassword.setText("");
                                Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            binding.userEmail.setText("");
                            binding.userPassword.setText("");
                            Toast.makeText(getActivity(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getContext(),"User Not Found" , Toast.LENGTH_SHORT).show();
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
                binding.userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        binding.hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.showPassword.setVisibility(View.VISIBLE);
                binding.hidePassword.setVisibility(View.GONE);
                binding.userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
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

        binding.userLogin.setAlpha(0f);
        binding.userLogin.setAlpha(0f);
        binding.userLogin.setTranslationY(50);
        binding.userLogin.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.forgotCredentials.setAlpha(0f);
        binding.forgotCredentials.setAlpha(0f);
        binding.forgotCredentials.setTranslationY(50);
        binding.forgotCredentials.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.userRegister.setAlpha(0f);
        binding.userRegister.setAlpha(0f);
        binding.userRegister.setTranslationY(50);
        binding.userRegister.animate().alpha(1f).translationYBy(-50).setDuration(1000);
    }
}