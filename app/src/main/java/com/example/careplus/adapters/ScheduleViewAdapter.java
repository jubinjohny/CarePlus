package com.example.careplus.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careplus.R;
import com.example.careplus.localStorage.DoctorViewCardData;
import com.example.careplus.localStorage.ScheduleViewCardData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ScheduleViewCardData> scheduleList;
    FirebaseFirestore db;
    public ScheduleViewAdapter(Context context, ArrayList<ScheduleViewCardData> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ScheduleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.schedule_view_row, parent, false);
        return new ScheduleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.day.setText(scheduleList.get(position).getDay());
        holder.date.setText(scheduleList.get(position).getDate());
        holder.FNshift.setText(scheduleList.get(position).getFNshift());
        holder.ANshift.setText(scheduleList.get(position).getANshift());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView day, date;
        TextView FNshift, ANshift;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.schedule_day);
            date = itemView.findViewById(R.id.schedule_date);
            FNshift = itemView.findViewById(R.id.schedule_FN);
            ANshift = itemView.findViewById(R.id.schedule_AN);
        }
    }
}
