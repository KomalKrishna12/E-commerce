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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewActivity extends AppCompatActivity {
    private String categoryName, price, description, name, currentDat, currentTime, productRandomKey, downloadImgUrl;
    private ImageView inputProductImg;
    private Button addNewProduct;
    private EditText inputProductName, inputProductDescription, inputProductPrice;
    private static final int gallery = 1;
    private Uri imageUri;
    private StorageReference productImgRef;
    private DatabaseReference productRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new);

        categoryName = getIntent().getExtras().get("category").toString();
        productImgRef = FirebaseStorage.getInstance().getReference().child("Product image");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);

        inputProductImg = findViewById(R.id.select_prodduct_image);
        addNewProduct = findViewById(R.id.add_products_input_btn);
        inputProductName = findViewById(R.id.product_name);
        inputProductDescription = findViewById(R.id.product_description);
        inputProductPrice = findViewById(R.id.product_price);

        inputProductImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatProductData();
            }
        });

    }

    private void validatProductData() {
        name = inputProductName.getText().toString();
        description = inputProductDescription.getText().toString();
        price = inputProductPrice.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            inputProductName.setError("Please write product name");
        } else if (TextUtils.isEmpty(description)) {
            inputProductDescription.setError("Please write product description");
        } else if (TextUtils.isEmpty(price)) {
            inputProductPrice.setError("Please write product price");
        } else {
            storeProductInfo();
        }

    }

    private void storeProductInfo() {
        loadingBar.setTitle("Adding new product");
        loadingBar.setMessage("Dear Admin, Please wait while we are adding new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat curDate = new SimpleDateFormat("mm-dd-yyyy ");
        currentDat = curDate.format(calendar.getTime());

        SimpleDateFormat curTime = new SimpleDateFormat("hh:mm:ss a");
        currentTime = curTime.format(calendar.getTime());

        productRandomKey = currentDat + currentTime;

        final StorageReference filePath = productImgRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddNewActivity.this, "Error : " + e.toString(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewActivity.this, "Image is uploaded successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImgUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewActivity.this, "got product image save successfully...", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pid", productRandomKey);
        hashMap.put("name", name);
        hashMap.put("price", price);
        hashMap.put("description", description);
        hashMap.put("date", currentDat);
        hashMap.put("time", currentTime);
        hashMap.put("image", downloadImgUrl);
        hashMap.put("category", categoryName);

        productRef.child(productRandomKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AdminAddNewActivity.this, AdminCategoryActivity.class));
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewActivity.this, "Product is added successfully...", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewActivity.this, "Error : " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallery);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallery && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            inputProductImg.setImageURI(imageUri);
        }
    }
}