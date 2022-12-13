package com.example.careplus.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careplus.R;
import com.example.careplus.localStorage.ClinicsViewData;
import com.example.careplus.localStorage.ScheduleDoctorsData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DoctorBookingViewAdapter extends RecyclerView.Adapter<DoctorBookingViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ScheduleDoctorsData> doctorsList;
    ArrayList<ClinicsViewData> clinicList;
    Map<String, Object> doctors = new HashMap<>();
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef, timeRef, changeTimes;
    LocalDate selectedDate;
    ArrayList<String> getTimes;
    String finalSelectedWeek = "";
    Map<String, String> timeMap;
    public DoctorBookingViewAdapter(Context context, ArrayList<ScheduleDoctorsData> doctorsList, ArrayList<ClinicsViewData> clinicList, LocalDate selectedDate) {
        this.context = context;
        this.doctorsList = doctorsList;
        this.clinicList = clinicList;
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public DoctorBookingViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.doctors_view_row, parent, false);
        return new DoctorBookingViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorBookingViewAdapter.MyViewHolder holder, int position) {
        Map<String, Object> scheduleEntry = new HashMap<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Map<String, Object>[] newAppointment = new Map[]{new HashMap<>()};
        final ArrayList<String>[] timesMap = new ArrayList[]{new ArrayList<>()};
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("PseudoAppointments");
        changeTimes = realTimeDb.getReference("DoctorsNextSchedule").child(doctorsList.get(position).getDoctorName()).child(selectedDate.getDayOfWeek().toString().charAt(0) + selectedDate.getDayOfWeek().toString().substring(1).toLowerCase(Locale.ROOT)).child("timeslots");
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        holder.doctorName.setText(doctorsList.get(position).getDoctorName());
        holder.availability.setText("Availability : " + doctorsList.get(position).getStartTime() + " to " +  doctorsList.get(position).getEndTime());
        holder.doctorImage.setImageResource(doctorsList.get(position).getImage());
        holder.checkImage.setImageResource(doctorsList.get(position).getSetCheck());
        holder.checkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nextMonday.plusDays(7).getDayOfMonth() >= selectedDate.getDayOfMonth()) {
                    finalSelectedWeek = "week1";
                } else if(nextMonday.plusDays(14).getDayOfMonth() >= selectedDate.getDayOfMonth()) {
                    finalSelectedWeek = "week2";
                }else if(nextMonday.plusDays(21).getDayOfMonth() >= selectedDate.getDayOfMonth()) {
                    finalSelectedWeek = "week3";
                } else if(nextMonday.plusDays(28).getDayOfMonth() >= selectedDate.getDayOfMonth()) {
                    finalSelectedWeek = "week4";
                }
                timeRef = realTimeDb.getReference("DoctorsNextSchedule").child(doctorsList.get(holder.getAbsoluteAdapterPosition()).getDoctorName()).child(selectedDate.getDayOfWeek().toString().charAt(0) + selectedDate.getDayOfWeek().toString().substring(1).toLowerCase(Locale.ROOT));
                ValueEventListener getTimeSlots = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AlertDialog.Builder timePicker = new AlertDialog.Builder(holder.itemView.getContext());
                        timePicker.setTitle("Select Time");
                        Log.d("===", finalSelectedWeek);
                        getTimes = (ArrayList<String>) snapshot.child("timeslots").child(finalSelectedWeek).getValue();
                        timePicker.setItems(getTimes.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface timeInterface, int i) {
                                newAppointment[0].put("doctorName", doctorsList.get(holder.getAbsoluteAdapterPosition()).getDoctorName());
                                newAppointment[0].put("day", doctorsList.get(holder.getAbsoluteAdapterPosition()).getDay());
                                newAppointment[0].put("clinicName", doctorsList.get(holder.getAbsoluteAdapterPosition()).getClinicName());
                                newAppointment[0].put("clinicEmail", doctorsList.get(holder.getAbsoluteAdapterPosition()).getClinicEmail());
                                newAppointment[0].put("appointmentDate", doctorsList.get(holder.getAbsoluteAdapterPosition()).getDate());
                                newAppointment[0].put("appointmentTime",  getTimes.get(i));
                                newAppointment[0].put("patientName", doctorsList.get(holder.getAbsoluteAdapterPosition()).getPatientName());
                                newAppointment[0].put("patientEmail" , user.getEmail());
                                if(doctorsList.get(holder.getAbsoluteAdapterPosition()).getInsuranceProvider().length() > 0) {
                                    newAppointment[0].put("insuranceProvider", doctorsList.get(holder.getAbsoluteAdapterPosition()).getInsuranceProvider());
                                } else {
                                    newAppointment[0].put("insuranceProvider", "Insurance Provider Unavailable");
                                }
                                Map<String, Object> fin = new HashMap<>();
                                fin.put(UUID.randomUUID().toString(), newAppointment[0]);
                                dbRef.updateChildren(fin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        AlertDialog.Builder successAlert = new AlertDialog.Builder(view.getContext());
                                        successAlert.setTitle("Booking Request Sent");
                                        successAlert.setMessage("Your booking request for "+  doctorsList.get(holder.getAbsoluteAdapterPosition()).getDate() + " at " +
                                                doctorsList.get(holder.getAbsoluteAdapterPosition()).getClinicEmail() + " is successfully sent.!");
                                        successAlert.setCancelable(true);
                                        successAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                holder.checkImage.setImageResource(R.drawable.ic_check);
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        successAlert.show();
                                    }
                                });
                                getTimes.remove(i);
                                clinicList.get(holder.getAbsoluteAdapterPosition()).setBookingPending("Pending Approval");
                                Map<String, Object> temp = new HashMap<>();
                                temp.put(finalSelectedWeek, getTimes);
                                changeTimes.updateChildren(temp);
                                timeInterface.dismiss();
                            }
                        });
                        timePicker.setPositiveButton("OK", null);
                        timePicker.setCancelable(true);
                        timePicker.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                };
                timeRef.addListenerForSingleValueEvent(getTimeSlots);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName, availability;
        ImageView doctorImage, checkImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctor_name);
            availability = itemView.findViewById(R.id.availability);
            doctorImage = itemView.findViewById(R.id.doctor_image);
            checkImage = itemView.findViewById(R.id.add);
        }
    }
}
