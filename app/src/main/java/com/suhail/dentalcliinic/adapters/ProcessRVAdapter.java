package com.suhail.dentalcliinic.adapters;

import static com.suhail.dentalcliinic.helper.FirebaseOperations.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.suhail.dentalcliinic.databinding.ProcessesItemDesignBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Process;

import java.util.List;

public class ProcessRVAdapter extends RecyclerView.Adapter<ProcessRVAdapter.ProcessViewHolder> {
ProcessesItemDesignBinding binding;
List<Process> process;
Context context;
FirebaseFirestore firestore;

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
    public void onBindViewHolder(@NonNull ProcessViewHolder holder, @SuppressLint("RecyclerView") int position) {
        firestore=FirebaseFirestore.getInstance();
      binding.txtProcessName.setText(process.get(position).getName());

      binding.imgClearIcon.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              firestore.collection(Constants.PROCESSES_COLLECTION_NAME).document(process.get(position).getName()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()){
                         firestore.collection(Constants.SUB_PROCESSES_COLLECTION_NAME).whereEqualTo("type",process.get(position).getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                             @Override
                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                 if (task.isSuccessful()){
                                     for (DocumentSnapshot document : task.getResult()) {
                                         firestore.collection(Constants.SUB_PROCESSES_COLLECTION_NAME).document(document.getId()).delete();
                                         Toast.makeText(context, "process and sub process delete successfully ", Toast.LENGTH_SHORT).show();
                                         Log.d(TAG, "deleted");
                                     }
                                 }else {
                                     Log.d(TAG, "Error getting documents: ", task.getException());
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
