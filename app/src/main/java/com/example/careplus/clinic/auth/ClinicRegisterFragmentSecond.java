package com.example.careplus.clinic.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentClinicRegisterSecondBinding;
import com.example.careplus.localStorage.SQLiteDBHelperClinic;
import com.example.careplus.patient.auth.PatientRegisterFragmentFourth;

public class ClinicRegisterFragmentSecond extends Fragment {
    FragmentClinicRegisterSecondBinding binding;
    SQLiteDBHelperClinic DB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClinicRegisterSecondBinding.inflate(inflater, container, false);
        DB = new SQLiteDBHelperClinic(getContext());
        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String address = binding.clinicAddressLine1.getText().toString().trim() + ", " + binding.clinicAddressLine2.getText().toString().trim();
                String state = binding.clinicState.getText().toString();
                String city = binding.clinicCity.getText().toString();
                String zipCode = binding.clinicZip.getText().toString();
                if(address.isEmpty()) {
                    binding.clinicAddressLine1.setError("Address field required");
                    binding.clinicAddressLine1.requestFocus();
                    return;
                }
                if(city.isEmpty()) {
                    binding.clinicCity.setError("City field required");
                    binding.clinicCity.requestFocus();
                    return;
                }
                if(state.isEmpty()) {
                    binding.clinicState.setError("State/Province field required");
                    binding.clinicState.requestFocus();
                    return;
                }
                if(zipCode.isEmpty()) {
                    binding.clinicZip.setError("Pincode field required");
                    binding.clinicZip.requestFocus();
                    return;
                }

                String clinicID = DB.getClinicID();
                Boolean updateClinicDetails = DB.updateClinicSecond(clinicID ,address+", "+city, city, state, zipCode);
                if(updateClinicDetails == true) {
                    ClinicRegisterFragmentThird nextFrag = new ClinicRegisterFragmentThird();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.clinic_login_fragment_container, nextFrag, "New Frag")
                            .addToBackStack("first").commit();
                } else {
                    Toast.makeText(getActivity(), "Data not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}