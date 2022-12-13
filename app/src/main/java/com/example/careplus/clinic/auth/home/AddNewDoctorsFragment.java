package com.example.careplus.clinic.auth.home;

import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.careplus.R;
import com.example.careplus.adapters.DoctorSearchAdapter;
import com.example.careplus.databinding.FragmentAddNewDoctorsBinding;
import com.example.careplus.localStorage.DoctorViewCardData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;


public class AddNewDoctorsFragment extends Fragment {
    FragmentAddNewDoctorsBinding binding;
    FirebaseFirestore db;
    String requestPending;
    String clinicType, clinicName;
    FirebaseUser user;
    FirebaseDatabase realTimeDB;
    DatabaseReference dbRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNewDoctorsBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        clinicType = doc.getData().get("type").toString();
                        binding.doctorViewTitle.setText("" + clinicType.toUpperCase() + " DOCTORS LIST");
                        clinicName = doc.getData().get("name").toString();
                        setRecyclerView();
                    }
                }
            }
        });
        return binding.getRoot();
    }

    public void setRecyclerView () {
        realTimeDB = FirebaseDatabase.getInstance();
        dbRef = realTimeDB.getReference("CommonAttributes");
        db = FirebaseFirestore.getInstance();
        db.collection("Doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<DoctorViewCardData> doctorList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String speciality = doc.getData().get("specialization").toString();
                        if(clinicType.contentEquals(speciality)) {
                            db.collection("Clinics").whereEqualTo("email", user.getEmail()).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                        if(task2.isSuccessful()) {
                                            for(QueryDocumentSnapshot doc2 : task2.getResult()) {
                                                if(doc2.getData().get("approvedList").toString().contains(doc.getData().get("email").toString())) {
                                                    break;
                                                } else {
                                                    if(doc.getData().get("requests").toString().contains(user.getEmail())) {
                                                        requestPending = "Pending Approval";
                                                    } else {
                                                        requestPending = "Request Time";
                                                    }
                                                    DoctorViewCardData abc = new DoctorViewCardData(doc.getData().get("firstName").toString() + " " + doc.getData().get("lastName").toString(),
                                                            doc.getData().get("specialization").toString(), doc.getData().get("email").toString(), doc.getId(), R.drawable.doctor, requestPending);
                                                    doctorList.add(abc);
                                                }
                                            }
                                        }
                                        if(doctorList.size() > 0) {
                                            binding.noBooking.setVisibility(View.GONE);
                                            DoctorSearchAdapter adapter = new DoctorSearchAdapter(getContext(), doctorList);
                                            binding.doctorsView.setAdapter(adapter);
                                            binding.doctorsView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        } else {
                                            binding.noBooking.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                            }

                        }
                    }
                }
        });
    }
}