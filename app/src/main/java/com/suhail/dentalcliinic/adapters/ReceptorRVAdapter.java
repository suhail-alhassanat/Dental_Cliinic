package com.suhail.dentalcliinic.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.suhail.dentalcliinic.databinding.ReceptionistDoctorItemDesignBinding;
import com.suhail.dentalcliinic.models.Receptor;

import java.util.List;

public class ReceptorRVAdapter extends RecyclerView.Adapter<ReceptorRVAdapter.ReceptorViewHolder> {
    ReceptionistDoctorItemDesignBinding binding;
    List<Receptor> receptors;
    Context context;
    public static final String DOCTOR_DETAILS_KAY="doctor_details";

    public ReceptorRVAdapter(List<Receptor> receptors, Context context) {
        this.receptors = receptors;
        this.context=context;
    }

    @NonNull
    @Override
    public ReceptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceptorViewHolder(ReceptionistDoctorItemDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceptorViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(receptors.get(position).getImageUrl()).into(binding.imgRecDocPhotoItem);
        binding.txtRecDocNameItem.setText(receptors.get(position).getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent doctorDetailsIntent=new Intent(context, DoctorDetailsActivity.class);
//                doctorDetailsIntent.putExtra(DOCTOR_DETAILS_KAY,receptors.get(position));
//                doctorDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(doctorDetailsIntent);
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return receptors.size();
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
    public class ReceptorViewHolder extends RecyclerView.ViewHolder{
        public ReceptorViewHolder(@NonNull ReceptionistDoctorItemDesignBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
