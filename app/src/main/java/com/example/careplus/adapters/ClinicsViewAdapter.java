package com.example.careplus.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careplus.R;
import com.example.careplus.localStorage.Appointments;
import com.example.careplus.localStorage.ClinicsViewData;
import com.example.careplus.localStorage.ScheduleDoctorsData;
import com.example.careplus.localStorage.ScheduleViewCardData;
import com.example.careplus.patient.home.PatientProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class ClinicsViewAdapter extends RecyclerView.Adapter<ClinicsViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ClinicsViewData> clinicsList;
    FirebaseFirestore db;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef, dbRefDoctors;
    String patientName, insuranceProvider;
    public ClinicsViewAdapter(Context context, ArrayList<ClinicsViewData> clinicsList) {
        this.context = context;
        this.clinicsList = clinicsList;
    }

    @NonNull
    @Override
    public ClinicsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.clinics_view_row, parent, false);
        return new ClinicsViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicsViewAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        realTimeDb = FirebaseDatabase.getInstance();
        dbRef = realTimeDb.getReference("PseudoAppointments");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       db.collection("Patients").whereEqualTo("email", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                patientName = doc.getData().get("firstName").toString() + " " + doc.getData().get("lastName").toString();
                                insuranceProvider = doc.getData().get("insuranceProvider").toString();
                            }
                        }
                    }
                });
        holder.name.setText(clinicsList.get(position).getName());
        holder.city.setText(clinicsList.get(position).getCity());
        holder.image.setImageResource(clinicsList.get(position).getImage());
        holder.booking.setText(clinicsList.get(position).getBookingPending());
        holder.booking.setOnClickListener(new View.OnClickListener() {
            LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            LocalDate comingMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            String dateStart = comingMonday.plusDays(1).getDayOfMonth() + "-" + comingMonday.getMonth().toString().charAt(0) + comingMonday.getMonth().toString().substring(1).toLowerCase(Locale.ROOT) + "-" + comingMonday.getYear();
            String dateEnd = comingMonday.plusDays(28).getDayOfMonth() + "-" + comingMonday.plusDays(28).getMonth().toString().charAt(0) + comingMonday.plusDays(28).getMonth().toString().substring(1).toLowerCase(Locale.ROOT) + "-" + comingMonday.plusDays(28).getYear();
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                dbRefDoctors = realTimeDb.getReference("NextSchedule").child(clinicsList.get(holder.getAbsoluteAdapterPosition()).getName());
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        nextMonday = nextMonday.plusDays(day - nextMonday.getDayOfMonth());
                        ArrayList<ScheduleDoctorsData> doctors = new ArrayList<ScheduleDoctorsData>();
                        AlertDialog.Builder doctorOptions = new AlertDialog.Builder(view.getContext());
                        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_dialogue_layout, null);
                        RecyclerView alertView = (RecyclerView) dialogView.findViewById(R.id.doctors_view);
                        ValueEventListener doctorListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot doc : snapshot.getChildren()) {
                                if(doc.getKey().toUpperCase(Locale.ROOT).contentEquals(nextMonday.getDayOfWeek().toString())) {
                                    for(DataSnapshot each : doc.getChildren()) {
                                        ScheduleDoctorsData entry = new ScheduleDoctorsData(each.child("doctorName").getValue().toString(), each.child("startTime").getValue().toString(),
                                                each.child("endTime").getValue().toString(), R.drawable.doctor, R.drawable.ic_not_check, clinicsList.get(holder.getAbsoluteAdapterPosition()).getName(), nextMonday.getDayOfWeek().toString(),
                                                clinicsList.get(holder.getAbsoluteAdapterPosition()).getEmail(), nextMonday.toString(), patientName, insuranceProvider);
                                        doctors.add(entry);
                                    }
                                }
                            }
                            if(!doctors.isEmpty()) {
                                DoctorBookingViewAdapter adapter = new DoctorBookingViewAdapter(view.getContext(), doctors, clinicsList, nextMonday);
                                alertView.setAdapter(adapter);
                                alertView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                        };
                        dbRefDoctors.addValueEventListener(doctorListener);
                        doctorOptions.setView(dialogView);
                        doctorOptions.setTitle("Select Doctor");
                        doctorOptions.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        doctorOptions.setCancelable(true);
                        doctorOptions.show();
                    }
                },
                nextMonday.getYear(),
                nextMonday.getMonthValue()-1,
                nextMonday.getDayOfMonth());
                DateTimeFormatter dateFormatter
                        = DateTimeFormatter.ofPattern("d-MMMM-uuuu", Locale.ENGLISH);
                long maxDate = LocalDate.parse( dateEnd+"", dateFormatter)
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli();
                long minDate = LocalDate.parse(dateStart+"", dateFormatter)
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant()
                        .toEpochMilli();
                dialog.getDatePicker().setMinDate(minDate);
                dialog.getDatePicker().setMaxDate(maxDate);
                Log.d("Here", dateEnd + " " + dateStart);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clinicsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, city;
        TextView booking;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clinic_name);
            city = itemView.findViewById(R.id.clinic_address);
            image = itemView.findViewById(R.id.clinic_image);
            booking = itemView.findViewById(R.id.book_appointment);
        }
    }
}
