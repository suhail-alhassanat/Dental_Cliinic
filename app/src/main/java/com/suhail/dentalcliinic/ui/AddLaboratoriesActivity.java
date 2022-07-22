package com.suhail.dentalcliinic.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityAddLaboratoriesBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Laboratory;

public class AddLaboratoriesActivity extends AppCompatActivity {

    //define firebase features objects
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    //define ProgressDialog
    ProgressDialog p ;

    ActivityAddLaboratoriesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddLaboratoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize progressDialog
        p = new ProgressDialog(AddLaboratoriesActivity.this);

        //initialize firebase
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        //implement save button code
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewLaboratory();
            }
        });

    }

    private void registerNewLaboratory(){
        //
        p.setTitle("Adding New Laboratory");
        p.setMessage("Loading..");
        p.setCanceledOnTouchOutside(false);
        p.show();
        //

        if(auth.getCurrentUser()!=null){
            String name = binding.edtLaboratoryName.getText().toString();
            String address = binding.edtAddress.getText().toString();
            String phone = binding.edtPhoneNo.getText().toString();

            //check if all required fields are filled
            if(name.equals("") || address.equals("") || phone.equals("")){
                Toast.makeText(this, "all fields required!", Toast.LENGTH_SHORT).show();
                p.dismiss();
            }
            else{

                //check if laboratory is already exist
                firestore.collection(Constants.LABORATORY_COLLECTION_NAME)
                        .document(name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        //if laboratory is already exist
                        if(task.isSuccessful()){
                            if(task.getResult().exists()){
                            Toast.makeText(AddLaboratoriesActivity.this, "Laboratory is already exist", Toast.LENGTH_SHORT).show();
                            p.dismiss();}
                            else{

                                //if laboratory does not exist (Add new laboratory)
                                Laboratory laboratory = new Laboratory(name,address,phone);
                                firestore.collection(Constants.LABORATORY_COLLECTION_NAME)
                                        .document(name)
                                        .set(laboratory)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    p.dismiss();
                                                    Toast.makeText(AddLaboratoriesActivity.this, "Laboratory added successfully", Toast.LENGTH_SHORT).show();
//                                        clearFields();
                                                }
                                                else
                                                {
                                                    p.dismiss();
                                                    Toast.makeText(AddLaboratoriesActivity.this, "Failed to add laboratory", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }

                        }
                    }
                });

            }
        }
        else
        {
            Toast.makeText(this, "you should login first!", Toast.LENGTH_SHORT).show();
            p.dismiss();
        }
    }

    private void clearFields(){
        binding.edtAddress.setText("");
        binding.edtLaboratoryName.setText("");
        binding.edtPhoneNo.setText("");
    }
}