package com.example.careplus.clinic.auth;

import android.os.Bundle;

import com.example.careplus.patient.auth.PatientLoginFragment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.careplus.R;

public class ClinicActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_login);
        getSupportActionBar().hide();

        if(savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.clinic_login_fragment_container,
                    new ClinicLoginFragment()).commit();
        }
    }
}