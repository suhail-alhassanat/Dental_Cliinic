package com.suhail.dentalcliinic.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.adapters.SubProcessRVAdapter;
import com.suhail.dentalcliinic.databinding.ActivityManageSubProcessesBinding;
import com.suhail.dentalcliinic.databinding.ActivityManageSubProcessesBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.SubProcess;

public class ManageSubProcessesActivity extends AppCompatActivity {
    ActivityManageSubProcessesBinding binding;
    //define firebase features object
    FirebaseFirestore firestore;

    //define progress dialog
    ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityManageSubProcessesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize firebase features object
        firestore=FirebaseFirestore.getInstance();

        //set visibility gone to root layout
        binding.rlRoot.setVisibility(View.GONE);

        //initialize progress dialog
        p=new ProgressDialog(ManageSubProcessesActivity.this);

        //push sub process data into recyclerView
        initSubProcessRv();

        //action on floating button to go to add process activity
        binding.fbAddSubProcesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageSubProcessesActivity.this,AddSubProcessActivity.class));
            }
        });


    }

    private void initSubProcessRv(){

        p.setTitle("Loading Sub Processes");
        p.setMessage("please wait... ");
        p.setCanceledOnTouchOutside(false);
        p.show();
        firestore.collection(Constants.SUB_PROCESSES_COLLECTION_NAME).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if (value!=null&&error==null){
                binding.rvDoctors.setAdapter(new SubProcessRVAdapter(value.toObjects(SubProcess.class),ManageSubProcessesActivity.this));
                binding.rvDoctors.setLayoutManager(new LinearLayoutManager(ManageSubProcessesActivity.this));
            }else{
                Toast.makeText(ManageSubProcessesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
                binding.rlRoot.setVisibility(View.VISIBLE);
                p.dismiss();
            }
        });
    }
}