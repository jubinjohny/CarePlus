package com.example.careplus.clinic.auth.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.careplus.MainActivity;
import com.example.careplus.R;
import com.example.careplus.databinding.FragmentClinicProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ClinicProfile extends Fragment {
    FirebaseFirestore db;
    FragmentClinicProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentClinicProfileBinding.inflate(inflater, container,false);
        FirebaseUser clinic = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Clinics").whereEqualTo("email", clinic.getEmail())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            binding.fclinicName.setText(doc.getData().get("name").toString());
                            binding.location.setText(doc.getData().get("address").toString() + " , " + doc.getData().get("city").toString());
                            binding.contactInfo.setText(doc.getData().get("email").toString());
                        }
                    }else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}