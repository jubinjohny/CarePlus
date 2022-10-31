package com.example.careplus.doctor.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentDoctorDashBoardBinding;
import com.example.careplus.patient.home.PatientDashBoardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DoctorDashBoardFragment extends Fragment {
    FragmentDoctorDashBoardBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentDoctorDashBoardBinding.inflate(inflater, container, false);
       binding.manageSchedule.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                ManageScheduleFragment newFrag = new ManageScheduleFragment();
               getActivity().getSupportFragmentManager().beginTransaction()
                       .replace(R.id.doctor_fragment_container, newFrag, "New Frag")
                       .addToBackStack("first").commit();
           }
       });
       binding.requestTime.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                NewRequestFragment nextFrag = new NewRequestFragment();
               getActivity().getSupportFragmentManager().beginTransaction()
                       .replace(R.id.doctor_fragment_container, nextFrag, "New Frag")
                       .addToBackStack("first").commit();
           }
       });
       binding.updateProfile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DoctorProfileFragment nextFrag = new DoctorProfileFragment();
               getActivity().getSupportFragmentManager().beginTransaction()
                       .replace(R.id.doctor_fragment_container, nextFrag, "New Frag")
                       .addToBackStack("first").commit();
           }
       });
       user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getEmail() != null ){
            checkNewRequest();
        }
       return binding.getRoot();
    }

    public void checkNewRequest() {
        db = FirebaseFirestore.getInstance();
        db.collection("Doctors").whereEqualTo("email", user.getEmail()).get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for ( QueryDocumentSnapshot doc : task.getResult())  {
                        if(doc.getData().get("requests").toString() != "[]") {
                            binding.newRequest.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }
}