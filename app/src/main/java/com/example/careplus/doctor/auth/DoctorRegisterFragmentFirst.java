package com.example.careplus.doctor.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.clinic.auth.ClinicRegisterFragmentSecond;
import com.example.careplus.databinding.FragmentDoctorRegisterFirstBinding;
import com.example.careplus.localStorage.SQLiteDBHelperDoctor;

import java.util.Date;

public class DoctorRegisterFragmentFirst extends Fragment {
    FragmentDoctorRegisterFirstBinding binding;
    SQLiteDBHelperDoctor DB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorRegisterFirstBinding.inflate(inflater, container,false);
        DB = new SQLiteDBHelperDoctor(getContext());
        binding.doctorPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = binding.doctorFirstName.getText().toString();
                String lastname = binding.doctorLastName.getText().toString();
                String email = binding.doctorEmail.getText().toString();
                String phone = binding.doctorPhone.getText().toString();
                Log.d("Test","Here");
                if(firstname.isEmpty()) {
                    binding.doctorFirstName.setError("First Name is Required");
                    binding.doctorFirstName.requestFocus();
                    return;
                }
                if(lastname.isEmpty()) {
                    binding.doctorLastName.setError("Last Name is Required");
                    binding.doctorLastName.requestFocus();
                    return;
                }
                if(email.isEmpty()) {
                    binding.doctorEmail.setError("Email is required");
                    binding.doctorEmail.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.doctorEmail.setError("Invalid email");
                    binding.doctorEmail.requestFocus();
                    return;
                }
                if(phone.isEmpty()) {
                    binding.doctorPhone.setError("Last Name is Required");
                    binding.doctorPhone.requestFocus();
                    return;
                }
                Date date = new Date();
                String uniqueDoctorID = "" + date.getTime();
                Boolean insertDoctorData = DB.insertInitialDoctorData(uniqueDoctorID, firstname, lastname,email,phone,"", "", "");
                Log.d("Test",insertDoctorData.toString());
                if(insertDoctorData == true) {
                    DoctorRegisterFragmentSecond nextFrag = new DoctorRegisterFragmentSecond();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.doctor_login_fragment_container, nextFrag, "New Frag")
                            .addToBackStack("first").commit();
                } else {
                    Toast.makeText(getActivity(), "Data not inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}