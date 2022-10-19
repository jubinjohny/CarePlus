package com.example.careplus.doctor.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.careplus.R;
import com.example.careplus.clinic.auth.ClinicLoginFragment;

public class DoctorActivityLogin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        getSupportActionBar().hide();

        if(savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.doctor_login_fragment_container,
                    new DoctorLoginFragment()).commit();
        }
    }
}