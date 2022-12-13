package com.example.careplus.clinic.auth;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.careplus.R;
import com.example.careplus.databinding.FragmentClinicRegisterSecondBinding;
import com.example.careplus.databinding.FragmentClinicRegisterThirdBinding;
import com.example.careplus.localStorage.SQLiteDBHelperClinic;

import java.util.Calendar;

public class ClinicRegisterFragmentThird extends Fragment {
    FragmentClinicRegisterThirdBinding binding;
    SQLiteDBHelperClinic DB;
    int weekdayStartHour, weekdayStartMin, weekdayEndHour, weekdayEndMin, weekendStartHour, weekendStartMin, weekendEndHour, weekendEndMin;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClinicRegisterThirdBinding.inflate(inflater, container, false);
        DB = new SQLiteDBHelperClinic(getContext());
        binding.weekdaysStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.weekdayCheckbox.isChecked()) {
                    final Calendar calendar = Calendar.getInstance();
                    int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
                    int minuteNow = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timer = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timer, int hourOfDay, int minute) {
                                    weekdayStartHour = hourOfDay;
                                    weekdayStartMin = minute;
                                    if(minute < 10) {
                                        if (hourOfDay < 10) {
                                            binding.weekdaysStartTime.setText("0"+hourOfDay + ":" + "0"+minute);
                                        } else {
                                            binding.weekdaysStartTime.setText(hourOfDay + ":" + "0"+minute);
                                        }
                                    } else {
                                        binding.weekdaysStartTime.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, hourNow, minuteNow, false);
                    timer.show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Select Weekdays")
                            .setMessage("Please select weekdays to choose time.")
                            .setNegativeButton("OK", null)
                            .show();
                }
            }
        });
        binding.weekdaysEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.weekdayCheckbox.isChecked()) {
                    final Calendar calendar = Calendar.getInstance();
                    int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
                    int minuteNow = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timer = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timer, int hourOfDay, int minute) {
                                    weekdayEndHour = hourOfDay;
                                    weekdayEndMin = minute;
                                    Log.d("Weekday End Time :", "" + weekdayEndHour + weekdayEndMin );
                                    if (minute < 10) {
                                        if (hourOfDay < 10) {
                                            binding.weekdaysEndTime.setText("0" + hourOfDay + ":" + "0" + minute);
                                        } else {
                                            binding.weekdaysEndTime.setText(hourOfDay + ":" + "0" + minute);
                                        }
                                    } else {
                                        binding.weekdaysEndTime.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, hourNow, minuteNow, false);
                    timer.show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Select Weekdays")
                            .setMessage("Please select weekdays to choose time.")
                            .setNegativeButton("OK", null)
                            .show();
                }
            }
        });
        binding.weekendStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.weekendCheckbox.isChecked()) {
                    final Calendar calendar = Calendar.getInstance();
                    int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
                    int minuteNow = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timer = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timer, int hourOfDay, int minute) {
                                    weekendStartHour = hourOfDay;
                                    weekendStartMin = minute;
                                    Log.d("Weekend Start Time :", "" + weekendStartHour + weekendStartMin );
                                    if (minute < 10) {
                                        if (hourOfDay < 10) {
                                            binding.weekendStartTime.setText("0" + hourOfDay + ":" + "0" + minute);
                                        } else {
                                            binding.weekendStartTime.setText(hourOfDay + ":" + "0" + minute);
                                        }
                                    } else {
                                        binding.weekendStartTime.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, hourNow, minuteNow, false);
                    timer.show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Select Weekends")
                            .setMessage("Please select weekends to choose time.")
                            .setNegativeButton("OK", null)
                            .show();
                }
            }
        });
        binding.weekendEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.weekendCheckbox.isChecked()) {
                    final Calendar calendar = Calendar.getInstance();
                    int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
                    int minuteNow = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timer = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timer, int hourOfDay, int minute) {
                                    weekendEndHour = hourOfDay;
                                    weekendEndMin = minute;
                                    Log.d("Weekend End Time :", "" + weekendEndHour + weekendEndMin );
                                    if (minute < 10) {
                                        if (hourOfDay < 10) {
                                            binding.weekendEndTime.setText("0" + hourOfDay + ":" + "0" + minute);
                                        } else {
                                            binding.weekendEndTime.setText(hourOfDay + ":" + "0" + minute);
                                        }
                                    } else {
                                        binding.weekendEndTime.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, hourNow, minuteNow, false);
                    timer.show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Select Weekends")
                            .setMessage("Please select weekends to choose time.")
                            .setNegativeButton("OK", null)
                            .show();
                }
            }
        });

        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String website = binding.clinicWebsite.getText().toString().trim();
                String clinicID = DB.getClinicID();
                Boolean updateClinicDetails = DB.updateClinicThird(clinicID, weekdayStartHour, weekdayStartMin,weekdayEndHour,weekdayEndMin,weekendStartHour,
                        weekendStartMin,weekendEndHour,weekendEndMin, website);
                if(updateClinicDetails == true) {
                    ClinicRegistrationFragmentLast nextFrag = new ClinicRegistrationFragmentLast();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.clinic_login_fragment_container, nextFrag, "New Frag")
                            .addToBackStack("first").commit();
                } else {
                    Toast.makeText(getActivity(), "Data not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }
}