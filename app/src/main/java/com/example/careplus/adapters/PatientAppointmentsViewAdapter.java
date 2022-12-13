package com.example.careplus.adapters;

import android.content.Context;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PatientAppointmentsViewAdapter extends RecyclerView.Adapter<PatientAppointmentsViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<NewAppointmentRequest> appointmentList;
    FirebaseFirestore db;
    FirebaseDatabase realTimeDb;
    DatabaseReference cancelRefClinic, pseudoRef, cancelRefDoctor;
    String clinicName;
    public PatientAppointmentsViewAdapter(Context context, ArrayList<NewAppointmentRequest> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public PatientAppointmentsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.booking_patient_view_row, parent, false);
        return new PatientAppointmentsViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAppointmentsViewAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        realTimeDb = FirebaseDatabase.getInstance();
        cancelRefClinic = realTimeDb.getReference("CancelledAppointmentsClinics");
        cancelRefDoctor = realTimeDb.getReference("CancelledAppointmentsDoctor");
        pseudoRef =  realTimeDb.getReference("ConfirmedAppointments");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Clinics").whereEqualTo("email", user.getEmail()).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            clinicName = doc.getData().get("name").toString();
                        }
                    }
                }
            });
        holder.patientName.setText(appointmentList.get(position).getClinicName());
        holder.patientEmail.setText(appointmentList.get(position).getClinicName());
        holder.image.setImageResource(appointmentList.get(position).getImage());
        holder.appointmentDate.setText(appointmentList.get(position).getAppointmentDate());
        holder.appointmentTime.setText(appointmentList.get(position).getAppointmentTime());
        holder.insuranceProvider.setText(appointmentList.get(position).getInsuranceProvider());
        holder.doctorName.setText("Doctor: " + appointmentList.get(position).getDoctorName());
        holder.cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String , Object> confirmAppointment = new HashMap<>();
                confirmAppointment.put("clinicEmail", appointmentList.get(holder.getAbsoluteAdapterPosition()).getClinicEmail());
                confirmAppointment.put("clinicName", appointmentList.get(holder.getAbsoluteAdapterPosition()).getClinicName());
                confirmAppointment.put("patientEmail", user.getEmail());
                confirmAppointment.put("doctorName", appointmentList.get(holder.getAbsoluteAdapterPosition()).getDoctorName());
                confirmAppointment.put("appointmentDate", appointmentList.get(holder.getAbsoluteAdapterPosition()).getAppointmentDate());
                confirmAppointment.put("appointmentTime", appointmentList.get(holder.getAbsoluteAdapterPosition()).getAppointmentTime());
                confirmAppointment.put("insuranceProvider", appointmentList.get(holder.getAbsoluteAdapterPosition()).getInsuranceProvider());
                Map<String, Object> fin = new HashMap<>();
                fin.put(UUID.randomUUID().toString(), confirmAppointment);
                cancelRefClinic.updateChildren(fin);
                cancelRefDoctor.updateChildren(fin);
                pseudoRef.child(appointmentList.get(holder.getAbsoluteAdapterPosition()).getID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                        PatientAppointmentsViewAdapter adapter = new PatientAppointmentsViewAdapter(view.getContext(), appointmentList);
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

        TextView patientName, patientEmail, insuranceProvider, doctorName;
        TextView appointmentDate, appointmentTime;
        TextView cancelBooking;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patient_name);
            patientEmail = itemView.findViewById(R.id.patient_email);
            doctorName = itemView.findViewById(R.id.doctor_name);
            image = itemView.findViewById(R.id.patient_image);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
            appointmentTime = itemView.findViewById(R.id.appointment_time);
            insuranceProvider = itemView.findViewById(R.id.patient_insurance);
            cancelBooking = itemView.findViewById(R.id.cancel_booking);
        }
    }
}
