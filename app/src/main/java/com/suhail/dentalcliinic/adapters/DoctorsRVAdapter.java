package com.suhail.dentalcliinic.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.suhail.dentalcliinic.databinding.ReceptionistDoctorItemDesignBinding;
import com.suhail.dentalcliinic.models.Doctor;
import com.suhail.dentalcliinic.ui.DoctorDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class DoctorsRVAdapter extends RecyclerView.Adapter<DoctorsRVAdapter.DoctorsViewHolder> {
    ReceptionistDoctorItemDesignBinding binding;
    List<Doctor> doctors;
    Context context;
    public static final String DOCTOR_DETAILS_KAY="doctor_details";

    public DoctorsRVAdapter(List<Doctor> doctors,Context context) {
        this.doctors = doctors;
        this.context=context;
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorsViewHolder(ReceptionistDoctorItemDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(doctors.get(position).getImageUrl()).into(binding.imgRecDocPhotoItem);
        binding.txtRecDocNameItem.setText(doctors.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent doctorDetailsIntent=new Intent(context, DoctorDetailsActivity.class);
                doctorDetailsIntent.putExtra(DOCTOR_DETAILS_KAY,doctors.get(position));
                doctorDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(doctorDetailsIntent);
                ((Activity)view.getContext()).finish();
            }
        });

    }


    @Override
    public int getItemCount() {
        return doctors.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //implement View Holder inner class
    public class DoctorsViewHolder extends RecyclerView.ViewHolder{
        public DoctorsViewHolder(@NonNull ReceptionistDoctorItemDesignBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
