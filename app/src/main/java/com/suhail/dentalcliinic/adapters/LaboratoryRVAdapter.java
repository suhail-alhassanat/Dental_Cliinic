package com.suhail.dentalcliinic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.LaboratoriesItemDesignBinding;
import com.suhail.dentalcliinic.models.Laboratory;

import java.util.List;

public class LaboratoryRVAdapter extends RecyclerView.Adapter<LaboratoryRVAdapter.LaboratoryViewHolder> {
    LaboratoriesItemDesignBinding binding;
    List<Laboratory> laboratories;

    public LaboratoryRVAdapter(List<Laboratory> laboratories) {
        this.laboratories = laboratories;
    }

    @NonNull
    @Override
    public LaboratoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LaboratoryViewHolder(LaboratoriesItemDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LaboratoryViewHolder holder, int position) {
            binding.txtLaboratoryName.setText(laboratories.get(position).getName());
            binding.txtAddress.setText(laboratories.get(position).getAddress());
            binding.txtPhoneNo.setText(laboratories.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return laboratories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class LaboratoryViewHolder extends RecyclerView.ViewHolder{

        public LaboratoryViewHolder(@NonNull LaboratoriesItemDesignBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
