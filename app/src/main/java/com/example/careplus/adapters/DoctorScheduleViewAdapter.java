package com.example.careplus.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.careplus.R;
import com.example.careplus.localStorage.DoctorViewSchedule;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class DoctorScheduleViewAdapter extends RecyclerView.Adapter<DoctorScheduleViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<DoctorViewSchedule> appointmentList;
    FirebaseFirestore db;
    FirebaseDatabase realTimeDb;
    DatabaseReference confirmRef;
    public DoctorScheduleViewAdapter(Context context, ArrayList<DoctorViewSchedule> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public DoctorScheduleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.doctor_schedule_view, parent, false);
        return new DoctorScheduleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorScheduleViewAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        realTimeDb = FirebaseDatabase.getInstance();
        confirmRef = realTimeDb.getReference("ConfirmedAppointments");
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        switch(appointmentList.get(position).getDay()) {
            case "Tuesday":
                nextMonday = nextMonday.plusDays(1);
                break;
            case "Wednesday":
                nextMonday = nextMonday.plusDays(2);
                break;
            case "Thursday":
                nextMonday = nextMonday.plusDays(3);
                break;
            case "Friday":
                nextMonday = nextMonday.plusDays(4);
                break;
            case "Saturday":
                nextMonday = nextMonday.plusDays(5);
                break;
            case "Sunday":
                nextMonday = nextMonday.plusDays(6);
                break;
            default:
                break;

        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.clinicName.setText(appointmentList.get(position).getClinicName());
        holder.date.setText(nextMonday.toString());
        holder.startTime.setText(appointmentList.get(position).getStartTime());
        holder.endTime.setText(appointmentList.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView clinicName, date;
        TextView startTime, endTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            clinicName = itemView.findViewById(R.id.clinic_name);
            date = itemView.findViewById(R.id.date);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
        }
    }
}
