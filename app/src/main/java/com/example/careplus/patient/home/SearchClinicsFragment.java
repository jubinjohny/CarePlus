package com.example.careplus.patient.home;

import android.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    FirebaseUser user;
    ArrayList<String> filteredList = new ArrayList<>();
    FirebaseDatabase realTimeDB;
    DatabaseReference dbRef;
    String insuranceProviderSelf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchClinicsBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               filterTheList(filteredList);
            }
        });
        db.collection("Users").whereEqualTo("email", user.getEmail()).get().
            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            insuranceProviderSelf = doc.getData().get("insuranceProvider").toString();
                        }
                    }
                }
            });
        setRecyclerView(filteredList);
        return binding.getRoot();
    }
    public void setRecyclerView(ArrayList<String> filteredData) {
        db = FirebaseFirestore.getInstance();
        realTimeDB = FirebaseDatabase.getInstance();
        dbRef = realTimeDB.getReference("CommonAttributes");
        db.collection("Clinics").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<ClinicsViewData> clinicsList = new ArrayList<>();
                    if(task.isSuccessful()) {
                        if(filteredData.isEmpty()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                ClinicsViewData clinic = new ClinicsViewData(doc.getData().get("clinicID").toString(), doc.getData().get("name").toString(),
                                        doc.getData().get("city").toString(), doc.getData().get("email").toString(), R.drawable.clinic2, "Book Appointment");
                                clinicsList.add(clinic);
                            }
                        } else {
                            if(filteredData.contains("My Insurance Provider")) {
                                filteredData.add(insuranceProviderSelf);
                            }
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                dbRef.child(doc.getData().get("name").toString()).addValueEventListener(new ValueEventListener() {
                                    boolean checked = false;
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot each : snapshot.getChildren()) {
                                            for (String listItem : filteredData) {
                                                if (each.getValue().toString().contains(listItem)) {
                                                    Log.d("Every", each + " " + listItem);
                                                    ClinicsViewData clinic = new ClinicsViewData(doc.getData().get("clinicID").toString(), doc.getData().get("name").toString(),
                                                            doc.getData().get("city").toString(), doc.getData().get("email").toString(), R.drawable.clinic2, "Book Appointment");
                                                    clinicsList.add(clinic);
                                                    checked = true;
                                                    break;
                                                }
                                            }
                                            if(checked) break;
                                        }
                                        if(clinicsList.size() == 0) {
                                            binding.noClinics.setVisibility(View.VISIBLE);
                                        } else {
                                            binding.noClinics.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        ClinicsViewAdapter adapter = new ClinicsViewAdapter(getContext(), clinicsList);
                        binding.clinicsView.setAdapter(adapter);
                        binding.clinicsView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }
            });
    }

    public void filterTheList(ArrayList<String> filteredData) {
        db = FirebaseFirestore.getInstance();
        AlertDialog.Builder filterList = new AlertDialog.Builder(getActivity());
        ArrayList<String> insuranceProviders = new ArrayList<>();
        ArrayList<String> filterData = new ArrayList<>();
        db.collection("InsuranceProviders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    insuranceProviders.add("My Insurance Provider");
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        insuranceProviders.add(doc.getId());
                    }
                }
                boolean[] checkedItems = new boolean[insuranceProviders.size()];
                filterList.setTitle("Filter By Insurance Provider");
                filterList.setMultiChoiceItems(insuranceProviders.toArray(new String[0]), checkedItems, (dialogInterface, which, isChecked) -> {
                    checkedItems[which] = isChecked;
                });

                filterList.setCancelable(false);
                filterList.setPositiveButton("Done", ((dialogInterface, which) -> {
                    for(int i = 0; i < checkedItems.length; i++) {
                        if(checkedItems[i]) {
                            filterData.add(insuranceProviders.get(i));
                        }
                    }
                    setRecyclerView(filterData);
                }));
                filterList.setNegativeButton("Close", ((dialogInterface, i) -> {
                    Toast.makeText(getActivity(), "Filtering Cancelled", Toast.LENGTH_SHORT).show();
                }));

                filterList.setNeutralButton("CLEAR ALL", ((dialogInterface, which) -> {
                    Arrays.fill(checkedItems, false);
                    filterData.clear();
                    setRecyclerView(filterData);
                }));

                filterList.create();
                filterList.show();
            }
        });
    }
}