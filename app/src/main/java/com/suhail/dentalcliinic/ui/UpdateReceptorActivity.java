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
import com.suhail.dentalcliinic.databinding.ActivityUpdateReceptorBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Doctor;
import com.suhail.dentalcliinic.models.Receptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateReceptorActivity extends AppCompatActivity {
ActivityUpdateReceptorBinding binding;
//define firebase feature object
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseStorage storage;
    Uri uri;
    //define receptor object
    Receptor receptor;

    //define receptor data object
    Receptor receptorData;

    //define calender
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateReceptorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize calender
        calendar=Calendar.getInstance();

        //initialize firebase features
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();

        //initialize show receptor details
        showReceptorDetails();



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
                new DatePickerDialog(UpdateReceptorActivity.this, startOfWorkDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(UpdateReceptorActivity.this, endOfWorkDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //load profile image from studio
        binding.imgReceptorPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcher.launch(intent);
            }
        });


        //initialize update receptor
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReceptor();
                finish();
                startActivity(new Intent(UpdateReceptorActivity.this,AdminUserManagementActivity.class));
            }
        });

    }


    private void updateReceptor() {
        if (auth.getCurrentUser() != null) {
            receptor = new Receptor();
            String name = binding.txtReceptorName.getText().toString();
            String phone = binding.edtPhoneNo.getText().toString();
            String address = binding.edtAddress.getText().toString();
            String salary = binding.edtSalary.getText().toString();
            String startOfWorkDate = binding.edtStartOfWork.getText().toString();
            String endOfWorkDate = binding.edtEndOfWork.getText().toString();


            if (!(name.equals("") || phone.equals("") || address.equals("") || salary.equals("")
                    || getWorkHour().equals("") || getWorkDays().equals("") || startOfWorkDate.equals("") || endOfWorkDate.equals(""))) {
                //initialize doctor object to add it to firestore
                receptor.setName(name);
                receptor.setPhone(phone);
                receptor.setAddress(address);
                receptor.setSalary(Float.parseFloat(salary));
                receptor.setWorkHours(getWorkHour());
                receptor.setWorkDays(getWorkDays());
                receptor.setWorkStartDate(startOfWorkDate);
                receptor.setWorkEndDate(endOfWorkDate);
                receptor.setEmail(receptorData.getEmail());
                receptor.setGender(receptorData.getGender());
                receptor.setIdentityNumber(receptorData.getIdentityNumber());


                if (uri != null) {
                    StorageReference reference = storage.getReference().child("receptorsProfileImages").child(receptorData.getEmail());
                    reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateReceptorActivity.this, "upload image successfully", Toast.LENGTH_SHORT).show();

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        receptor.setImageUrl(uri.toString());
                                        firestore.collection(Constants.RECEPTORS_COLLECTION_NAME).document(receptor.getEmail()).set(receptor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                             if (task.isSuccessful()){
                                                 Toast.makeText(UpdateReceptorActivity.this, "Receptor update successfully", Toast.LENGTH_SHORT).show();
                                             }else
                                                 Toast.makeText(UpdateReceptorActivity.this, "Receptor update failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UpdateReceptorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                            } else {
                                Toast.makeText(UpdateReceptorActivity.this, "upload image failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateReceptorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    receptor.setImageUrl(receptorData.getImageUrl());
                    firestore.collection(Constants.RECEPTORS_COLLECTION_NAME).document(receptorData.getEmail()).set(receptor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UpdateReceptorActivity.this, "receptor update successfully", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(UpdateReceptorActivity.this, "receptor update failed ", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateReceptorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }else
            Toast.makeText(this, "must be logged in ", Toast.LENGTH_SHORT).show();
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


    ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                uri = result.getData().getData();
                if(uri!=null) {
                    binding.imgReceptorPhoto.setImageURI(uri);
                }
            }
        }
    });


    private String getWorkHour(){
        String workHours;
        if (binding.rbWorkHourMorning.isChecked()) {
            workHours = "Morning 9:00-2:00 Am";
        }else{
            workHours = "Morning 3:00-8:00 pm";
        }
        return workHours;
    }

    private void showReceptorDetails(){
        receptorData= (Receptor) getIntent().getSerializableExtra(ReceptorDetailsActivity.UPDATE_RECEPTOR_KEY);
        if (receptorData !=null){

            if (!receptorData.getImageUrl().isEmpty()) {
                Picasso.get().load(receptorData.getImageUrl()).into(binding.imgReceptorPhoto);
            }
            if (!receptorData.getPhone().isEmpty()) {
                binding.edtPhoneNo.setText(receptorData.getPhone());
            }

            if (!receptorData.getName().isEmpty()) {
                binding.txtReceptorName.setText(receptorData.getName());
            }

            if (!receptorData.getAddress().isEmpty()) {
                binding.edtAddress.setText(receptorData.getAddress());
            }


            if (!receptorData.getWorkDays().isEmpty()) {
                binding.chSaturday.setChecked(receptorData.getWorkDays().contains("saturday"));
                binding.chSunday.setChecked(receptorData.getWorkDays().contains("sunday"));
                binding.chMonday.setChecked(receptorData.getWorkDays().contains("monday"));
                binding.chTuesday.setChecked(receptorData.getWorkDays().contains("Tuesday"));
                binding.chWednesday.setChecked(receptorData.getWorkDays().contains("Wednesday"));
                binding.chThursday.setChecked(receptorData.getWorkDays().contains("Thursday"));
            }

            if (!receptorData.getWorkHours().isEmpty()) {
                binding.rbWorkHourMorning.setChecked(receptorData.getWorkHours().equals("Morning 9:00-2:00 Am"));
                binding.rbWorkHourEvening.setChecked(receptorData.getWorkHours().equals("Morning 3:00-8:00 pm"));
            }

            if (!receptorData.getWorkStartDate().isEmpty()) {
                binding.edtStartOfWork.setText(receptorData.getWorkStartDate());
            }

            if (!receptorData.getWorkEndDate().isEmpty()) {
                binding.edtEndOfWork.setText(receptorData.getWorkEndDate());
            }

            if (!String.valueOf(receptorData.getSalary()).isEmpty()){
                binding.edtSalary.setText(String.valueOf(receptorData.getSalary()));
            }
        }
    }

}