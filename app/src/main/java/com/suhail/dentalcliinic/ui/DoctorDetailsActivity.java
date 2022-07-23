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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.adapters.DoctorsRVAdapter;
import com.suhail.dentalcliinic.databinding.ActivityDoctorDetailsBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Doctor;

public class DoctorDetailsActivity extends AppCompatActivity {
ActivityDoctorDetailsBinding binding;

//constants variable
public static final String UPDATE_DOCTOR_KEY="updateDoctor";
//declare firestore
FirebaseFirestore firestore;
boolean isDeleted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDoctorDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//sent data and go to update activity
        binding.imgEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateDoctorIntent =new Intent(DoctorDetailsActivity.this,UpdateDoctorActivity.class);
                updateDoctorIntent.putExtra(UPDATE_DOCTOR_KEY,getIntent().getSerializableExtra(DoctorsRVAdapter.DOCTOR_DETAILS_KAY));
                startActivity(updateDoctorIntent);
                finish();
            }
        });


//initialize  firestore
        firestore=FirebaseFirestore.getInstance();

//load  the doctor data and put them in all fields
        Doctor  doctorData = (Doctor) getIntent().getSerializableExtra(DoctorsRVAdapter.DOCTOR_DETAILS_KAY);
        if (doctorData != null) {
            if (!doctorData.getImageUrl().isEmpty()) {
                Picasso.get().load(doctorData.getImageUrl()).into(binding.imgDoctorPhoto);
            }
            if (!doctorData.getPhone().isEmpty()) {
                binding.txtPhone.setText(doctorData.getPhone());
            }

            if (!doctorData.getName().isEmpty()) {
                binding.txtDoctorName.setText(doctorData.getName());
            }

            if (!String.valueOf(doctorData.getIdentityNumber()).isEmpty()) {
                binding.txtIdentity.setText(String.valueOf(doctorData.getIdentityNumber()));
            }
            if (!doctorData.getAddress().isEmpty()) {
                binding.txtAddress.setText(doctorData.getAddress());
            }

            if (!doctorData.getMembershipNumber().isEmpty()) {
                binding.txtMembershipNo.setText(doctorData.getMembershipNumber());
            }
            if (!doctorData.getDepartment().isEmpty()) {
                binding.txtDepartment.setText(doctorData.getDepartment());
            }

            if (!doctorData.getWorkDays().isEmpty()) {
               binding.spWorkDay.setAdapter(new ArrayAdapter(DoctorDetailsActivity.this, android.R.layout.simple_list_item_1,doctorData.getWorkDays()));
            }
            if (!doctorData.getWorkHours().isEmpty()) {
                binding.txtWorkHour.setText(doctorData.getWorkHours());
            }
            if (!doctorData.getWorkStartDate().isEmpty()) {
                binding.txtStartOfWork.setText(doctorData.getWorkStartDate());
            }

            if (!doctorData.getWorkEndDate().isEmpty()) {
                binding.txtEndOfWork.setText(doctorData.getWorkEndDate());
            }
            if (!doctorData.getGender().isEmpty()) {
                binding.txtGender.setText(doctorData.getGender());
            }
            if (!doctorData.getEmail().isEmpty()) {
                binding.txtEmail.setText(doctorData.getEmail());
            }
            if (!String.valueOf(doctorData.getSalary()).isEmpty()){
                binding.txtSalary.setText(String.valueOf(doctorData.getSalary()));
            }
        }

//delete doctors
        binding.imgClearIcon.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View view) {
              if (doctorData!=null){
                  delDoctor(doctorData.getEmail());
              }
                   finish();
            }
           });


    }



//methode for delete doctors
    public void delDoctor(String docId){

        firestore.collection(Constants.DOCTORS_COLLECTION_NAME).document(docId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                  isDeleted=true;
                    Toast.makeText(DoctorDetailsActivity.this, "delete successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DoctorDetailsActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}