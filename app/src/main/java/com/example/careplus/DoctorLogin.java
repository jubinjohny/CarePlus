package com.example.careplus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.zip.Inflater;

public class DoctorLogin extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.doctor_login_layout, container, false);
        TextView register_tv = (TextView) root.findViewById(R.id.registerTV_Doctor);
        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterView_Doctor regFrag = new RegisterView_Doctor();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, regFrag, "New Frag")
                        .addToBackStack(null).commit();
            }
        });
        return root;
    }
}
