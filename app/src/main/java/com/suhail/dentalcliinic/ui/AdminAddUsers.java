package com.suhail.dentalcliinic.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityAdminAddUsersBinding;
import com.suhail.dentalcliinic.models.Doctor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AdminAddUsers extends AppCompatActivity {
    ActivityAdminAddUsersBinding binding;

    //define firebase features objects
   private FirebaseFirestore firestore;
   private FirebaseAuth auth;
   private FirebaseStorage storage;

   //profile image url
    private String doctorProfileImageUrl;
    private Uri uri;
    boolean imageChanged;


    //calendar
    Calendar myCalendar = Calendar.getInstance();
    ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                uri = result.getData().getData();
                if(uri!=null) {
                    binding.imgUserPhoto.setImageURI(uri);
                    imageChanged=true;
                }
        }
        }
    });
    private boolean createUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize imageChanged
        imageChanged=false;

        //initialize firebase
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        //initialize firebaseStorage
        storage=FirebaseStorage.getInstance();

        //initialize default image
        doctorProfileImageUrl="https://firebasestorage.googleapis.com/v0/b/dental-clinic-e56a4.appspot.com/o/doctorsProfileImages%2Fuser.png?alt=media&token=e303a606-3ce9-48df-93b4-b5a03e6a05dc";
        //get Date from User
        DatePickerDialog.OnDateSetListener startWorkingDate =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(binding.edtWorkStartDate);
            }
        };
        DatePickerDialog.OnDateSetListener endWorkingDate =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(binding.edtWorkEndDate);
            }
        };
        binding.edtWorkStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AdminAddUsers.this,startWorkingDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.edtWorkEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AdminAddUsers.this,endWorkingDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //implement save button code
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewDoctor();
            }
        });

        binding.imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcher.launch(intent);
            }
        });
    }
    private void updateLabel(EditText editText){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
    }


    public void registerNewDoctor() {
        if (auth.getCurrentUser() != null) {
            Doctor doctor=new Doctor();
            String name = binding.edtUserName.getText().toString();
            String email = binding.edtEmail.getText().toString();
            String phone = binding.edtPhoneNo.getText().toString();
            String address = binding.edtAddress.getText().toString();
            String identityNumberStr = binding.edtId.getText().toString();
            String membershipNumber = binding.edtMemberNo.getText().toString();
            String department = binding.spDepartment.getSelectedItem().toString();
            String gender;
            if (binding.rdMail.isChecked())
                gender = "male";
            else
                gender = "female";
            ArrayList<String> workDays = getWorkDays();
            String workHours = binding.spWorkTime.getSelectedItem().toString();
            String workStartDate = binding.edtWorkStartDate.getText().toString();
            String workEndDate = binding.edtWorkEndDate.getText().toString();

            if (name.equals("") || email.equals("") || phone.equals("") || address.equals("") || identityNumberStr.equals("")
                    || membershipNumber.equals("") || department.equals("") || gender.equals("") || workDays.size() == 0
                    || workHours.equals("") || workStartDate.equals("") || workEndDate.equals("")) {
                Toast.makeText(this, "please fill all fields first", Toast.LENGTH_SHORT).show();
            } else {

                int identityNumber = Integer.parseInt(identityNumberStr);

                //initialize doctor object to add it to firestore
                doctor.setName(name);
                doctor.setEmail(email);
                doctor.setPhone(phone);
                doctor.setAddress(address);
                doctor.setIdentityNumber(identityNumber);
                doctor.setMembershipNumber(membershipNumber);
                doctor.setDepartment(department);
                doctor.setGender(gender);
                doctor.setWorkHours(workHours);
                doctor.setWorkDays(workDays);
                doctor.setWorkStartDate(workStartDate);
                doctor.setWorkEndDate(workEndDate);
                doctor.setFirstTime(true);

                if(imageChanged == false) {
                    doctor.setImageUrl(doctorProfileImageUrl);
                    firestore.collection("doctors").document(email).set(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AdminAddUsers.this, "Added doctor Succefully", Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else
                                Toast.makeText(AdminAddUsers.this, "Failed to add doctor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                //define reference for doctor profile image
                StorageReference ref = storage.getReference().child("doctorsProfileImages").child(email);

                //check all requirement for uploading image
                if(uri != null && !email.equals("")){

                    //upload the image
                    ref.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AdminAddUsers.this, "uploaded file successfully", Toast.LENGTH_SHORT).show();
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        firestore.collection("doctors").document(email).set(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AdminAddUsers.this, "Added doctor Succefully", Toast.LENGTH_SHORT).show();
                                                    clearFields();
                                                } else
                                                    Toast.makeText(AdminAddUsers.this, "Failed to add doctor", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(AdminAddUsers.this, "failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminAddUsers.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }


            }
        }}
        else
            Toast.makeText(this, "something wrong with your account", Toast.LENGTH_SHORT).show();
    }

    //method to clear all fields after adding the user
    private void clearFields() {
        binding.edtUserName.setText("");
        binding.edtEmail.setText("");
        binding.edtAddress.setText("");
        binding.edtPhoneNo.setText("");
        binding.edtMemberNo.setText("");
        binding.edtId.setText("");
        binding.edtSalary.setText("");
        binding.edtWorkEndDate.setText("");
        binding.edtWorkStartDate.setText("");
        binding.chSaturday.setChecked(true);
        binding.chSunday.setChecked(false);
        binding.chMonday.setChecked(false);
        binding.chTuesday.setChecked(false);
        binding.chWednesday.setChecked(false);
        binding.chThursday.setChecked(false);
        binding.rdMail.setChecked(true);
        binding.rdFemale.setChecked(false);
        doctorProfileImageUrl="https://firebasestorage.googleapis.com/v0/b/dental-clinic-e56a4.appspot.com/o/doctorsProfileImages%2Fuser.png?alt=media&token=e303a606-3ce9-48df-93b4-b5a03e6a05dc";
        Picasso.get().load(doctorProfileImageUrl).into(binding.imgUserPhoto);
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

}