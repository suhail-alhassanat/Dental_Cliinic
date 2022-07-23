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
import com.suhail.dentalcliinic.models.Receptor;
import com.suhail.dentalcliinic.ui.ReceptorDetailsActivity;

import java.util.List;

public class ReceptorRVAdapter extends RecyclerView.Adapter<ReceptorRVAdapter.ReceptorViewHolder> {
    ReceptionistDoctorItemDesignBinding binding;
    List<Receptor> receptors;
    Context context;
    public static final String Receptor_DETAILS_KAY = "receptor_details";

    public ReceptorRVAdapter(List<Receptor> receptors, Context context) {
        this.receptors = receptors;
        this.context = context;


    }

    @NonNull
    @Override
    public ReceptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceptorViewHolder(ReceptionistDoctorItemDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceptorViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(receptors.get(position).getImageUrl()).into(binding.imgRecDocPhotoItem);
        binding.txtRecDocNameItem.setText(receptors.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receptorDetailsIntent = new Intent(context, ReceptorDetailsActivity.class);
                receptorDetailsIntent.putExtra(Receptor_DETAILS_KAY, receptors.get(position));
                receptorDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(receptorDetailsIntent);
                ((Activity)view.getContext()).finish();
            }
        });
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
    public class ReceptorViewHolder extends RecyclerView.ViewHolder {
        public ReceptorViewHolder(@NonNull ReceptionistDoctorItemDesignBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}


