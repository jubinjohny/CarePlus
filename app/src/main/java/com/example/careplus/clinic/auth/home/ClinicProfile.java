package com.example.careplus.clinic.auth.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.careplus.MainActivity;
import com.example.careplus.R;
import com.example.careplus.clinic.auth.ClinicActivityLogin;
import com.example.careplus.databinding.FragmentClinicProfileBinding;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClinicProfile extends Fragment {
    FirebaseFirestore db;
    FragmentClinicProfileBinding binding;
    FirebaseDatabase realTimeDB;
    DatabaseReference dbRef;
    String clinicID, clinicName;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentClinicProfileBinding.inflate(inflater, container,false);
        FirebaseUser clinic = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        realTimeDB = FirebaseDatabase.getInstance();
        dbRef = realTimeDB.getReference("CommonAttributes");
        db.collection("Clinics").whereEqualTo("email", clinic.getEmail())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            clinicID = doc.getData().get("clinicID").toString();
                            binding.clinicName.setText(doc.getData().get("name").toString());
                            clinicName = doc.getData().get("name").toString();
                            binding.location.setText(doc.getData().get("address").toString());
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

        binding.insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                realTimeDB = FirebaseDatabase.getInstance();
                dbRef = realTimeDB.getReference("CommonAttributes");
                ArrayList<String> selfInsuranceProviders = new ArrayList<>();
                dbRef.child(clinicName).child("insuranceProviders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot each : snapshot.getChildren()) {
                            selfInsuranceProviders.add(each.getValue().toString());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AlertDialog.Builder insuranceList = new AlertDialog.Builder(getActivity());
                ArrayList<String> insuranceProviders = new ArrayList<>();
                db.collection("InsuranceProviders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                insuranceProviders.add(doc.getId());
                            }
                            boolean[] checkedItems = new boolean[insuranceProviders.size()];
                            insuranceList.setTitle("Insurance Providers");
                            insuranceList.setNegativeButton("CLOSE" , ((dialogInterface, which) -> {

                            }));
                            if(!binding.clinicName.isEnabled()) {
                                if(selfInsuranceProviders.isEmpty()) {
                                    insuranceList.setMessage("No Insurance Providers Added");
                                } else {
                                    insuranceList.setItems(selfInsuranceProviders.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                }
                            } else {

                                insuranceList.setTitle("Choose Insurance Providers");
                                insuranceList.setMultiChoiceItems(insuranceProviders.toArray(new String[0]), checkedItems, (dialogInterface, which, isChecked) -> {
                                    checkedItems[which] = isChecked;
                                });
                                insuranceList.setCancelable(true);
                                insuranceList.setPositiveButton("UPDATE" , ((dialogInterface, which) -> {
                                    selfInsuranceProviders.clear();
                                    for(int i = 0; i < checkedItems.length; i++) {
                                        if(checkedItems[i]) {
                                            selfInsuranceProviders.add(insuranceProviders.get(i));
                                        }
                                    }
                                    Map<String , Object> insuranceProviderList = new HashMap<>();
                                    insuranceProviderList.put("insuranceProviders", selfInsuranceProviders);
                                    Map<String, Object> fin = new HashMap<>();
                                    fin.put(clinicName+"", insuranceProviderList);
                                    dbRef.updateChildren(fin);
                                }));
                            }
                            insuranceList.create();
                            insuranceList.show();
                        }
                    }
                });
            }
        });

        binding.updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.updateProfile.getText().toString() == "Update Profile") {
                    binding.updateProfile.setText("Confirm Update");
                    binding.clinicName.setEnabled(true);
                    binding.location.setEnabled(true);
                    binding.contactInfo.setEnabled(true);
                } else {
                    Map<String, Object> updateData= new HashMap<>();
                    updateData.put("name", binding.clinicName.getText().toString());
                    updateData.put("address", binding.location.getText().toString());
                    updateData.put("email", binding.contactInfo.getText().toString());
                    db.collection("Clinics").document(clinicID).update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            binding.updateProfile.setText("Update Profile");
                            binding.clinicName.setEnabled(false);
                            binding.location.setEnabled(false);
                            binding.contactInfo.setEnabled(false);
                        }
                    });
                }
            }
        });

        binding.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                db.collection("Clinics").document(clinicID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Clinic Account Deleted", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Clinic Account Deletion Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            };
        });
        this.startAnimations();
        return binding.getRoot();
    }

    public void startAnimations() {
        binding.clinicNameLayout.setAlpha(0f);
        binding.clinicNameLayout.setAlpha(0f);
        binding.clinicNameLayout.setTranslationY(50);
        binding.clinicNameLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.clinicLocationLayout.setAlpha(0f);
        binding.clinicLocationLayout.setAlpha(0f);
        binding.clinicLocationLayout.setTranslationY(50);
        binding.clinicLocationLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.insuranceProvidersLayout.setAlpha(0f);
        binding.insuranceProvidersLayout.setAlpha(0f);
        binding.insuranceProvidersLayout.setTranslationY(50);
        binding.insuranceProvidersLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.contactInfoLayout.setAlpha(0f);
        binding.contactInfoLayout.setAlpha(0f);
        binding.contactInfoLayout.setTranslationY(50);
        binding.contactInfoLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.updateProfileLayout.setAlpha(0f);
        binding.updateProfileLayout.setAlpha(0f);
        binding.updateProfileLayout.setTranslationY(50);
        binding.updateProfileLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.logoutLayout.setAlpha(0f);
        binding.logoutLayout.setAlpha(0f);
        binding.logoutLayout.setTranslationY(50);
        binding.logoutLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.deleteAccountLayout.setAlpha(0f);
        binding.deleteAccountLayout.setAlpha(0f);
        binding.deleteAccountLayout.setTranslationY(50);
        binding.deleteAccountLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);
    }
}