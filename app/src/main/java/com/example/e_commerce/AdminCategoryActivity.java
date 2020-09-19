package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView tshirt, sports_tshirt, female_Dress, sweater, glasses, purse, hat, shoes, headphone, watch, laptop, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tshirt = findViewById(R.id.t_shirt);
        sports_tshirt = findViewById(R.id.sports_t_shirt);
        female_Dress = findViewById(R.id.female_dress);
        sweater = findViewById(R.id.sweater);
        glasses = findViewById(R.id.glasses);
        purse = findViewById(R.id.purse_bags);
        hat = findViewById(R.id.hats);
        shoes = findViewById(R.id.shoes);
        headphone = findViewById(R.id.headphones);
        watch = findViewById(R.id.watches);
        laptop = findViewById(R.id.lapop);
        phone = findViewById(R.id.mobiles);

        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "T-shirt");
                startActivity(intent);
            }
        });

        sports_tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Sports T-shirts");
                startActivity(intent);
            }
        });
        female_Dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Female Dresses");
                startActivity(intent);
            }
        });

        sweater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Sweaters");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Glasses");
                startActivity(intent);
            }
        });

        purse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Purse");
                startActivity(intent);
            }
        });

        hat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Hats");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Shoes");
                startActivity(intent);
            }
        });
        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Headphones");
                startActivity(intent);
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Watch");
                startActivity(intent);
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Laptop");
                startActivity(intent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewActivity.class);
                intent.putExtra("category", "Mobile Phone");
                startActivity(intent);
            }
        });

    }
}