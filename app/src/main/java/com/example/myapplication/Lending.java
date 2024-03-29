package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Lending extends AppCompatActivity {
CardView open_beneficiary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lending);

        open_beneficiary = findViewById(R.id.btn_open_beneficiary);


        open_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBeneficiary = new Intent(Lending.this,Beneficiary.class);
                startActivity(openBeneficiary);
            }
        });
    }
}