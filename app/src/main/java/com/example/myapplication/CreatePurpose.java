package com.example.myapplication;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CreatePurpose extends AppCompatActivity {
Spinner transaction_Category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purpose);

        transaction_Category = findViewById(R.id.transaction_item_category);


        // add array item to category items
        List<String> itemList = new ArrayList<>();
        itemList.add("Transaction IN");
        itemList.add("Transaction OUT");
        itemList.add("On HOLD");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatePurpose.this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transaction_Category.setAdapter(adapter);
    }
}