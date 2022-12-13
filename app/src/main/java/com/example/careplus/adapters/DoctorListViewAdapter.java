package com.example.careplus.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careplus.R;
import com.example.careplus.localStorage.SQLiteDBHelperDoctor;
import com.example.careplus.localStorage.SQLiteDBScheduleDoctors;
import com.example.careplus.localStorage.ScheduleDoctorsData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorListViewAdapter extends RecyclerView.Adapter<DoctorListViewAdapter.MyViewHolder> {
    Context context;
    View view;
    ArrayList<ScheduleDoctorsData> doctorsList, scheduledList;
    int destination;
    FirebaseFirestore db;
    FirebaseDatabase realTimeDb;
    DatabaseReference dbRef;
    SQLiteDBScheduleDoctors DB;
    int numberOfDoctors = 0;
    Map<String, Object> doctors = new HashMap<>();
    public DoctorListViewAdapter(Context context, ArrayList<ScheduleDoctorsData> doctorsList, ArrayList<ScheduleDoctorsData> scheduledList, int destination, View view) {
        this.context = context;
        this.doctorsList = doctorsList;
        this.scheduledList = scheduledList;
        this.destination = destination;
        this.view = view;
    }

    @NonNull
    @Override
    public DoctorListViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.doctors_view_row, parent, false);
        return new DoctorListViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListViewAdapter.MyViewHolder holder, int position) {
        TextView destTV = (TextView) view.findViewById(destination);
        db = FirebaseFirestore.getInstance();
        Map<String, Object> insert = new HashMap<>();
        realTimeDb = FirebaseDatabase.getInstance();
        scheduledList = new ArrayList<ScheduleDoctorsData>();
        dbRef = realTimeDb.getReference("NextSchedule").child(doctorsList.get(position).getClinicName());

        DB = new SQLiteDBScheduleDoctors(holder.itemView.getContext());
        Map<String, Object> scheduleEntry = new HashMap<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.doctorName.setText(doctorsList.get(position).getDoctorName());
        holder.availability.setText("Availability : " + doctorsList.get(position).getStartTime() + " to " +  doctorsList.get(position).getEndTime());
        holder.doctorImage.setImageResource(doctorsList.get(position).getImage());
        holder.checkImage.setImageResource(doctorsList.get(position).getSetCheck());
        holder.checkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scheduledList.contains(doctorsList.get(holder.getAbsoluteAdapterPosition()))) {
                    holder.checkImage.setImageResource(R.drawable.ic_not_check);
                    scheduleEntry.remove(holder.getAbsoluteAdapterPosition());
                    doctorsList.get(holder.getAbsoluteAdapterPosition()).setSetCheck(R.drawable.ic_not_check);
                    scheduledList.remove(doctorsList.get(holder.getAbsoluteAdapterPosition()));
                    dbRef.child(doctorsList.get(holder.getAbsoluteAdapterPosition()).getDay()).child(String.valueOf(holder.getAbsoluteAdapterPosition())).removeValue();
                    numberOfDoctors = numberOfDoctors - 1;
                } else {
                    holder.checkImage.setImageResource(R.drawable.ic_check);
                    ScheduleDoctorsData entry = new ScheduleDoctorsData(doctorsList.get(holder.getAbsoluteAdapterPosition()).getDoctorName(),
                            doctorsList.get(holder.getAbsoluteAdapterPosition()).getStartTime(), doctorsList.get(holder.getAbsoluteAdapterPosition()).getEndTime());
                    doctors.put(String.valueOf(numberOfDoctors), entry);
                    scheduleEntry.put(doctorsList.get(holder.getAbsoluteAdapterPosition()).getDay(), doctors);
                    dbRef.updateChildren(scheduleEntry);
                    doctorsList.get(holder.getAbsoluteAdapterPosition()).setSetCheck(R.drawable.ic_check);
                    scheduledList.add(doctorsList.get(holder.getAbsoluteAdapterPosition()));
                    numberOfDoctors = numberOfDoctors + 1;
                }
                if(numberOfDoctors > 0 ) {
                    destTV.setBackgroundResource(R.drawable.rounded_green_buttons);
                } else {
                    destTV.setBackgroundResource(R.drawable.rounded_white_buttons);
                }
            }
        });
        if(numberOfDoctors > 0 ) {
            destTV.setBackgroundResource(R.drawable.rounded_green_buttons);
        } else {
            destTV.setBackgroundResource(R.drawable.rounded_white_buttons);
        }
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
