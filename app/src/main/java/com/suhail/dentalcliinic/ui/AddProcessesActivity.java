package com.suhail.dentalcliinic.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityAddProcessesBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Process;

public class AddProcessesActivity extends AppCompatActivity {
    ActivityAddProcessesBinding binding;
//define firebase features objects
    FirebaseAuth auth;
    FirebaseFirestore firestore;

//define progress dialog
    ProgressDialog p;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddProcessesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize progress dialog
        p=new ProgressDialog(AddProcessesActivity.this);



        //initialize firebase features objects
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        
        //action on save button to add process
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProcess();
            }
        });

    }
    
    private void addProcess(){
        String name=binding.edtProcessName.getText().toString();
        String processSection=binding.spProcessSection.getSelectedItem().toString();


        if (auth.getCurrentUser()!=null){

            //show progress dialog
            p.setTitle("Adding New Process");
            p.setMessage("Please wait... ");
            p.setCanceledOnTouchOutside(false);
            p.show();

           //chick all fails are filled or not
            if (!(processSection.equals("")||name.equals(""))){

            //store process on firestore

                firestore.collection(Constants.PROCESSES_COLLECTION_NAME).document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                Toast.makeText(AddProcessesActivity.this, "sorry! this process is already exists", Toast.LENGTH_SHORT).show();
                                p.dismiss();
                            }else {
                                firestore.collection(Constants.PROCESSES_COLLECTION_NAME).document(name).set(new Process(name, processSection)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddProcessesActivity.this, "added process successfully", Toast.LENGTH_SHORT).show();
                                            //clear name
                                            binding.edtProcessName.setText("");
                                            //dismiss progress dialog
                                            p.dismiss();
                                        } else {
                                            Toast.makeText(AddProcessesActivity.this, "process add failed", Toast.LENGTH_SHORT).show();
                                            p.dismiss();
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddProcessesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        p.dismiss();
                                    }
                                });
                            }
                        }else {
                            Toast.makeText(AddProcessesActivity.this, "try again", Toast.LENGTH_SHORT).show();
                            p.dismiss();
                        }
                    }
                });
            }else {
                Toast.makeText(this, "Please enter all failed ", Toast.LENGTH_SHORT).show();
                p.dismiss();
            }

        }else {
            Toast.makeText(this, "Sorry! you must be logged in", Toast.LENGTH_SHORT).show();
            p.dismiss();
        }
        
    }

}