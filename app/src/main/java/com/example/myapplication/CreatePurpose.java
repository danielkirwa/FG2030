package com.example.myapplication;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CreatePurpose extends AppCompatActivity {
Spinner transaction_Category;
Button savePurpose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purpose);

        transaction_Category = findViewById(R.id.transaction_item_category);
        savePurpose = findViewById(R.id.btn_save_transaction_item);


        // add array item to category items
        List<String> itemList = new ArrayList<>();
        itemList.add("Transaction IN");
        itemList.add("Transaction OUT");
        itemList.add("On HOLD");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatePurpose.this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transaction_Category.setAdapter(adapter);



        // insert purpose to db
        // Write a message to the database
        savePurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database =  FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Purpose");

                myRef.setValue("Hello, World!");
            }
        });

    }
}