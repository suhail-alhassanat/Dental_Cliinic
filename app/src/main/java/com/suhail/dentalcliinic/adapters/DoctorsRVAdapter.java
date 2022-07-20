package com.suhail.dentalcliinic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.suhail.dentalcliinic.databinding.ReceptionistDoctorItemDesignBinding;
import com.suhail.dentalcliinic.models.Doctor;

import java.util.ArrayList;

public class DoctorsRVAdapter extends RecyclerView.Adapter<DoctorsRVAdapter.DoctorsViewHolder> {
    ReceptionistDoctorItemDesignBinding binding;
    ArrayList<Doctor> doctors;

    public DoctorsRVAdapter(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorsViewHolder(ReceptionistDoctorItemDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsViewHolder holder, int position) {
        Picasso.get().load(doctors.get(position).getImageUrl()).into(binding.imgRecDocPhotoItem);
        binding.txtRecDocNameItem.setText(doctors.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return doctors.size();
    }


    //implement View Holder inner class
    public class DoctorsViewHolder extends RecyclerView.ViewHolder{
        public DoctorsViewHolder(@NonNull ReceptionistDoctorItemDesignBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
