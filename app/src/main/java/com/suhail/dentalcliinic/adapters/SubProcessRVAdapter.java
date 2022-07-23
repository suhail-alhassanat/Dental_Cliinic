package com.suhail.dentalcliinic.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suhail.dentalcliinic.databinding.ReceptionistDoctorItemDesignBinding;
import com.suhail.dentalcliinic.databinding.SubProcessItemDesignBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.SubProcess;

import java.util.List;

public class SubProcessRVAdapter extends RecyclerView.Adapter<SubProcessRVAdapter.SubProcessViewHolder> {
SubProcessItemDesignBinding binding;
List<SubProcess> subProcesses;
FirebaseFirestore firestore;
FirebaseAuth auth;
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
    public void onBindViewHolder(@NonNull SubProcessViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //initialize firestore objects
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        binding.txtSubProcessName.setText(subProcesses.get(position).getName());
        binding.txtSubProcessType.setText(subProcesses.get(position).getType());
        binding.txtSubProcessPrice.setText(String.valueOf(subProcesses.get(position).getPrice()));

        //to delete item
        binding.imgClearIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (auth.getCurrentUser()!=null){
                  firestore.collection(Constants.SUB_PROCESSES_COLLECTION_NAME).document(subProcesses.get(position).getName()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){
                              Toast.makeText(context, "sub process deleted successfully", Toast.LENGTH_SHORT).show();
                          }else
                              Toast.makeText(context, "sub process deleted failed ", Toast.LENGTH_SHORT).show();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  });
              }else {
                  Toast.makeText(context, "must be logged in", Toast.LENGTH_SHORT).show();
              }
            }
        });
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
