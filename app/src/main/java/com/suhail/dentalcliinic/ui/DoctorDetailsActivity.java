package com.suhail.dentalcliinic.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.suhail.dentalcliinic.models.Doctor;

public class DoctorDetailsActivity extends AppCompatActivity {
ActivityDoctorDetailsBinding binding;
//declare firestore
FirebaseFirestore firestore;
boolean isDeleted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDoctorDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize  firestore
        firestore=FirebaseFirestore.getInstance();

//load  the doctor data put them in all fields
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

        }

//delete doctors





binding.imgClearIcon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
  delDoctor(doctorData.getEmail());
  finish();
    }
});
    }



//methode for delete doctors
    public void delDoctor(String docId){

        firestore.collection("doctors").document(docId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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