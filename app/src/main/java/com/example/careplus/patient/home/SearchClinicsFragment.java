package com.example.careplus.patient.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.adapters.ClinicsViewAdapter;
import com.example.careplus.adapters.ScheduleViewAdapter;
import com.example.careplus.databinding.FragmentSearchClinicsBinding;
import com.example.careplus.localStorage.ClinicsViewData;
import com.example.careplus.localStorage.ScheduleViewCardData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SearchClinicsFragment extends Fragment {
    FragmentSearchClinicsBinding binding;
    FirebaseFirestore db;
    String[] clinics;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchClinicsBinding.inflate(inflater, container, false);
        setRecyclerView();
        return binding.getRoot();
    }
    public void setRecyclerView() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Clinics").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<ClinicsViewData> clinicsList = new ArrayList<>();
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                ClinicsViewData clinic = new ClinicsViewData(doc.getData().get("clinicID").toString(), doc.getData().get("name").toString(),
                                        doc.getData().get("city").toString(), doc.getData().get("email").toString(), R.drawable.clinic2);
                                clinicsList.add(clinic);
                            }
                        }
                        ClinicsViewAdapter adapter = new ClinicsViewAdapter(getContext(), clinicsList);
                        binding.clinicsView.setAdapter(adapter);
                        binding.clinicsView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                });
    }
}