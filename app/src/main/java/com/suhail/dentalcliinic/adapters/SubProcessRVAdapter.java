package com.suhail.dentalcliinic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suhail.dentalcliinic.databinding.ReceptionistDoctorItemDesignBinding;
import com.suhail.dentalcliinic.databinding.SubProcessItemDesignBinding;
import com.suhail.dentalcliinic.models.SubProcess;

import java.util.List;

public class SubProcessRVAdapter extends RecyclerView.Adapter<SubProcessRVAdapter.SubProcessViewHolder> {
SubProcessItemDesignBinding binding;
List<SubProcess> subProcesses;
Context context;

    public SubProcessRVAdapter( List<SubProcess> subProcesses, Context context) {
        this.subProcesses = subProcesses;
        this.context = context;
    }

    @NonNull
    @Override
    public SubProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubProcessViewHolder(SubProcessItemDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubProcessViewHolder holder, int position) {
        binding.txtSubProcessName.setText(subProcesses.get(position).getName());
        binding.txtSubProcessType.setText(subProcesses.get(position).getType());
        binding.txtSubProcessPrice.setText(String.valueOf(subProcesses.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return subProcesses.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class SubProcessViewHolder extends RecyclerView.ViewHolder{

        public SubProcessViewHolder(@NonNull SubProcessItemDesignBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
