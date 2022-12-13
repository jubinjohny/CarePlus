package com.example.careplus.patient.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.adapters.ClinicsViewAdapter;
import com.example.careplus.databinding.FragmentSearchClinicsBinding;
import com.example.careplus.localStorage.ClinicsViewData;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SearchClinicsFragment extends Fragment {
    FragmentSearchClinicsBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    ArrayList<String> filteredList = new ArrayList<>();
    FirebaseDatabase realTimeDB;
    DatabaseReference dbRef;
    String insuranceProviderSelf;
    ListView list;
    ArrayAdapter<String> listAdapter;
    ArrayList<String> clinics = new ArrayList<>();
    ArrayList<String> newFilteredList = new ArrayList<>();
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchClinicsBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.filter_types);
                TextView filterByType = (TextView) dialog.findViewById(R.id.filter_by_type);
                TextView filterByInsurance = (TextView) dialog.findViewById(R.id.filter_by_insurance);
                filterByType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        filterByType(filteredList);
                    }
                });
                filterByInsurance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        filterTheList(filteredList);
                    }
                });
                dialog.setCancelable(true);
                dialog.show();
            }
        });
        db.collection("Patients").whereEqualTo("email", user.getEmail()).get().
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
        db.collection("Clinics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        clinics.add(doc.getData().get("name").toString());
                    }
                    list = (ListView) binding.getRoot().findViewById(R.id.listview);
                    listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,clinics);
                    list.setAdapter(listAdapter);
                    searchView = (SearchView) binding.getRoot().findViewById(R.id.simpleSearchView);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            newFilteredList.clear();
                            for(String each : clinics) {
                                if(each.contains(newText.toLowerCase(Locale.ROOT)) || each.contains(newText.toUpperCase(Locale.ROOT))) {
                                    listAdapter.getFilter().filter(newText);
                                    newFilteredList.add(each);
                                } else {
                                    Toast.makeText(getContext(), "No Match Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            Log.d("DataFiltered", newFilteredList.toString());
                            if(newFilteredList.size() > 0 && newText.length() > 0) {
                                filterByName(newFilteredList);
                            } else if(newFilteredList.size() > 0 && newText.length() == 0){
                                setRecyclerView(new ArrayList<>());
                            } else {
                                setRecyclerView(newFilteredList);
                            }
                            return false;
                        }
                    });
                }
            }
        });
        setRecyclerView(filteredList);
        this.startAnimations();
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
                                                Log.d("DataList", listItem);
                                                if (each.getValue().toString().contains(listItem)) {
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

    public void filterByName(ArrayList<String> filteredList) {
        db.collection("Clinics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<ClinicsViewData> clinicsList = new ArrayList<>();
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        if(filteredList.contains(doc.getData().get("name").toString())) {
                            ClinicsViewData clinic = new ClinicsViewData(doc.getData().get("clinicID").toString(), doc.getData().get("name").toString(),
                                    doc.getData().get("city").toString(), doc.getData().get("email").toString(), R.drawable.clinic2, "Book Appointment");
                            clinicsList.add(clinic);
                        }
                    }
                    if(clinicsList.size() == 0) {
                        binding.noClinics.setVisibility(View.VISIBLE);
                    } else {
                        binding.noClinics.setVisibility(View.INVISIBLE);
                    }
                    ClinicsViewAdapter adapter = new ClinicsViewAdapter(getContext(), clinicsList);
                    binding.clinicsView.setAdapter(adapter);
                    binding.clinicsView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        });
    }

    public void filterByType(ArrayList<String> filteredList) {
        AlertDialog.Builder filterList = new AlertDialog.Builder(getActivity());
        filterList.setTitle("Filter By Clinic Type");
        ArrayList<String> clinicTypes = new ArrayList<>();
        ArrayList<String> filterData = new ArrayList<>();
        clinicTypes.add("Physiotherapy");
        clinicTypes.add("Dentistry");
        clinicTypes.add("Optometry");
        boolean[] checkedItems = new boolean[clinicTypes.size()];
        filterList.setMultiChoiceItems(clinicTypes.toArray(new String[0]), checkedItems, (dialogInterface, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        filterList.setCancelable(false);
        filterList.setPositiveButton("Done", ((dialogInterface, which) -> {
            for(int i = 0; i < checkedItems.length; i++) {
                if(checkedItems[i]) {
                    filterData.add(clinicTypes.get(i));
                }
            }
            db.collection("Clinics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<ClinicsViewData> clinicsList = new ArrayList<>();
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            if(filterData.contains(doc.getData().get("type").toString())) {
                                ClinicsViewData clinic = new ClinicsViewData(doc.getData().get("clinicID").toString(), doc.getData().get("name").toString(),
                                        doc.getData().get("city").toString(), doc.getData().get("email").toString(), R.drawable.clinic2, "Book Appointment");
                                clinicsList.add(clinic);
                            }
                        }
                        if(clinicsList.size() == 0) {
                            binding.noClinics.setVisibility(View.VISIBLE);
                        } else {
                            binding.noClinics.setVisibility(View.INVISIBLE);
                        }
                        ClinicsViewAdapter adapter = new ClinicsViewAdapter(getContext(), clinicsList);
                        binding.clinicsView.setAdapter(adapter);
                        binding.clinicsView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }
            });
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

    public void startAnimations() {

        binding.listTitle.setAlpha(0f);
        binding.listTitle.setAlpha(0f);
        binding.listTitle.setTranslationY(50);
        binding.listTitle.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.searchBarLayout.setAlpha(0f);
        binding.searchBarLayout.setAlpha(0f);
        binding.searchBarLayout.setTranslationY(50);
        binding.searchBarLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.clinicsView.setAlpha(0f);
        binding.clinicsView.setAlpha(0f);
        binding.clinicsView.setTranslationY(50);
        binding.clinicsView.animate().alpha(1f).translationYBy(-50).setDuration(1000);
    }

}