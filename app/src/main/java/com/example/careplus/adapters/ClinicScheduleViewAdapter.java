package com.example.careplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careplus.R;
import com.example.careplus.localStorage.SQLiteDBScheduleDoctors;
import com.example.careplus.localStorage.ScheduleDoctorsData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClinicScheduleViewAdapter extends RecyclerView.Adapter<ClinicScheduleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ScheduleDoctorsData> doctorsList;
    Map<String, Object> doctors = new HashMap<>();
    public ClinicScheduleViewAdapter(Context context, ArrayList<ScheduleDoctorsData> doctorsList) {
        this.context = context;
        this.doctorsList = doctorsList;
    }

    @NonNull
    @Override
    public ClinicScheduleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.doctors_view_row, parent, false);
        return new ClinicScheduleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicScheduleViewAdapter.MyViewHolder holder, int position) {
        Map<String, Object> scheduleEntry = new HashMap<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.doctorName.setText(doctorsList.get(position).getDoctorName());
        holder.availability.setText("Availability : " + doctorsList.get(position).getStartTime() + " to " +  doctorsList.get(position).getEndTime());
        holder.doctorImage.setImageResource(doctorsList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName, availability;
        ImageView doctorImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctor_name);
            availability = itemView.findViewById(R.id.availability);
            doctorImage = itemView.findViewById(R.id.doctor_image);
        }
    }
}
