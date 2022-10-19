package com.example.careplus.entry;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.careplus.MainActivity;
import com.example.careplus.R;
import com.example.careplus.clinic.auth.ClinicActivityLogin;
import com.example.careplus.doctor.auth.DoctorActivityLogin;
import com.example.careplus.patient.auth.PatientActivityLogin;

public class EntryFragmentSecond extends Fragment{
    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_entry_second, container, false);

        ImageView patientImage = (ImageView) root.findViewById(R.id.patient_login_image);
        ImageView clinicImage = (ImageView) root.findViewById(R.id.clinic_login_image);
        ImageView doctorImage = (ImageView) root.findViewById(R.id.doctor_login_image);

        LinearLayout patientLayout = (LinearLayout) root.findViewById(R.id.patient_layout);
        LinearLayout clinicLayout = (LinearLayout) root.findViewById(R.id.clinic_layout);
        LinearLayout doctorLayout = (LinearLayout) root.findViewById(R.id.doctor_layout);

        patientImage.setImageResource(R.drawable.patient2);
        clinicImage.setImageResource(R.drawable.clinic2);
        doctorImage.setImageResource(R.drawable.nurse);

        patientLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PatientActivityLogin.class);
                startActivity(i);
            }
        });
        clinicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ClinicActivityLogin.class);
                startActivity(i);
            }
        });
        doctorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DoctorActivityLogin.class);
                startActivity(i);
            }
        });
        return root;
    };
}