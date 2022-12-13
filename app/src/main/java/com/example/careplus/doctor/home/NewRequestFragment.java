package com.example.careplus.doctor.home;

import android.net.CaptivePortal;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.careplus.R;
import com.example.careplus.adapters.AppointmentsViewAdapter;
import com.example.careplus.adapters.DoctorSearchAdapter;
import com.example.careplus.adapters.NewRequestDoctorAdapter;
import com.example.careplus.databinding.FragmentNewRequestBinding;
import com.example.careplus.localStorage.DoctorViewCardData;
import com.example.careplus.localStorage.RequestViewCardData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;

public class NewRequestFragment extends Fragment {
    FragmentNewRequestBinding binding;
    private DatabaseReference mDatabase;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewRequestBinding.inflate(inflater, container, false);
        setRecyclerView();
        return binding.getRoot();
    }

    public void setRecyclerView () {
        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ArrayList<RequestViewCardData> clinicList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Clinics").whereArrayContains("pendingRequests", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    clinicList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        RequestViewCardData abc = new RequestViewCardData(doc.getData().get("name").toString(),
                                doc.getData().get("address").toString()+", "+doc.getData().get("city").toString()+", "+ doc.getData().get("zipCode"), doc.getData().get("email").toString(), doc.getId(), R.drawable.clinic);
                        clinicList.add(abc);
                    }
                    if(clinicList.size() > 0) {
                        binding.noBooking.setVisibility(View.GONE);
                        NewRequestDoctorAdapter adapter = new NewRequestDoctorAdapter(getContext(), clinicList);
                        binding.requestView.setAdapter(adapter);
                        binding.requestView.setLayoutManager(new LinearLayoutManager(getContext()));
                    } else {
                        binding.noBooking.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}