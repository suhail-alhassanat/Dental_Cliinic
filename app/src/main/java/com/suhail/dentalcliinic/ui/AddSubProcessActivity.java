package com.suhail.dentalcliinic.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.adapters.ProcessRVAdapter;
import com.suhail.dentalcliinic.adapters.ProcessSPAdapter;
import com.suhail.dentalcliinic.databinding.ActivityAddSubProcessBinding;
import com.suhail.dentalcliinic.helper.Constants;
import com.suhail.dentalcliinic.models.Process;
import com.suhail.dentalcliinic.models.SubProcess;

public class AddSubProcessActivity extends AppCompatActivity {
ActivityAddSubProcessBinding binding;

    //define firebase features objects
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    //define progress dialog
    ProgressDialog p;

    //define progress dialog
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddSubProcessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize progress dialog
        p=new ProgressDialog(AddSubProcessActivity.this);

        //initialize progress dialog
        pd=new ProgressDialog(AddSubProcessActivity.this);

        //initialize firebase features objects
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        //initialize process into spinner
        initProcessSp();


        //action on save button to add process
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubProcess();
            }
        });
    }

    private void initProcessSp() {

        p.setTitle("Loading processes");
        p.setMessage("please wait... ");
        p.setCanceledOnTouchOutside(false);
        p.show();

        firestore.collection(Constants.PROCESSES_COLLECTION_NAME).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null&&error==null){
                binding.spSubProcessType.setAdapter(new ProcessSPAdapter(value.toObjects(Process.class),AddSubProcessActivity.this));
                p.dismiss();
                }else {
                    Toast.makeText(AddSubProcessActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }
            }
        });


    }



    private void addSubProcess() {
         String name = binding.edtSubProcessName.getText().toString();
         String type = binding.spSubProcessType.getSelectedItem().toString();
         float price =Float.parseFloat(binding.edtSubProcessPrice.getText().toString()) ;


        if (auth.getCurrentUser() != null) {

            //show progress dialog
            pd.setTitle("Adding New Sub Process");
            pd.setMessage("Please wait... ");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            //chick all fails are filled or not
            if (!(name.equals("") || type.equals("")||String.valueOf(price).equals(""))){
                firestore.collection(Constants.SUB_PROCESSES_COLLECTION_NAME).document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                Toast.makeText(AddSubProcessActivity.this, "sorry! this sub process is already exists ", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }else {
                                firestore.collection(Constants.SUB_PROCESSES_COLLECTION_NAME).document(name).set(new SubProcess(name,type,price)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(AddSubProcessActivity.this, "added sub process successfully", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();

                                            //clear failed
                                            binding.edtSubProcessName.setText("");
                                            binding.edtSubProcessPrice.setText("");

                                        }else {
                                            Toast.makeText(AddSubProcessActivity.this, "added sub process failed", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();

                                        }
                                        pd.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddSubProcessActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                });
                            }
                        }
                    }
                });

            }else{
                Toast.makeText(this, "please enter all fields first !", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

           }else{
            Toast.makeText(this, "oops! you are not logged in", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }

    }
}