package com.suhail.dentalcliinic.ui;

import static com.suhail.dentalcliinic.helper.FirebaseOperations.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityUpdateDoctorBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.helper.FirebaseOperations;
import com.suhail.dentalcliinic.models.Doctor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateDoctorActivity extends AppCompatActivity {
ActivityUpdateDoctorBinding binding;
    //define firebase features objects
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth auth;

    //define doctor object
    Doctor doctor;

    //define doctor data object
    Doctor doctorData;

    //profile image url
    private String doctorProfileImageUrl;
    private Uri uri;
    boolean imageChanged;

    //calendar
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize firebase features objects
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();

        //get doctor data from DoctorDetailActivity and set it into all failed
        showDoctorDetails();

        //get  start of work date from User
        DatePickerDialog.OnDateSetListener startOfWorkDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                binding.edtStartOfWork.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.getTime()));
            }
        };

        binding.edtStartOfWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateDoctorActivity.this, startOfWorkDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //get  end of work date from User
        DatePickerDialog.OnDateSetListener endOfWorkDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                binding.edtEndOfWork.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.getTime()));
            }
        };

        binding.edtEndOfWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateDoctorActivity.this, endOfWorkDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //load profile image from studio
        binding.imgDoctorPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcher.launch(intent);
            }
        });


binding.btnSave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        updateDoctor();
    }
});

    }


    private void showDoctorDetails(){
         doctorData= (Doctor) getIntent().getSerializableExtra(DoctorDetailsActivity.UPDATE_DOCTOR_KEY);
        if (doctorData!=null){

            if (!doctorData.getImageUrl().isEmpty()) {
                Picasso.get().load(doctorData.getImageUrl()).into(binding.imgDoctorPhoto);
            }
            if (!doctorData.getPhone().isEmpty()) {
                binding.edtPhoneNo.setText(doctorData.getPhone());
            }

            if (!doctorData.getName().isEmpty()) {
                binding.txtDoctorName.setText(doctorData.getName());
            }

            if (!doctorData.getAddress().isEmpty()) {
                binding.edtAddress.setText(doctorData.getAddress());
            }

            if (!doctorData.getDepartment().isEmpty()) {
                binding.rbDep1.setChecked(doctorData.getDepartment().equals("dept1"));
                binding.rbDep2.setChecked(doctorData.getDepartment().equals("dept2"));
                binding.rbDep3.setChecked(doctorData.getDepartment().equals("dept3"));
                binding.rbDep4.setChecked(doctorData.getDepartment().equals("dept4"));
            }

            if (!doctorData.getWorkDays().isEmpty()) {
                binding.chSaturday.setChecked(doctorData.getWorkDays().contains("saturday"));
                binding.chSunday.setChecked(doctorData.getWorkDays().contains("sunday"));
                binding.chMonday.setChecked(doctorData.getWorkDays().contains("monday"));
                binding.chTuesday.setChecked(doctorData.getWorkDays().contains("Tuesday"));
                binding.chWednesday.setChecked(doctorData.getWorkDays().contains("Wednesday"));
                binding.chThursday.setChecked(doctorData.getWorkDays().contains("Thursday"));
            }

            if (!doctorData.getWorkHours().isEmpty()) {
                binding.rbWorkHourMorning.setChecked(doctorData.getWorkHours().equals("Morning 9:00-2:00 Am"));
                binding.rbWorkHourEvening.setChecked(doctorData.getWorkHours().equals("Morning 3:00-8:00 pm"));
            }

            if (!doctorData.getWorkStartDate().isEmpty()) {
                binding.edtStartOfWork.setText(doctorData.getWorkStartDate());
            }

            if (!doctorData.getWorkEndDate().isEmpty()) {
                binding.edtEndOfWork.setText(doctorData.getWorkEndDate());
            }

            if (!String.valueOf(doctorData.getSalary()).isEmpty()){
                binding.edtSalary.setText(String.valueOf(doctorData.getSalary()));
            }
        }

    }

    ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                uri = result.getData().getData();
                if(uri!=null) {
                    binding.imgDoctorPhoto.setImageURI(uri);
                    imageChanged=true;
                }
            }
        }
    });


    private void updateDoctor(){
       if (auth.getCurrentUser()!=null) {
           doctor = new Doctor();
           String name = binding.txtDoctorName.getText().toString();
           String phone = binding.edtPhoneNo.getText().toString();
           String address = binding.edtAddress.getText().toString();
           String salary = binding.edtSalary.getText().toString();
           String startOfWorkDate = binding.edtStartOfWork.getText().toString();
           String endOfWorkDate = binding.edtEndOfWork.getText().toString();


           if (!(name.equals("")||phone.equals("")||address.equals("")||getDepartment().equals("")||salary.equals("")
           ||getWorkHour().equals("")||getWorkDays().equals("")||startOfWorkDate.equals("")||endOfWorkDate.equals(""))){
               //initialize doctor object to add it to firestore
               doctor.setName(name);
               doctor.setPhone(phone);
               doctor.setAddress(address);
               doctor.setDepartment(getDepartment());
               doctor.setSalary(Float.parseFloat(salary));
               doctor.setWorkHours(getWorkHour());
               doctor.setWorkDays(getWorkDays());
               doctor.setWorkStartDate(startOfWorkDate);
               doctor.setWorkEndDate(endOfWorkDate);
               doctor.setEmail(doctorData.getEmail());
               doctor.setGender(doctorData.getGender());
               doctor.setIdentityNumber(doctorData.getIdentityNumber());
               doctor.setMembershipNumber(doctorData.getMembershipNumber());
               uploadImage();

               firestore.collection(Constants.DOCTORS_COLLECTION_NAME).document(doctorData.getEmail()).set(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          Toast.makeText(UpdateDoctorActivity.this, "doctor update successfully", Toast.LENGTH_SHORT).show();
                      }else{
                          Toast.makeText(UpdateDoctorActivity.this, " doctor update failed", Toast.LENGTH_SHORT).show();
                      }
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(UpdateDoctorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               });

           }



       }
    }


//method for upload image and store it into storage cloud
    private void uploadImage(){
        if ( uri !=null){
            StorageReference reference=storage.getReference().child("doctorsProfileImages").child(doctorData.getEmail());
            reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(UpdateDoctorActivity.this, "upload file successfully", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                doctor.setImageUrl(uri.toString());
                                Log.d(TAG, "onSuccess: "+uri);
                                Toast.makeText(UpdateDoctorActivity.this, "success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(UpdateDoctorActivity.this, "upload file failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateDoctorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            doctor.setImageUrl(doctorData.getImageUrl());
        }

    }
    //method to check which days doctor work and return it as an arraylist
    private ArrayList<String> getWorkDays() {
        ArrayList<String> workDays=new ArrayList<>();

        if(binding.chSaturday.isChecked())
            workDays.add(binding.chSaturday.getText().toString());
        if(binding.chSunday.isChecked())
            workDays.add(binding.chSunday.getText().toString());
        if(binding.chMonday.isChecked())
            workDays.add(binding.chMonday.getText().toString());
        if(binding.chWednesday.isChecked())
            workDays.add(binding.chWednesday.getText().toString());
        if(binding.chThursday.isChecked())
            workDays.add(binding.chTuesday.getText().toString());
        if(binding.chThursday.isChecked())
            workDays.add(binding.chThursday.getText().toString());

        return workDays;
    }

    //method to check in which department the doctor work
    private String getDepartment(){

        String department;

        if (binding.rbDep1.isChecked()) {
            department = "dept1";
        } else if (binding.rbDep2.isChecked()){
            department = "dept2";
        }else if (binding.rbDep3.isChecked()) {
            department = "dept3";
        }else{
            department = "dept4";
        }

        return department;
    }

private String getWorkHour(){
    String workHours;
    if (binding.rbWorkHourMorning.isChecked()) {
        workHours = "Morning 9:00-2:00 Am";
    }else{
        workHours = "Morning 3:00-8:00 pm";
    }
    return workHours;
  }

}