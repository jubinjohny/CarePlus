package com.example.careplus.clinic.auth.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentScheduleDoctorsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ScheduleDoctorsFragment extends Fragment implements View.OnClickListener {
    FragmentScheduleDoctorsBinding binding;
    FirebaseFirestore db;
    FirebaseUser user;
    ArrayList<String> doctorsList;
    ArrayList<String> doctors;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScheduleDoctorsBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        doctorsList = (ArrayList<String>) doc.getData().get("approvedList");
                    }
                }
            }
        });
        doctors = new ArrayList<>();
        binding.mondayFn.setOnClickListener(this);
        binding.mondayAn.setOnClickListener(this);
        binding.tuesdayFn.setOnClickListener(this);
        binding.tuesdayAn.setOnClickListener(this);
        binding.wednesdayAn.setOnClickListener(this);
        binding.wednesdayFn.setOnClickListener(this);
        binding.thursdayAn.setOnClickListener(this);
        binding.thursdayFn.setOnClickListener(this);
        binding.fridayFn.setOnClickListener(this);
        binding.fridayAn.setOnClickListener(this);
        binding.saturdayAn.setOnClickListener(this);
        binding.saturdayFn.setOnClickListener(this);
        binding.sundayFn.setOnClickListener(this);
        binding.sundayAn.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        db = FirebaseFirestore.getInstance();
        AlertDialog.Builder doctorOptions = new AlertDialog.Builder(getActivity());
        doctorOptions.setTitle("Select Doctor");
        switch(view.getId()) {
            case R.id.monday_an:
              getDoctorsList("Monday AN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.mondayAn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.monday_fn:
               getDoctorsList("Monday FN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.mondayFn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.tuesday_an:
                getDoctorsList("Tuesday AN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.tuesdayAn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.tuesday_fn:
                getDoctorsList("Tuesday FN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.tuesdayFn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.wednesday_an:
                getDoctorsList("Wednesday AN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.wednesdayAn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.wednesday_fn:
                getDoctorsList("Wednesday FN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.wednesdayFn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.thursday_fn:
                getDoctorsList("Thursday FN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.thursdayFn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.thursday_an:
                getDoctorsList("Thursday AN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.thursdayAn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.friday_an:
                getDoctorsList("Friday AN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.fridayAn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.friday_fn:
                getDoctorsList("Friday FN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.fridayFn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.saturday_fn:
                getDoctorsList("Saturday FN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.saturdayFn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.saturday_an:
                getDoctorsList("Saturday AN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.saturdayAn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.sunday_fn:
                getDoctorsList("Sunday FN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.sundayFn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            case R.id.sunday_an:
                getDoctorsList("Sunday AN");
                if(doctors.size() > 0) {
                    doctorOptions.setItems(doctors.toArray(new String[0]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            binding.sundayAn.setText(doctors.get(i));
                        }
                    });
                } else {
                    doctorOptions.setMessage("No Doctors Available");
                    doctorOptions.setNegativeButton("OK", null);
                }
                doctorOptions.show();
                break;
            default:
                break;
        }
    }

    public void getDoctorsList(String shift) {
        for(String doctor : doctorsList) {
            db.collection("Doctors").whereEqualTo("email", doctor).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<String> avail = new ArrayList<>();
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                avail = (ArrayList<String>) doc.getData().get("availability");
                                Log.d("Here",avail.toString());
                                if(avail.contains(shift) == false) {
                                    doctors.add(doc.getData().get("firstName").toString());
                                }
                            }
                        }
                    }
                });
        }
    }
}