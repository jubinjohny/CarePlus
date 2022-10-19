package com.example.careplus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserLogin extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          ViewGroup root = (ViewGroup) inflater.inflate(R.layout.user_login_layout, container, false);
          TextView register_tv = (TextView) root.findViewById(R.id.registerTV_User);
        register_tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                RegisterView_User regFrag = new RegisterView_User();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, regFrag, "New Frag")
                        .addToBackStack(null).commit();
            }
        });
        return root;
    }
}
