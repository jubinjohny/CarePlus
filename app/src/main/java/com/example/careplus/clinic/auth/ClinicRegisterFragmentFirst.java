package com.example.careplus.clinic.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentClinicRegisterFirstBinding;
import com.example.careplus.localStorage.SQLiteDBHelperClinic;

import java.util.Date;

public class ClinicRegisterFragmentFirst extends Fragment {
    FragmentClinicRegisterFirstBinding binding;
    SQLiteDBHelperClinic DB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClinicRegisterFirstBinding.inflate(inflater, container, false);
        DB = new SQLiteDBHelperClinic(getContext());
        binding.phoneClinic.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.clinicTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder clinicTypes = new AlertDialog.Builder(getActivity());
                clinicTypes.setTitle("Select Type");
                String[] options = {"Physiotherapy", "Optometry", "Dentistry"};
                clinicTypes.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int selection) {
                        dialogInterface.dismiss();
                        switch(selection) {
                            case 0:
                                binding.clinicTypes.setText("Physiotherapy");
                                break;
                            case 1:
                                binding.clinicTypes.setText("Optometry");
                                break;
                            case 2:
                                binding.clinicTypes.setText("Dentistry");
                                break;
                            default:
                                break;
                        }
                    }
                });
                clinicTypes.show();
            }
        });
        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.clinicName.getText().toString();
                String type = binding.clinicTypes.getText().toString();
                String email = binding.clinicEmail.getText().toString();
                String phone = binding.phoneClinic.getText().toString();
                if(name.isEmpty()) {
                    binding.clinicName.setError("First Name is Required");
                    binding.clinicName.requestFocus();
                    return;
                }
                if(type.isEmpty()) {
                    binding.clinicTypes.setError("Last Name is Required");
                    binding.clinicTypes.requestFocus();
                    return;
                }
                if(email.isEmpty()) {
                    binding.clinicEmail.setError("Email is required");
                    binding.clinicEmail.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.clinicEmail.setError("Invalid email");
                    binding.clinicEmail.requestFocus();
                    return;
                }
                if(phone.isEmpty()) {
                    binding.phoneClinic.setError("Last Name is Required");
                    binding.phoneClinic.requestFocus();
                    return;
                }
                Date date = new Date();
                String uniqueClinicID = "" + date.getTime();
                Boolean initializeClinicRegData = DB.insertInitialClinicData(uniqueClinicID,name,type,email,phone,
                        "empty","empty", "empty", "empty", 1000, 1000, 1000, 1000, 1000,
                        1000, 1000, 1000, "empty", "empty", "empty", "empty",
                        "empty", "empty");
                if(initializeClinicRegData == true) {
                    ClinicRegisterFragmentSecond nextFrag = new ClinicRegisterFragmentSecond();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.clinic_login_fragment_container, nextFrag, "New Frag")
                            .addToBackStack("first").commit();
                } else {
                    Toast.makeText(getActivity(), "Data not inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}