package com.example.careplus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careplus.R;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.example.careplus.localStorage.UpcomingScheduleData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UpcomingScheduleViewAdapter extends RecyclerView.Adapter<UpcomingScheduleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<UpcomingScheduleData> scheduleList;
    FirebaseFirestore db;
    FirebaseDatabase realTimeDb;
    DatabaseReference scheduleRef;
    public UpcomingScheduleViewAdapter(Context context, ArrayList<UpcomingScheduleData> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public UpcomingScheduleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.schedule_view_row, parent, false);
        return new UpcomingScheduleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingScheduleViewAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        realTimeDb = FirebaseDatabase.getInstance();
        scheduleRef = realTimeDb.getReference("ConfirmedAppointments");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.date.setText(scheduleList.get(position).getDate());
        holder.day.setText(scheduleList.get(position).getDay());
        holder.startTime.setText("Start Time : " + scheduleList.get(position).getStartTime());
        holder.endTime.setText("End Time : " + scheduleList.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView day, date;
        TextView startTime, endTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.schedule_day);
            date = itemView.findViewById(R.id.schedule_date);
            startTime = itemView.findViewById(R.id.schedule_FN);
            endTime = itemView.findViewById(R.id.schedule_AN);
        }
    }
}
