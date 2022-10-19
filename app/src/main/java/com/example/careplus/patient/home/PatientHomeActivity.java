package com.example.careplus.patient.home;

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
import android.widget.Toolbar;

import com.example.careplus.MainActivity;
import com.example.careplus.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PatientHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        drawer = findViewById(R.id.patient_drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,
                R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.patient_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.patient_fragment_container,
                    new PatientDashBoardFragment()).commit();
            navigationView.setCheckedItem(R.id.patient_dashboard);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.patient_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.patient_fragment_container,
                        new PatientDashBoardFragment()).commit();
                break;
            case R.id.patient_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.patient_fragment_container,
                        new PatientProfileFragment()).commit();
                break;
            case R.id.patient_logout:
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
}