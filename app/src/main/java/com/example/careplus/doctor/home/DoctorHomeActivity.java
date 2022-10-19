package com.example.careplus.doctor.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.careplus.MainActivity;
import com.example.careplus.R;
import com.example.careplus.patient.home.PatientDashBoardFragment;
import com.example.careplus.patient.home.PatientProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorHomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        drawer = findViewById(R.id.doctor_drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,
                R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.doctor_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                    new DoctorDashBoardFragment()).commit();
            navigationView.setCheckedItem(R.id.doctor_dashboard);
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.doctor_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                        new DoctorDashBoardFragment()).commit();
                break;
            case R.id.doctor_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.doctor_fragment_container,
                        new DoctorProfileFragment()).commit();
                break;
            case R.id.doctor_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}