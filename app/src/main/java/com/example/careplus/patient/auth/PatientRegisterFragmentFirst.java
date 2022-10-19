package com.example.careplus.patient.auth;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentRegisterPatientFirstBinding;
import com.example.careplus.localStorage.SQLiteDBHelper;
import java.util.Date;
import java.util.UUID;

public class PatientRegisterFragmentFirst extends Fragment {

    FragmentRegisterPatientFirstBinding binding;
    SQLiteDBHelper DB;
    private static final String TAG = "test";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterPatientFirstBinding.inflate(inflater, container, false);
        DB = new SQLiteDBHelper(getContext());

        binding.userNextFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = binding.firstNameUser.getText().toString();
                String lName = binding.lastNameUser.getText().toString();
                String email = binding.emailUser.getText().toString();
                if(fName.isEmpty()) {
                    binding.firstNameUser.setError("First Name is Required");
                    binding.firstNameUser.requestFocus();
                    return;
                }
                if(lName.isEmpty()) {
                    binding.lastNameUser.setError("Last Name is Required");
                    binding.lastNameUser.requestFocus();
                    return;
                }
                if(email.isEmpty()) {
                    binding.emailUser.setError("Email is required");
                    binding.emailUser.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailUser.setError("Invalid email");
                    binding.emailUser.requestFocus();
                    return;
                }
                Date date = new Date();
                String uniquePatientID = "" + date.getTime();
                Boolean initializePatientDetails = DB.insertPatientInitialData(uniquePatientID,fName, lName, email, "", "", "", "", "", "", "", "", "");
                if(initializePatientDetails == true) {
                    PatientRegisterFragmentSecond nextFrag = new PatientRegisterFragmentSecond();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.patient_login_fragment_container, nextFrag, "New Frag")
                            .addToBackStack("first").commit();
                } else {
                    Toast.makeText(getActivity(), "Data not inserted", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return binding.getRoot();
    }
}