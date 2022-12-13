package com.example.careplus.patient.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.TransferDataFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.grpc.HandlerRegistry;

public class TransferDataFragment extends Fragment {
    TransferDataFragmentBinding binding;
    FirebaseFirestore db;
    ArrayList<String> clinicsList = new ArrayList<>();
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = TransferDataFragmentBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.senderEmail.setText(user.getEmail());
        db.collection("Clinics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        clinicsList.add(doc.getData().get("email").toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, clinicsList.toArray(new String[clinicsList.size()]));
                    adapter.setDropDownViewResource(com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item);
                    binding.spinnerTo.setAdapter(adapter);
                    binding.spinnerAbout.setAdapter(adapter);
                }
            }
        });
        binding.sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.spinnerAbout.getSelectedItem().toString() == binding.spinnerTo.getSelectedItem().toString()) {
                    Toast.makeText(getContext(), "Please have different emails", Toast.LENGTH_SHORT).show();
                } else {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    String subject = "Medical Data Transfer Request";
                    String message = "Hello " + binding.spinnerTo.getSelectedItem().toString() + ",\n" +
                            "Please transfer the medical data related to " + user.getEmail() + " to " + binding.spinnerAbout.getSelectedItem().toString() +
                            " for medical purposes.\n Thank you.";
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{binding.spinnerTo.getSelectedItem().toString()});
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(email, "Choose an email client :"));
                    Toast.makeText(getContext(), "Email Send..!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }
}