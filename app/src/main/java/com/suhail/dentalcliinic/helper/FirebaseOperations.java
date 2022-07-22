package com.suhail.dentalcliinic.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.suhail.dentalcliinic.models.Doctor;
import com.suhail.dentalcliinic.models.Receptor;
import com.suhail.dentalcliinic.models.userCategory;

public class FirebaseOperations {
    Context context;
    public static final String TAG="TAG";
    private FirebaseOperations(Context context){
        this.context=context;
    }

    //global variables
    private boolean addResult=false;
    private boolean addToCategoryResult;

    //singleTon way
    public static FirebaseOperations getInstance(Context context){
        return new FirebaseOperations(context);
    }
    //define firebase features objects
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    public boolean registerNewUser(Object user) {
        if (auth.getCurrentUser() != null) {

            //Doctor add operation
            if(user instanceof Doctor){
                firestore.collection("doctors").document(((Doctor)user).getEmail()).set((Doctor)user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: before addtocategory "+addResult);
                            if(addToCategoryTable(((Doctor) user).getEmail(),2)){
                                addResult=true;
                                Log.d(TAG, "onComplete: after add to category is success "+addResult);
                            }
                            }
                    }
                });
            }
        }

        //Receptor add operation
        else if(user instanceof Receptor){
            firestore.collection("receptors").document(((Receptor)user).getEmail()).set((Receptor)user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if(addToCategoryTable(((Receptor) user).getEmail(),3))
                            addResult=true;
                        else
                            addResult=false;
                    } else
                        addResult=false;
                }
            });
        }
        Log.d(TAG, "registerNewUser: result "+addResult);
        return addResult;
    }

    private boolean addToCategoryTable(String email, int type) {
        firestore.collection("userCategories").document(email).set(new userCategory(email,type)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    addToCategoryResult = true;
                    Log.d(TAG, "onComplete: addToCategory result"+addToCategoryResult);
                }else {
                 addToCategoryResult=false;
                    Toast.makeText(context, "Failed to add to category", Toast.LENGTH_SHORT).show();
            }}
        });
        Log.d(TAG, "addToCategoryTable: result: "+addToCategoryResult);
        return addToCategoryResult;
    }
    private int userExist=0;
    public int isUserExist(Object user){
        if(user instanceof Doctor){
            firestore.collection("doctors")
                    .document(((Doctor) user).getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        userExist = 1;
                }
                }
            });
        }


        else if(user instanceof Receptor){
            firestore.collection("receptors")
                    .document(((Receptor) user).getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                    userExist = 1;
                            }
                        }
                    });

        }
        return userExist;
    }
}
