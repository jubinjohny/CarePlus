package com.example.careplus.patient.auth;

import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentPatientRegisterThirdBinding;
import com.example.careplus.localStorage.SQLiteDBHelper;


public class PatientRegisterFragmentThird extends Fragment {

    FragmentPatientRegisterThirdBinding binding;
    SQLiteDBHelper DB;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentPatientRegisterThirdBinding.inflate(inflater,container, false);
        DB = new SQLiteDBHelper(getContext());
        binding.userNextThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = binding.addressUser1.getText().toString().trim() + ", " + binding.addressUser2.getText().toString().trim();
                String state = binding.stateUser.getText().toString();
                String city = binding.cityUser.getText().toString();
                String zipCode = binding.pincodeUser.getText().toString();
                if(binding.addressUser1.getText().toString().isEmpty()) {
                    binding.addressUser1.setError("Address field required");
                    binding.addressUser1.requestFocus();
                    return;
                }
                if(city.isEmpty()) {
                    binding.cityUser.setError("City field required");
                    binding.cityUser.requestFocus();
                    return;
                }
                if(state.isEmpty()) {
                    binding.stateUser.setError("State/Province field required");
                    binding.stateUser.requestFocus();
                    return;
                }
                if(zipCode.isEmpty()) {
                    binding.pincodeUser.setError("Pincode field required");
                    binding.pincodeUser.requestFocus();
                    return;
                }
                String patientID = DB.getPatientID();
                Boolean updatePatientDetails = DB.updatePatientThird(patientID ,address, city, state, zipCode);
                if(updatePatientDetails == true) {
                    PatientRegisterFragmentFourth nextFrag = new PatientRegisterFragmentFourth();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.patient_login_fragment_container, nextFrag, "New Frag")
                            .addToBackStack("third").commit();
                } else {
                    Toast.makeText(getActivity(), "Data not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }
}