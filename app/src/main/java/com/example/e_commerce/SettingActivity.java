package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Prevelet.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private TextView profileChangeTxtBtn, closeBtn, updateBtn;
    private EditText fullNameEdittext, phoneNoEdittext, addressEdittext;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profileImageView = findViewById(R.id.profile_setting_image);
        profileChangeTxtBtn = findViewById(R.id.profile_setting_image_change);
        closeBtn = findViewById(R.id.close_setting_btn);
        updateBtn = findViewById(R.id.update_setting_btn);
        fullNameEdittext = findViewById(R.id.settings_full_name);
        phoneNoEdittext = findViewById(R.id.settings_phone_number);
        addressEdittext = findViewById(R.id.settings_address);

        userInfoDisplay(profileImageView, fullNameEdittext, phoneNoEdittext, addressEdittext);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

        profileChangeTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error, try again!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {

        if (TextUtils.isEmpty(fullNameEdittext.getText().toString())) {
            fullNameEdittext.setError("Name is mandaatory");
        } else if (TextUtils.isEmpty(phoneNoEdittext.getText().toString())) {
            phoneNoEdittext.setError("phone no. is mandaatory");
        } else if (TextUtils.isEmpty(addressEdittext.getText().toString())) {
            addressEdittext.setError("address is mandaatory");
        } else if (checker.equals("clicked")) {
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account details.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePicRef.child(Prevalent.currentOnlineUsers.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                        throw task.getException();
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUri = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("name", fullNameEdittext.getText().toString());
                        hashMap.put("address", addressEdittext.getText().toString());
                        hashMap.put("phoneorder", phoneNoEdittext.getText().toString());
                        hashMap.put("image", myUri);
                        ref.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                        Toast.makeText(SettingActivity.this, "Profle info updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", fullNameEdittext.getText().toString());
        hashMap.put("address", addressEdittext.getText().toString());
        hashMap.put("phoneorder", phoneNoEdittext.getText().toString());
        ref.child(Prevalent.currentOnlineUsers.getPhone()).updateChildren(hashMap);

        startActivity(new Intent(SettingActivity.this, SettingActivity.class));
        Toast.makeText(SettingActivity.this, "Profle info updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEdittext,
                                 final EditText phoneNoEdittext, final EditText addressEdittext) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").
                child(Prevalent.currentOnlineUsers.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEdittext.setText(name);
                        phoneNoEdittext.setText(phone);
                        addressEdittext.setText(address);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}