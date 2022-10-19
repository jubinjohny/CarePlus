package com.example.careplus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterView_User extends Fragment {
    private FirebaseAuth userAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_layout_user, container, false);
        EditText user_name = root.findViewById(R.id.name_user);
        EditText user_email = root.findViewById(R.id.email_user);
        EditText user_dob = root.findViewById(R.id.dob_user);
        EditText user_password = root.findViewById(R.id.password_user);
        EditText user_confirm = root.findViewById(R.id.confirm_password_user);
        TextView reg_user = root.findViewById(R.id.user_register);

        reg_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = user_name.getText().toString().trim();
                String email = user_email.getText().toString().trim();
                String license = user_dob.getText().toString().trim();
                String password = user_password.getText().toString().trim();
                String confirmPass = user_confirm.getText().toString().trim();

                if(name.isEmpty()) {
                    user_name.setError("Name is required");
                    user_name.requestFocus();
                    return;
                }
                if(email.isEmpty()) {
                    user_email.setError("Email is required");
                    user_email.requestFocus();
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    user_email.setError("Please provide valid email");
                    user_email.requestFocus();
                    return;
                }
                if(license.isEmpty()) {
                    user_dob.setError("ID is required");
                    user_dob.requestFocus();
                    return;
                }
                if(password.isEmpty()) {
                    user_password.setError("password is required");
                    user_password.requestFocus();
                    return;
                } else if(password.length() < 6 ) {
                    user_password.setError("password length less than 6");
                    user_password.requestFocus();
                    return;
                }
                if(confirmPass.isEmpty()) {
                    user_confirm.setError("Confirm your password");
                    user_confirm.requestFocus();
                    return;
                } else if (confirmPass.contentEquals(password) == false) {
                    user_confirm.setError("password mismatch");
                    user_confirm.requestFocus();
                    return;
                }

                userAuth = FirebaseAuth.getInstance();
                userAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Successfully Added User", Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                } else { Toast.makeText(getContext(), "Failed to Register User", Toast.LENGTH_SHORT).show(); }
                            }
                        });
            }
        });
        return root;
    }
}