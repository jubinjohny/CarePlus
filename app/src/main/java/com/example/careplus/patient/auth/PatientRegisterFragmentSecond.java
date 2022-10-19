package com.example.careplus.patient.auth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentPatientRegisterSecondBinding;
import com.example.careplus.localStorage.SQLiteDBHelper;

import java.util.Calendar;

public class PatientRegisterFragmentSecond extends Fragment {
    FragmentPatientRegisterSecondBinding binding;
    SQLiteDBHelper DB;
    private static final String TAG = "MyActivity";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentPatientRegisterSecondBinding.inflate(inflater, container, false);
        DB = new SQLiteDBHelper(getContext());
        binding.phoneUser.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.dobUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.
                        binding.dobUser.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        binding.genderUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder genderOptions = new AlertDialog.Builder(getActivity());
                genderOptions.setTitle("Select Gender");
                String[] options = {"Female", "Male", "Another Gender", "Prefer not to answer"};
                genderOptions.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int selection) {
                        dialogInterface.dismiss();
                        switch(selection) {
                            case 0:
                                binding.genderUser.setText("Female");
                                break;
                            case 1:
                                binding.genderUser.setText("Male");
                                break;
                            case 2:
                                binding.genderUser.setText("Another Gender");
                                break;
                            case 3:
                                binding.genderUser.setText("Prefer not to say");
                                break;
                            default:
                                break;
                        }
                    }
                });
                genderOptions.show();
            }
        });

        binding.userNextSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dob = binding.dobUser.getText().toString();
                String phone = binding.phoneUser.getText().toString();
                String gender = binding.genderUser.getText().toString();
                if(dob.isEmpty()) {
                    binding.dobUser.setError("DOB is Required");
                    binding.dobUser.requestFocus();
                    return;
                }
                if(phone.isEmpty()) {
                    binding.phoneUser.setError("Phone number is required");
                    binding.phoneUser.requestFocus();
                    return;
                }
                String patientID = DB.getPatientID();
                Boolean updatePatientDetails = DB.updatePatientSecond(patientID, dob, phone, gender);
                if(updatePatientDetails == true) {
                    PatientRegisterFragmentThird nextFrag = new PatientRegisterFragmentThird();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.patient_login_fragment_container, nextFrag, "New Frag")
                            .addToBackStack("second").commit();
                } else {
                    Toast.makeText(getActivity(), "Data not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}