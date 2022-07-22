package com.suhail.dentalcliinic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.suhail.dentalcliinic.databinding.ProcessesItemDesignBinding;
import com.suhail.dentalcliinic.models.Process;

import java.util.List;

public class ProcessRVAdapter extends RecyclerView.Adapter<ProcessRVAdapter.ProcessViewHolder> {
ProcessesItemDesignBinding binding;
List<Process> process;
Context context;

    public ProcessRVAdapter(List<Process> process, Context context) {
        this.process = process;
        this.context = context;
    }

    @NonNull
    @Override
    public ProcessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProcessViewHolder(ProcessesItemDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, int position) {
      binding.txtProcessName.setText(process.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return process.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ProcessViewHolder extends RecyclerView.ViewHolder{

        public ProcessViewHolder(@NonNull ProcessesItemDesignBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;

        }
    }

}
