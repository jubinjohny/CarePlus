package com.example.careplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,
              R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserLogin()).commit();
            navigationView.setCheckedItem(R.id.nav_user);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserLogin()).commit();
                break;
            case R.id.nav_clinic:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ClinicLogin()).commit();
                break;
            case R.id.nav_doctor:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DoctorLogin()).commit();
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