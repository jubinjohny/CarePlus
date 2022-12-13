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
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.careplus.R;
import com.example.careplus.clinic.auth.home.ClinicHomeActivity;
import com.example.careplus.clinic.auth.home.ClinicHomeFragment;
import com.example.careplus.doctor.home.DoctorDashBoardFragment;
import com.example.careplus.doctor.home.DoctorHomeActivity;
import com.example.careplus.patient.home.PatientHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;

public class EntryFragmentFirst extends Fragment {
    LottieAnimationView loginButton;
    FirebaseFirestore db;
    FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_entry_first, container, false);
        root.getBackground().setAlpha(80);
        String[] collections = new String[3];
        collections[0] = "Patients";
        collections[1] = "Clinics";
        collections[2] = "Doctors";
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if(user != null) {
            for(String each : collections) {
                db.collection(each).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                if(doc.getData().get("email").toString().contentEquals(user.getEmail())) {
                                    switch(each) {
                                        case "Patients":
                                            Intent user = new Intent(getActivity(), PatientHomeActivity.class);
                                            startActivity(user);
                                            break;
                                        case "Doctors":
                                            Intent doctor = new Intent(getActivity(), DoctorHomeActivity.class);
                                            startActivity(doctor);
                                            break;
                                        case "Clinics":
                                            Intent clinic = new Intent(getActivity(), ClinicHomeActivity.class);
                                            startActivity(clinic);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        loginButton = (LottieAnimationView) root.findViewById(R.id.loginButtonMain);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EntryFragmentSecond entryFragmentSecond = new EntryFragmentSecond();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.entry_fragment_container, entryFragmentSecond, "New Frag")
                        .addToBackStack(null).commit();
            }
        });
        return root;
    }
}