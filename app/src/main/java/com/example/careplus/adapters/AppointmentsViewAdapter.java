package com.example.careplus.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AppointmentsViewAdapter extends RecyclerView.Adapter<AppointmentsViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<NewAppointmentRequest> appointmentList;
    FirebaseFirestore db;
    FirebaseDatabase realTimeDb;
    DatabaseReference confirmRef, pseudoRef;
    public AppointmentsViewAdapter(Context context, ArrayList<NewAppointmentRequest> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.booking_view_row, parent, false);
        return new AppointmentsViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsViewAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        realTimeDb = FirebaseDatabase.getInstance();
        confirmRef = realTimeDb.getReference("ConfirmedAppointments");
        pseudoRef =  realTimeDb.getReference("PseudoAppointments");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.patientName.setText(appointmentList.get(position).getPatientName());
        holder.patientEmail.setText(appointmentList.get(position).getPatientEmail());
        holder.image.setImageResource(appointmentList.get(position).getImage());
        holder.appointmentDate.setText(appointmentList.get(position).getAppointmentDate());
        holder.appointmentTime.setText(appointmentList.get(position).getAppointmentTime());
        holder.insuranceProvider.setText(appointmentList.get(position).getInsuranceProvider());
        holder.acceptBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String , Object> confirmAppointment = new HashMap<>();
                confirmAppointment.put("patientEmail", appointmentList.get(holder.getAbsoluteAdapterPosition()).getPatientEmail());
                confirmAppointment.put("patientName", appointmentList.get(holder.getAbsoluteAdapterPosition()).getPatientName());
                confirmAppointment.put("clinicEmail", user.getEmail());
                confirmAppointment.put("appointmentDate", appointmentList.get(holder.getAbsoluteAdapterPosition()).getAppointmentDate());
                confirmAppointment.put("appointmentTime", appointmentList.get(holder.getAbsoluteAdapterPosition()).appointmentTime);
                confirmAppointment.put("insuranceProvider", appointmentList.get(holder.getAbsoluteAdapterPosition()).getInsuranceProvider());
                Map<String, Object> fin = new HashMap<>();
                fin.put(UUID.randomUUID().toString(), confirmAppointment);
                confirmRef.updateChildren(fin);
                pseudoRef.child(appointmentList.get(holder.getAbsoluteAdapterPosition()).getID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                appointmentList.remove(holder.getAbsoluteAdapterPosition());
                                AppointmentsViewAdapter adapter = new AppointmentsViewAdapter(view.getContext(), appointmentList);
                                adapter.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(view.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        holder.declineBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pseudoRef.child(appointmentList.get(holder.getAbsoluteAdapterPosition()).getID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                        AppointmentsViewAdapter adapter = new AppointmentsViewAdapter(view.getContext(), appointmentList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(view.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView patientName, patientEmail, insuranceProvider;
        TextView appointmentDate, appointmentTime;
        TextView acceptBooking, declineBooking;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patient_name);
            patientEmail = itemView.findViewById(R.id.patient_email);
            image = itemView.findViewById(R.id.patient_image);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
            appointmentTime = itemView.findViewById(R.id.appointment_time);
            insuranceProvider = itemView.findViewById(R.id.patient_insurance);
            acceptBooking = itemView.findViewById(R.id.approve_request);
            declineBooking = itemView.findViewById(R.id.decline_request);
        }
    }
}
