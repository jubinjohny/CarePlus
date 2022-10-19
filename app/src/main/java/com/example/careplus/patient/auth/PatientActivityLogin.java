package com.example.careplus.patient.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.careplus.R;

public class PatientActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_activity_login);
        getSupportActionBar().show();

        if(savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.patient_login_fragment_container,
                    new PatientLoginFragment()).commit();
        }
    }
}