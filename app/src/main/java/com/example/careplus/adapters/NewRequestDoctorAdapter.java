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
import com.example.careplus.localStorage.RequestViewCardData;
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

public class NewRequestDoctorAdapter extends RecyclerView.Adapter<NewRequestDoctorAdapter.MyViewHolder> {
    Context context;
    ArrayList<RequestViewCardData> clinicList;
    FirebaseFirestore db;
    public NewRequestDoctorAdapter(Context context, ArrayList<RequestViewCardData> clinicList) {
        this.context = context;
        this.clinicList = clinicList;
    }

    @NonNull
    @Override
    public NewRequestDoctorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.request_view_row, parent, false);
        return new NewRequestDoctorAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewRequestDoctorAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        List<String> requests;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        holder.clinicName.setText(clinicList.get(position).getClinicName());
        holder.clinicAddress.setText(clinicList.get(position).getAddress());
        holder.clinicImage.setImageResource(clinicList.get(position).getImage());
        holder.declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.approveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView clinicImage;
        TextView clinicName, clinicAddress;
        TextView approveRequest, declineRequest;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            clinicImage = itemView.findViewById(R.id.clinic_image);
            clinicName = itemView.findViewById(R.id.clinic_name);
            clinicAddress = itemView.findViewById(R.id.clinic_address);
            declineRequest = itemView.findViewById(R.id.decline_request);
            approveRequest = itemView.findViewById(R.id.approve_request);
        }
    }
}
