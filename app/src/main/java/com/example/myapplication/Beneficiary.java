package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Beneficiary extends AppCompatActivity {
Spinner beneficiary_category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary);
        beneficiary_category = findViewById(R.id.beneficiary_category);

        List<String> itemList = new ArrayList<>();
        itemList.add("Select Type");
        itemList.add("Transaction IN");
        itemList.add("Transaction OUT");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Beneficiary.this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beneficiary_category.setAdapter(adapter);

    }
}