package com.example.careplus.doctor.home;

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
import com.example.careplus.databinding.FragmentDoctorProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorProfileFragment extends Fragment {
    FragmentDoctorProfileBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    String userId;
    ArrayList<String> approvedList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentDoctorProfileBinding.inflate(inflater,container,false);
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Doctors").whereEqualTo("email", doctor.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                userId = doc.getId();
                                binding.firstName.setText(doc.getData().get("firstName").toString());
                                binding.lastName.setText(doc.getData().get("lastName").toString());
                                binding.contactInfo.setText(doc.getData().get("email").toString());
                                if(doc.getData().get("approvedList").toString() != "" ) {
                                    approvedList = (ArrayList<String>) doc.getData().get("approvedList");
                                } else {
                                    approvedList = new ArrayList<>();
                                }
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
        binding.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                db.collection("Doctors").document(userId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Doctor Account Deleted", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Doctor Account Deletion Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            };
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.firstName.isEnabled()) {
                    Map<String, Object> updateData= new HashMap<>();
                    updateData.put("firstname", binding.firstName.getText().toString());
                    updateData.put("lastName", binding.lastName.getText().toString());
                    updateData.put("clinics", binding.clinics.getText().toString());
                    updateData.put("email", binding.contactInfo.getText().toString());
                    db.collection("Doctors").document(userId)
                            .update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    binding.firstName.setEnabled(false);
                                    binding.lastName.setEnabled(false);
                                    binding.contactInfo.setEnabled(false);
                                    binding.update.setText("Update Profile");
                                }
                            });
                } else {
                    binding.firstName.setEnabled(true);
                    binding.lastName.setEnabled(true);
                    binding.contactInfo.setEnabled(true);
                    binding.update.setText("Complete Update");
                }
            }
        });
        binding.clinics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder insuranceOptions = new AlertDialog.Builder(getActivity());
                insuranceOptions.setTitle("Clinics Engaged");
                if(approvedList.size() > 0) {
                    insuranceOptions.setItems(approvedList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    insuranceOptions.setPositiveButton("OK", null);
                    insuranceOptions.setCancelable(true);
                    insuranceOptions.show();
                } else {
                    Toast.makeText(getActivity(), "No list available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.startAnimations();
       return binding.getRoot();
    }

    public void startAnimations() {
        binding.firstNameLayout.setAlpha(0f);
        binding.firstNameLayout.setAlpha(0f);
        binding.firstNameLayout.setTranslationY(50);
        binding.firstNameLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.lastNameLayout.setAlpha(0f);
        binding.lastNameLayout.setAlpha(0f);
        binding.lastNameLayout.setTranslationY(50);
        binding.lastNameLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        binding.clinicsEngagedLayout.setAlpha(0f);
        binding.clinicsEngagedLayout.setAlpha(0f);
        binding.clinicsEngagedLayout.setTranslationY(50);
        binding.clinicsEngagedLayout.animate().alpha(1f).translationYBy(-50).setDuration(1000);

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