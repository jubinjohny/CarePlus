package com.example.careplus.patient.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentSearchClinicsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchClinicsFragment extends ListFragment implements AdapterView.OnItemClickListener {
    FragmentSearchClinicsBinding binding;
    FirebaseFirestore db;
    String[] clinics;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchClinicsBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        db.collection("Clinics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    clinics = new String[task.getResult().size()];
                    int i =0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        clinics[i] = doc.getData().get("name").toString();
                        Log.d("Names : ", doc.getData().get("name").toString());
                        i++;
                    }
                    Log.d("Clinics", clinics.toString());
                } else {
                    Toast.makeText(getActivity(), "No Clinics Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String[] city = {"Hello", "Hi", "How","Are", "You"};
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, city);
        binding.list.setAdapter(listAdapter);
        binding.list.setOnItemClickListener(this);
        return binding.getRoot();
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}