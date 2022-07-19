package com.suhail.dentalcliinic.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityAdminAddUsersBinding;

public class AdminAddUsers extends AppCompatActivity {
ActivityAdminAddUsersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminAddUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}