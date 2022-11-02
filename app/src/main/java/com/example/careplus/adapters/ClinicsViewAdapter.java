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
import com.example.careplus.localStorage.Appointments;
import com.example.careplus.localStorage.ClinicsViewData;
import com.example.careplus.localStorage.ScheduleViewCardData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class ClinicsViewAdapter extends RecyclerView.Adapter<ClinicsViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ClinicsViewData> clinicsList;
    FirebaseFirestore db;
    public ClinicsViewAdapter(Context context, ArrayList<ClinicsViewData> clinicsList) {
        this.context = context;
        this.clinicsList = clinicsList;
    }

    @NonNull
    @Override
    public ClinicsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.clinics_view_row, parent, false);
        return new ClinicsViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClinicsViewAdapter.MyViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        holder.name.setText(clinicsList.get(position).getName());
        holder.city.setText(clinicsList.get(position).getCity());
        holder.image.setImageResource(clinicsList.get(position).getImage());
        holder.booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bookingDates = new AlertDialog.Builder(view.getContext());
                AlertDialog.Builder bookingHours = new AlertDialog.Builder(view.getContext());
                bookingDates.setTitle("Select Date");
                bookingHours.setTitle("Select Time");
                LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<String> days = new ArrayList<>();
                ArrayList<String> time = new ArrayList<>();
                for (int i =0 ; i < 7 ; i++) {
                    days.add(nextMonday.getDayOfWeek().toString());
                    dates.add(nextMonday + " " + nextMonday.getDayOfWeek().toString());
                    nextMonday = nextMonday.plusDays(1);
                }
                bookingDates.setItems(dates.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        db.collection("Clinics").whereEqualTo("email", clinicsList.get(holder.getAbsoluteAdapterPosition()).getEmail())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        for(QueryDocumentSnapshot doc : task.getResult()) {
                                            if(days.get(i) == "Sunday" || days.get(i) == "Saturday") {
                                                for (int j = Integer.parseInt(doc.getData().get("weekendStartHour").toString()); j <= Integer.parseInt(doc.getData().get("weekendEndHour").toString()); j++) {
                                                    time.add(j + ":" + doc.getData().get("weekendStartMin"));
                                                }
                                            } else {
                                                for (int j = Integer.parseInt(doc.getData().get("weekdayStartHour").toString()); j <= Integer.parseInt(doc.getData().get("weekdayEndHour").toString()); j++) {
                                                    time.add(j + ":" + doc.getData().get("weekdayStartMin"));
                                                }
                                            }
                                        }
                                    }
                                    bookingHours.setItems(time.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();

                                            Appointments newAppointmentRequest = new Appointments(clinicsList.get(holder.getAbsoluteAdapterPosition()).getName(), days.get(i), dates.get(i), user.getEmail() );
                                            db.collection("Appointments").document(clinicsList.get(holder.getAbsoluteAdapterPosition()).getEmail())
                                                            .set(newAppointmentRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(view.getContext(), "Booking Request Success", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(view.getContext(), "Booking Request Failure", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                            holder.booking.setText("Pending");
                                        }
                                    });
                                    bookingHours.show();
                                }
                            });
                    }
                });
                bookingDates.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clinicsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, city;
        TextView doctorsList, booking;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clinic_name);
            city = itemView.findViewById(R.id.clinic_address);
            image = itemView.findViewById(R.id.clinic_image);
            doctorsList = itemView.findViewById(R.id.check_doctors);
            booking = itemView.findViewById(R.id.book_appointment);
        }
    }
}
