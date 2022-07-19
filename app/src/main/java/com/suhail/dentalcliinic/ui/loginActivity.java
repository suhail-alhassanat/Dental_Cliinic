package com.suhail.dentalcliinic.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityLoginBinding;

public class loginActivity extends AppCompatActivity {
ActivityLoginBinding binding;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();

        //check if user already logged in
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(loginActivity.this,AdminControlActivity.class));
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get email & password from editTexts
                String email=binding.edtLoginEmail.getText().toString();
                String password=binding.edtLoginPass.getText().toString();

                //check if any of fields is empty
                if(email.equals("") || password.equals("")){
                    Toast.makeText(loginActivity.this, "please fill all fields first", Toast.LENGTH_SHORT).show();
                }
                else
                    //call login method
                    login(email,password);
            }
        });
    }

    //Login function using FirebaseAuth (Email & password)
    private void login(String email,String password){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(loginActivity.this, "login succefull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(loginActivity.this,AdminControlActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}