package com.suhail.dentalcliinic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminUserManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore=FirebaseFirestore.getInstance();

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

    @Override
    protected void onRestart() {
        super.onRestart();
        initDoctorsRV();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDoctorsRV();
    }


    private void initDoctorsRV() {
        firestore.collection("doctors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.rvDoctors.setAdapter(new DoctorsRVAdapter(value.toObjects(Doctor.class), getBaseContext()));
                binding.rvDoctors.setLayoutManager(new LinearLayoutManager(AdminUserManagementActivity.this, RecyclerView.HORIZONTAL, false));
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









//    private void initDoctorsRV(){
//        firestore.collection("doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    ArrayList<Doctor> doctors=new ArrayList<>();
//                    ArrayList<HashMap<Object,Object>> maps=new ArrayList<>();
//                    for(QueryDocumentSnapshot document:task.getResult()){
//                        Doctor doctor=document.toObject(Doctor.class);
//                        doctors.add(doctor);
//                    }
//                        DoctorsRVAdapter adapter=new DoctorsRVAdapter(doctors,AdminUserManagementActivity.this);
//                        binding.rvDoctors.setAdapter(adapter);
//                        binding.rvDoctors.setLayoutManager(new LinearLayoutManager(AdminUserManagementActivity.this, RecyclerView.HORIZONTAL,false));
//                }
//                else
//                    Toast.makeText(AdminUserManagementActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}