package com.suhail.dentalcliinic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityUpdateDoctorBinding;

public class UpdateDoctorActivity extends AppCompatActivity {
ActivityUpdateDoctorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}