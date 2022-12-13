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
import com.example.careplus.doctor.home.NewRequestFragment;
import com.example.careplus.localStorage.DoctorViewCardData;
import com.example.careplus.localStorage.NewAppointmentRequest;
import com.example.careplus.localStorage.RequestViewCardData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewRequestDoctorAdapter extends RecyclerView.Adapter<NewRequestDoctorAdapter.MyViewHolder> {
    Context context;
    ArrayList<RequestViewCardData> clinicList;
    ArrayList<DoctorViewCardData> doctorList;
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
                db.collection("Clinics").document(clinicList.get(holder.getAbsoluteAdapterPosition()).getID())
                    .update("pendingRequests", FieldValue.arrayRemove(user.getEmail())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                db.collection("Doctors").whereEqualTo("email", user.getEmail()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                            if(task2.isSuccessful()) {
                                                for(QueryDocumentSnapshot doc : task2.getResult()) {
                                                    db.collection("Doctors").document(doc.getId())
                                                        .update("requests",  FieldValue.arrayRemove(clinicList.get(holder.getAbsoluteAdapterPosition()).getEmail())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()) {
                                                                    holder.declineRequest.setText("Declined");
                                                                    holder.declineRequest.setBackgroundResource(R.drawable.rounded_delete_account);
                                                                    if(clinicList.size() > 0) {
                                                                        clinicList.remove(holder.getAbsoluteAdapterPosition());
                                                                    }
                                                                    NewRequestDoctorAdapter adapter = new NewRequestDoctorAdapter(view.getContext(),clinicList);
                                                                    adapter.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                                                                }
                                                            }
                                                        });
                                                }
                                            }
                                        }
                                    });
                            }
                        }
                    });
            }
        });
        holder.approveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Clinics").document(clinicList.get(holder.getAbsoluteAdapterPosition()).getID())
                    .update("pendingRequests", FieldValue.arrayRemove(user.getEmail())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                db.collection("Doctors").whereEqualTo("email", user.getEmail())
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                            if(task2.isSuccessful()) {
                                                for(QueryDocumentSnapshot doc : task2.getResult()) {
                                                    db.collection("Doctors").document(doc.getId())
                                                        .update("requests",  FieldValue.arrayRemove(clinicList.get(holder.getAbsoluteAdapterPosition()).getEmail())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                db.collection("Doctors").document(doc.getId())
                                                                        .update("approvedList", FieldValue.arrayUnion(clinicList.get(holder.getAbsoluteAdapterPosition()).getEmail()));
                                                                holder.approveRequest.setText("Approved");
                                                                db.collection("Clinics").document(clinicList.get(holder.getAbsoluteAdapterPosition()).getID())
                                                                        .update("approvedList", FieldValue.arrayUnion(doc.getData().get("email").toString()))
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                clinicList.remove(holder.getAbsoluteAdapterPosition());
                                                                                NewRequestDoctorAdapter adapter = new NewRequestDoctorAdapter(view.getContext(),clinicList);
                                                                                adapter.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                                                                            }
                                                                        });
                                                            }
                                                        });
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
            clinicAddress = itemView.findViewById(R.id.schedule_FN);
            declineRequest = itemView.findViewById(R.id.decline_request);
            approveRequest = itemView.findViewById(R.id.approve_request);
        }
    }
}
