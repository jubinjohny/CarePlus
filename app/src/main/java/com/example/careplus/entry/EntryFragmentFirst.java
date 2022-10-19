package com.example.careplus.entry;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.careplus.R;

public class EntryFragmentFirst extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_entry_first, container, false);
        Button continueButton = (Button) root.findViewById(R.id.continueButton);
        ImageView logo = (ImageView) root.findViewById(R.id.logo);
        logo.setImageResource(R.drawable.carepluslogo);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EntryFragmentSecond entryFragmentSecond = new EntryFragmentSecond();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.entry_fragment_container, entryFragmentSecond, "New Frag")
                        .addToBackStack(null).commit();
            }
        });
        return root;
    }
}