package com.suhail.dentalcliinic.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.adapters.LaboratoryRVAdapter;
import com.suhail.dentalcliinic.adapters.ProcessRVAdapter;
import com.suhail.dentalcliinic.databinding.ActivityManagePrecessesBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Laboratory;
import com.suhail.dentalcliinic.models.Process;

public class ManagePrecessesActivity extends AppCompatActivity {
ActivityManagePrecessesBinding binding;

    //define firebase features objects
    private FirebaseFirestore firestore;


    //define progress dialog
    ProgressDialog p ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityManagePrecessesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //gone root layout
          binding.rlRoot.setVisibility(View.GONE);

        //initialize firebase features objects
        firestore=FirebaseFirestore.getInstance();

        //initialize progress dialog
        p=new ProgressDialog(ManagePrecessesActivity.this);

        //initialize processRv
        initializeProcessRv();


        //action on add floating action btn to go to AddProcessActivity
        binding.fbAddProcesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagePrecessesActivity.this,AddProcessesActivity.class));
            }
        });



    }

    private void initializeProcessRv(){
        p.setTitle("Loading processes");
        p.setMessage("please wait... ");
        p.setCanceledOnTouchOutside(false);
        p.show();
        firestore.collection(Constants.PROCESSES_COLLECTION_NAME).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null&&error==null){
                    binding.rvProcesses.setAdapter(new ProcessRVAdapter(value.toObjects(Process.class),ManagePrecessesActivity.this));
                    binding.rvProcesses.setLayoutManager(new LinearLayoutManager(ManagePrecessesActivity.this));
                    binding.rlRoot.setVisibility(View.VISIBLE);
                    p.dismiss();
                }else {
                    Toast.makeText(ManagePrecessesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.rlRoot.setVisibility(View.VISIBLE);
                    p.dismiss();
                }
            }
        });
    }
}