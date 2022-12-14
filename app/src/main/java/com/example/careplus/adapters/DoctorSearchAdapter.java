package com.example.careplus.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.careplus.localStorage.DoctorViewCardData;
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
import java.util.HashMap;
import java.util.List;

public class DoctorSearchAdapter extends RecyclerView.Adapter<DoctorSearchAdapter.MyViewHolder> {
    Context context;
    ArrayList<DoctorViewCardData> doctorList;
    FirebaseFirestore db;
    public DoctorSearchAdapter(Context context, ArrayList<DoctorViewCardData> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DoctorSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new DoctorSearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorSearchAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        List<String> requests;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.doctorName.setText(doctorList.get(position).getDoctorName());
        holder.doctorSpecial.setText(doctorList.get(position).getSpecialization());
        holder.doctorImage.setImageResource(doctorList.get(position).getImage());
        holder.requestTime.setText(doctorList.get(position).getRequestPending());
        holder.requestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               db.collection("Doctors").document(doctorList.get(holder.getAbsoluteAdapterPosition()).getID())
                       .update("requests", FieldValue.arrayUnion(user.getEmail())).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()) {
                                   doctorList.get(holder.getAbsoluteAdapterPosition()).setRequestPending("Pending Approval");
                                   holder.requestTime.setText(doctorList.get(holder.getAbsoluteAdapterPosition()).getRequestPending());
                                   db.collection("Clinics").whereEqualTo("email", user.getEmail())
                                           .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                                   if(task2.isSuccessful()) {
                                                       for(QueryDocumentSnapshot doc : task2.getResult()) {
                                                           db.collection("Clinics").document(doc.getId())
                                                                   .update("pendingRequests",  FieldValue.arrayUnion(doctorList.get(holder.getAbsoluteAdapterPosition()).getEmail()));
                                                       }
                                                   }
                                               }
                                           });
                               }
                           }
                       });
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView doctorImage;
        TextView doctorName, doctorSpecial;
        TextView requestTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.doctor_image);
            doctorName = itemView.findViewById(R.id.doctor_name);
            doctorSpecial = itemView.findViewById(R.id.doctor_specialization);
            requestTime = itemView.findViewById(R.id.request_time);
        }
    }
}
