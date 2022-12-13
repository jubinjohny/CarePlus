package com.example.careplus.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careplus.R;
import com.example.careplus.localStorage.NewAppointmentRequest;
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

public class UpcomingAppointmentsViewAdapter extends RecyclerView.Adapter<UpcomingAppointmentsViewAdapter.MyViewHolder> {
    Context context;
    String person;
    ArrayList<NewAppointmentRequest> appointmentList;
    FirebaseFirestore db;
    FirebaseDatabase realTimeDb;
    DatabaseReference confirmRef;
    public UpcomingAppointmentsViewAdapter(Context context, ArrayList<NewAppointmentRequest> appointmentList, String person) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.person = person;
    }

    @NonNull
    @Override
    public UpcomingAppointmentsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.upcoming_view_row, parent, false);
        return new UpcomingAppointmentsViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAppointmentsViewAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        realTimeDb = FirebaseDatabase.getInstance();
        confirmRef = realTimeDb.getReference("ConfirmedAppointments");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.patientName.setText(appointmentList.get(position).getPatientName());
        holder.appointmentDate.setText(appointmentList.get(position).getAppointmentDate());
        holder.appointmentTime.setText(appointmentList.get(position).getAppointmentTime());
        holder.insuranceProvider.setText(appointmentList.get(position).getInsuranceProvider());
        holder.doctorName.setText(appointmentList.get(position).getDoctorName());
        holder.bookingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.booking_dialogue_layout);
                TextView patientName = (TextView) dialog.findViewById(R.id.patient_name);
                TextView patientEmail = (TextView) dialog.findViewById(R.id.patient_email);
                ImageView callBtn = (ImageView) dialog.findViewById(R.id.call_patient);
                ImageView mailBtn = (ImageView) dialog.findViewById(R.id.mail_patient);
                patientName.setText(appointmentList.get(holder.getAbsoluteAdapterPosition()).getPatientName());
                patientEmail.setText(appointmentList.get(holder.getAbsoluteAdapterPosition()).getPatientEmail());
                callBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (person.contentEquals("Patients")) {
                            db.collection("Clinics").whereEqualTo("email", appointmentList.get(holder.getAbsoluteAdapterPosition()).getClinicEmail()).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                                String phoneNumber = doc.getData().get("phone").toString().replaceAll("[^0-9]", "");
                                                phoneNumber = "+"+phoneNumber;
                                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                callIntent.setData(Uri.parse("tel:"+ phoneNumber));//change the number
                                                context.startActivity(callIntent);
                                            }
                                        }
                                    }
                                });
                        } else {
                            db.collection("Patients").whereEqualTo("email", appointmentList.get(holder.getAbsoluteAdapterPosition()).getPatientEmail()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                for(QueryDocumentSnapshot doc : task.getResult()) {
                                                    String phoneNumber = doc.getData().get("phone").toString().replaceAll("[^0-9]", "");
                                                    phoneNumber = "+"+phoneNumber;
                                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                    callIntent.setData(Uri.parse("tel:"+ phoneNumber));//change the number
                                                    context.startActivity(callIntent);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
                mailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(person.contentEquals("Patients")) {
                            String email = appointmentList.get(holder.getAbsoluteAdapterPosition()).getClinicEmail();
                            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                            context.startActivity(emailIntent);
                        } else {
                            String email = appointmentList.get(holder.getAbsoluteAdapterPosition()).getPatientEmail();
                            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                            context.startActivity(emailIntent);
                        }
                    }
                });
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView patientName, insuranceProvider, doctorName;
        TextView appointmentDate, appointmentTime;
        LinearLayout bookingView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patient_name);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
            appointmentTime = itemView.findViewById(R.id.appointment_time);
            insuranceProvider = itemView.findViewById(R.id.patient_insurance);
            doctorName = itemView.findViewById(R.id.doctor_name);
            bookingView = itemView.findViewById(R.id.booking_view);
        }
    }
}
