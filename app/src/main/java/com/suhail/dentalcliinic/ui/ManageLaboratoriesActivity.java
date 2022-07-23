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
import com.suhail.dentalcliinic.databinding.ActivityManageLaboratorisBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Laboratory;

public class ManageLaboratoriesActivity extends AppCompatActivity {
ActivityManageLaboratorisBinding binding;

    //define firebase features objects
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    //define ProgressDialog
    ProgressDialog p ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityManageLaboratorisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize progressDialog
        p = new ProgressDialog(ManageLaboratoriesActivity.this);

        //initialize firebase
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        //initialize recycler
        initLaboratoriesRv();

        //implement add button code
        binding.fbAddLaboratories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageLaboratoriesActivity.this,AddLaboratoriesActivity.class));
            }
        });
    }

    private void initLaboratoriesRv(){
        firestore.collection(Constants.LABORATORY_COLLECTION_NAME).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!=null && error == null){
                binding.rvLaboratories.setAdapter(new LaboratoryRVAdapter(value.toObjects(Laboratory.class)));
                binding.rvLaboratories.setLayoutManager(new LinearLayoutManager(ManageLaboratoriesActivity.this, RecyclerView.HORIZONTAL,false));
            }
            else
            {
                Toast.makeText(ManageLaboratoriesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                p.dismiss();
            }
            }
        });
    }
}