package com.suhail.dentalcliinic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.suhail.dentalcliinic.R;
import com.suhail.dentalcliinic.databinding.ActivityAdminControlBinding;

public class AdminControlActivity extends AppCompatActivity {
ActivityAdminControlBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initializing user management button
        binding.btnUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminControlActivity.this,AdminUserManagementActivity.class));
            }
        });

        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(AdminControlActivity.this, binding.btnSettings);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //check what item user clicked
                        switch (item.getItemId()){
                            case R.id.menu_laboratories:
                                startActivity(new Intent(AdminControlActivity.this,ManageLaboratoriesActivity.class));
                                break;
                            case R.id.menu_pharma:
                                Toast.makeText(AdminControlActivity.this, "you cliked: "+item.getTitle(), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(AdminControlActivity.this,ManageLaboratoriesActivity.class));
                                break;
                            case R.id.menu_process:
                                Toast.makeText(AdminControlActivity.this, "you cliked: "+item.getTitle(), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(AdminControlActivity.this,ManageLaboratoriesActivity.class));
                                break;
                            case R.id.menu_sup_process:
                                Toast.makeText(AdminControlActivity.this, "you cliked: "+item.getTitle(), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(AdminControlActivity.this,ManageLaboratoriesActivity.class));
                                break;
                        }
                        return true;
                    }
                });

                // Showing the popup menu
                popupMenu.show();



            }
        });


    }
}