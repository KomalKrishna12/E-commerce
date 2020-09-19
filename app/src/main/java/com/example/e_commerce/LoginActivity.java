package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Model.Users;
import com.example.e_commerce.Prevelet.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText inputPhoneNumber, inputPassword;
    private Button loginBtn;
    private TextView adminLink, nonAdminLink;
    private ProgressDialog loadingBar;
    private String parentDb = "Users";
    private CheckBox checkboxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPhoneNumber = findViewById(R.id.login_phone_no_input);
        inputPassword = findViewById(R.id.login_password_input);
        loginBtn = findViewById(R.id.login_btn);
        adminLink = findViewById(R.id.admin_panel_link);
        nonAdminLink = findViewById(R.id.non_admin_panel_link);
        loadingBar = new ProgressDialog(this);
        checkboxRememberMe = findViewById(R.id.remember_me_checkbox);
        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                nonAdminLink.setVisibility(View.VISIBLE);
                parentDb = "Admin";
            }
        });

        nonAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                nonAdminLink.setVisibility(View.INVISIBLE);
                parentDb = "Users";
            }
        });
    }

    private void loginUser() {
        String phone = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            inputPhoneNumber.setError("Enter Phone number");
        } else if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter password");
        } else {
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please wait! while we are checking credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            allowAccesToAccount(phone, password);
        }
    }

    private void allowAccesToAccount(final String phone, final String password) {

        if (checkboxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.userPhoneKey, phone);
            Paper.book().write(Prevalent.userPasswordKey, password);
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDb).child(phone).exists()) {
                    Users userData = snapshot.child(parentDb).child(phone).getValue(Users.class);
                    if (userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {
                            if (parentDb.equals("Admin")) {
                                Toast.makeText(LoginActivity.this, "Welcome admin, you are logged in successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            } else if (parentDb.equals("Users")) {
                                Toast.makeText(LoginActivity.this, "logged in successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUsers=userData;
                                startActivity(intent);
                            }
                        } else
                            Toast.makeText(LoginActivity.this, "password is incorrect", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " no is not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}