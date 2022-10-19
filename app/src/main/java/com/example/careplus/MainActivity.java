package com.example.careplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.careplus.entry.EntryFragmentFirst;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        if(savedInstanceState == null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.entry_fragment_container,
                    new EntryFragmentFirst()).commit();
        }
    }
}