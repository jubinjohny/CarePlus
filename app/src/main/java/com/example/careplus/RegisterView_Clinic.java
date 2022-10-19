package com.example.careplus;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterView_Clinic extends Fragment {
    private FirebaseAuth clinicAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_layout_clinic, container, false);
        EditText clinic_name = root.findViewById(R.id.name_clinic);
        EditText clinic_email = root.findViewById(R.id.email_clinic);
        EditText clinic_id = root.findViewById(R.id.clinic_id);
        EditText clinic_location = root.findViewById(R.id.clinic_location);
        EditText clinic_password = root.findViewById(R.id.password_clinic);
        EditText clinic_passcon = root.findViewById(R.id.confirm_password_clinic);
        TextView clinic_reg = root.findViewById(R.id.register_clinic);

        clinic_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = clinic_name.getText().toString().trim();
                String email = clinic_email.getText().toString().trim();
                String id = clinic_id.getText().toString().trim();
                String location = clinic_location.getText().toString().trim();
                String password = clinic_password.getText().toString().trim();
                String confirmPass = clinic_passcon.getText().toString().trim();

                if(name.isEmpty()) {
                    clinic_name.setError("Name is required");
                    clinic_name.requestFocus();
                    return;
                }
                if(email.isEmpty()) {
                    clinic_email.setError("Email is required");
                    clinic_email.requestFocus();
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    clinic_email.setError("Please provide valid email");
                    clinic_email.requestFocus();
                    return;
                }
                if(id.isEmpty()) {
                    clinic_id.setError("ID is required");
                    clinic_id.requestFocus();
                    return;
                }
                if(location.isEmpty()) {
                    clinic_location.setError("location is required");
                    clinic_location.requestFocus();
                    return;
                }
                if(password.isEmpty()) {
                    clinic_password.setError("password is required");
                    clinic_password.requestFocus();
                    return;
                }
                if(confirmPass.isEmpty()) {
                    clinic_passcon.setError("Confirm your password");
                    clinic_passcon.requestFocus();
                    return;
                } else if (confirmPass.contentEquals(password) == false) {
                    clinic_passcon.setError("password mismatch");
                    clinic_passcon.requestFocus();
                    return;
                }


            clinicAuth = FirebaseAuth.getInstance();
            clinicAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
//                                ClinicSignUpData clinic = new ClinicSignUpData(name, email, id, location, password);
//                                FirebaseDatabase.getInstance().getReference("Clinics")
//                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                        .setValue(clinic).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful()) {
//                                                    Toast.makeText(getContext(), "Successfully Added Clinic", Toast.LENGTH_SHORT).show();
//                                                    return;
//                                                } else {
//                                                    Toast.makeText(getContext(), "Failed to Register", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
                                Toast.makeText(getContext(), "Successfully Added Clinic", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            } else { Toast.makeText(getContext(), "Failed to Register", Toast.LENGTH_SHORT).show(); }
                        }
                    });
            }
        });
        return root;
    }
}