package com.suhail.dentalcliinic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.adapters.DoctorsRVAdapter;
import com.suhail.dentalcliinic.databinding.ActivityAdminAddUsersBinding;
import com.suhail.dentalcliinic.databinding.ActivityAdminUserManagementBinding;
import com.suhail.dentalcliinic.models.Doctor;

import java.util.ArrayList;
import java.util.HashMap;


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

        //move to add doctor activity
        binding.imgAddDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminUserManagementActivity.this,AdminAddUsers.class));
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

    private void initDoctorsRV(){
        firestore.collection("doctors").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<HashMap<Object,Object>> maps=new ArrayList<>();
                    for(QueryDocumentSnapshot document:task.getResult()){
                        maps.add(new HashMap<>(document.getData()));
                    }
                    ArrayList<Doctor> doctors=new ArrayList<>();
                    for(HashMap<Object,Object> map:maps){
                        Doctor doctor=new Doctor();
                        doctor.setName(map.get("name").toString());
                        doctor.setEmail(map.get("email").toString());
                        doctor.setPhone(map.get("phone").toString());
                        doctor.setAddress(map.get("address").toString());
                        doctor.setIdentityNumber(Integer.parseInt(map.get("identityNumber").toString()));
                        doctor.setMembershipNumber(map.get("membershipNumber").toString());
                        doctor.setDepartment(map.get("department").toString());
                        doctor.setGender(map.get("gender").toString());
                        doctor.setWorkDays((ArrayList<String>) map.get("workDays"));
                        doctor.setWorkHours(map.get("workHours").toString());
                        doctor.setHiringDate(map.get("workStartDate").toString());
                        doctor.setEndDate(map.get("workEndDate").toString());
                        doctor.setImageUrl(map.get("imageUrl").toString());

                        doctors.add(doctor);

                        DoctorsRVAdapter adapter=new DoctorsRVAdapter(doctors);
                        binding.rvDoctors.setAdapter(adapter);
                        binding.rvDoctors.setLayoutManager(new LinearLayoutManager(AdminUserManagementActivity.this, RecyclerView.HORIZONTAL,false));
                    }
                }
                else
                    Toast.makeText(AdminUserManagementActivity.this, "error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}