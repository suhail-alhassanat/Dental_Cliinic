package com.suhail.dentalcliinic.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.adapters.DoctorsRVAdapter;
import com.suhail.dentalcliinic.adapters.ReceptorRVAdapter;
import com.suhail.dentalcliinic.databinding.ActivityReceptorDetailsBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Doctor;
import com.suhail.dentalcliinic.models.Receptor;

public class ReceptorDetailsActivity extends AppCompatActivity {
    ActivityReceptorDetailsBinding binding;
    //constants variable
    public static final String UPDATE_RECEPTOR_KEY="updateReceptor";
    //declare firestore
    FirebaseFirestore firestore;

    //receptor object
    Receptor receptorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReceptorDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sent data and go to update activity
        binding.imgEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateReceptorIntent =new Intent(ReceptorDetailsActivity.this,UpdateReceptorActivity.class);
                updateReceptorIntent.putExtra(UPDATE_RECEPTOR_KEY,getIntent().getSerializableExtra(ReceptorRVAdapter.Receptor_DETAILS_KAY));
                startActivity(updateReceptorIntent);
                finish();
            }
        });


        //calling receptor details method
        recDetails();

         //initialize  firestore
        firestore=FirebaseFirestore.getInstance();



        binding.imgClearIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (receptorData.getEmail()!=null){
                    delRec(receptorData.getEmail());
                }
                startActivity(new Intent(ReceptorDetailsActivity.this,AdminUserManagementActivity.class));
                finish();
            }
        });


    }


    //methode for delete doctors
    public void delRec(String docId){

        firestore.collection(Constants.DOCTORS_COLLECTION_NAME).document(docId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(ReceptorDetailsActivity.this, "delete successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ReceptorDetailsActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReceptorDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //method for load  the receptor data and put them in all fields
    public void recDetails(){
         receptorData = (Receptor) getIntent().getSerializableExtra(ReceptorRVAdapter.Receptor_DETAILS_KAY);
        if (receptorData != null) {
            if (!receptorData.getImageUrl().isEmpty()) {
                Picasso.get().load(receptorData.getImageUrl()).into(binding.imgDoctorPhoto);
            }
            if (!receptorData.getPhone().isEmpty()) {
                binding.txtPhone.setText(receptorData.getPhone());
            }

            if (!receptorData.getName().isEmpty()) {
                binding.txtReceptorName.setText(receptorData.getName());
            }

            if (!String.valueOf(receptorData.getIdentityNumber()).isEmpty()) {
                binding.txtIdentity.setText(String.valueOf(receptorData.getIdentityNumber()));
            }
            if (!receptorData.getAddress().isEmpty()) {
                binding.txtAddress.setText(receptorData.getAddress());
            }

            if (!receptorData.getWorkDays().isEmpty()) {
                binding.spWorkDay.setAdapter(new ArrayAdapter(ReceptorDetailsActivity.this, android.R.layout.simple_list_item_1,receptorData.getWorkDays()));
            }
            if (!receptorData.getWorkHours().isEmpty()) {
                binding.txtWorkHour.setText(receptorData.getWorkHours());
            }
            if (!receptorData.getWorkStartDate().isEmpty()) {
                binding.txtStartOfWork.setText(receptorData.getWorkStartDate());
            }

            if (!receptorData.getWorkEndDate().isEmpty()) {
                binding.txtEndOfWork.setText(receptorData.getWorkEndDate());
            }
            if (!receptorData.getGender().isEmpty()) {
                binding.txtGender.setText(receptorData.getGender());
            }
            if (!receptorData.getEmail().isEmpty()) {
                binding.txtEmail.setText(receptorData.getEmail());
            }
            if (!String.valueOf(receptorData.getSalary()).isEmpty()){
                binding.txtSalary.setText(String.valueOf(receptorData.getSalary()));
            }
        }
    }
}