package com.example.careplus.clinic.auth.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

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

import java.util.List;

public class ClinicHomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_home);
        drawer = findViewById(R.id.clinic_drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,
                R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.clinic_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.clinic_fragment_container,
                    new ClinicHomeFragment()).commit();
            navigationView.setCheckedItem(R.id.patient_dashboard);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.clinic_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.clinic_fragment_container,
                        new ClinicHomeFragment()).commit();
                break;
            case R.id.clinic_bookings:
                getSupportFragmentManager().beginTransaction().replace(R.id.clinic_fragment_container,
                        new ClinicProfile()).commit();
                break;
            case R.id.clinic_logout:
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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            this.finishAffinity();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}