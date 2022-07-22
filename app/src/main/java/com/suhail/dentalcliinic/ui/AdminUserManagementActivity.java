package com.suhail.dentalcliinic.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.suhail.dentalcliinic.adapters.DoctorsRVAdapter;
import com.suhail.dentalcliinic.adapters.ReceptorRVAdapter;
import com.suhail.dentalcliinic.databinding.ActivityAdminUserManagementBinding;
import com.suhail.dentalcliinic.models.Doctor;
import com.suhail.dentalcliinic.models.Receptor;


public class AdminUserManagementActivity extends AppCompatActivity {
ActivityAdminUserManagementBinding binding;

FirebaseFirestore firestore;
//define progress dialog
  ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminUserManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore=FirebaseFirestore.getInstance();

        //initialize progress dialog
        p=new ProgressDialog(AdminUserManagementActivity.this);

        binding.rlRoot.setVisibility(View.GONE);

        //initialize doctors RV
        initDoctorsRV();

        //initialize receptors RV
        initReceptorsRV();



        //move to add doctor activity
        binding.imgAddDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUserManagementActivity.this,AdminAddUsers.class));
            }
        });

        //move to add receptor activity
        binding.imgAddReceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUserManagementActivity.this, AdminAddReceptor.class));
            }
        });
    }

    private void initDoctorsRV() {
        p.setTitle("Loading doctor and receptor data ");
        p.setMessage("pleas wait... ");
        p.setCanceledOnTouchOutside(false);
        p.show();
        firestore.collection("doctors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null&&error==null){
                    binding.rvDoctors.setAdapter(new DoctorsRVAdapter(value.toObjects(Doctor.class), getBaseContext()));
                    binding.rvDoctors.setLayoutManager(new LinearLayoutManager(AdminUserManagementActivity.this, RecyclerView.HORIZONTAL, false));
                    binding.rlRoot.setVisibility(View.VISIBLE);
                    p.dismiss();
                }else {
                    Toast.makeText(AdminUserManagementActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.rlRoot.setVisibility(View.VISIBLE);
                    p.dismiss();
                }

            }

        });
    }
        private void initReceptorsRV(){
            firestore.collection("receptors").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    binding.rvReceptors.setAdapter(new ReceptorRVAdapter(value.toObjects(Receptor.class),getBaseContext()));
                    binding.rvReceptors.setLayoutManager(new LinearLayoutManager(AdminUserManagementActivity.this, RecyclerView.HORIZONTAL,false));
                }
            });
    }

}