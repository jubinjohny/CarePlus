package com.example.careplus.clinic.auth.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentClinicHomeBinding;

public class ClinicHomeFragment extends Fragment {
    FragmentClinicHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClinicHomeBinding.inflate(inflater, container, false);
        binding.addDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewDoctorsFragment nextFrag = new AddNewDoctorsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.manageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClinicProfile nextFrag = new ClinicProfile();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.viewTimeslots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvailableTimeSlotFragment nextFrag = new AvailableTimeSlotFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        binding.scheduleDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleDoctorsFragment nextFrag = new ScheduleDoctorsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clinic_fragment_container, nextFrag, "New Frag")
                        .addToBackStack("first").commit();
            }
        });
        return binding.getRoot();
    }
}