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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpcomingAppointmentsViewAdapter extends RecyclerView.Adapter<UpcomingAppointmentsViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<NewAppointmentRequest> appointmentList;
    FirebaseFirestore db;
    FirebaseDatabase realTimeDb;
    DatabaseReference confirmRef;
    public UpcomingAppointmentsViewAdapter(Context context, ArrayList<NewAppointmentRequest> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
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
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView patientName, insuranceProvider;
        TextView appointmentDate, appointmentTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patient_name);
            appointmentDate = itemView.findViewById(R.id.appointment_date);
            appointmentTime = itemView.findViewById(R.id.appointment_time);
            insuranceProvider = itemView.findViewById(R.id.patient_insurance);
        }
    }
}
