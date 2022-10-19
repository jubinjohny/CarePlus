package com.example.careplus;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterView_Doctor extends Fragment {
    private FirebaseAuth docAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_layout_doctor, container, false);
        EditText doc_name = root.findViewById(R.id.name_doctor);
        EditText doc_email = root.findViewById(R.id.email_doctor);
        EditText doc_license = root.findViewById(R.id.doctor_license);
        EditText doc_major = root.findViewById(R.id.doctor_major);
        EditText doc_password = root.findViewById(R.id.password_doctor);
        EditText doc_confirm = root.findViewById(R.id.doc_confirm_password);
        TextView reg_doctor = root.findViewById(R.id.doc_register);

        reg_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = doc_name.getText().toString().trim();
                String email = doc_email.getText().toString().trim();
                String license = doc_license.getText().toString().trim();
                String major = doc_major.getText().toString().trim();
                String password = doc_password.getText().toString().trim();
                String confirmPass = doc_confirm.getText().toString().trim();

                if(name.isEmpty()) {
                    doc_name.setError("Name is required");
                    doc_name.requestFocus();
                    return;
                }
                if(email.isEmpty()) {
                    doc_email.setError("Email is required");
                    doc_email.requestFocus();
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    doc_email.setError("Please provide valid email");
                    doc_email.requestFocus();
                    return;
                }
                if(license.isEmpty()) {
                    doc_license.setError("ID is required");
                    doc_license.requestFocus();
                    return;
                }
                if(major.isEmpty()) {
                    doc_major.setError("location is required");
                    doc_major.requestFocus();
                    return;
                }
                if(password.isEmpty()) {
                    doc_password.setError("password is required");
                    doc_password.requestFocus();
                    return;
                } else if(password.length() < 6 ) {
                    doc_password.setError("password length less than 6");
                    doc_password.requestFocus();
                    return;
                }
                if(confirmPass.isEmpty()) {
                    doc_confirm.setError("Confirm your password");
                    doc_confirm.requestFocus();
                    return;
                } else if (confirmPass.contentEquals(password) == false) {
                    doc_confirm.setError("password mismatch");
                    doc_confirm.requestFocus();
                    return;
                }

                docAuth = FirebaseAuth.getInstance();
                docAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Successfully Added Doctor", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                } else { Toast.makeText(getContext(), "Failed to Register Doctor", Toast.LENGTH_SHORT).show(); }
                            }
                        });
            }
        });
        return root;
    }
}