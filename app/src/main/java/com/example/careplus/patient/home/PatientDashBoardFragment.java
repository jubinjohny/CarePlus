package com.example.careplus.patient.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentPatientDashBoardBinding;

public class PatientDashBoardFragment extends Fragment {
   FragmentPatientDashBoardBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    binding = FragmentPatientDashBoardBinding.inflate(inflater, container,false);
    binding.searchClinics.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SearchClinicsFragment newFrag = new SearchClinicsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.patient_fragment_container, newFrag, "New Frag")
                    .addToBackStack("first").commit();
        }
    });
    binding.updateProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PatientProfileFragment nextFrag = new PatientProfileFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.patient_fragment_container, nextFrag, "New Frag")
                    .addToBackStack("first").commit();
        }
    });
    return binding.getRoot();
}
}