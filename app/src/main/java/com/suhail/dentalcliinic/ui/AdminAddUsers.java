package com.suhail.dentalcliinic.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityAdminAddUsersBinding;

import java.util.ArrayList;
import java.util.Date;

public class AdminAddUsers extends AppCompatActivity {
ActivityAdminAddUsersBinding binding;
FirebaseFirestore firestore;
FirebaseAuth auth;
FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminAddUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
    public boolean registerNewDoctor(){
        if(auth.getCurrentUser() != null){
            String name = binding.edtUserName.getText().toString();
            String email = binding.edtEmail.getText().toString();
            String phone = binding.edtPhoneNo.getText().toString();
            String address = binding.
            int identityNumber;
            String membershipNumber;
            String specialization;
            String imageUrl;
            String department;
            ArrayList<String> workDays;
            ArrayList<String> workHours;
            Date hiringDate;
            Date endDate;
        }
    }
}