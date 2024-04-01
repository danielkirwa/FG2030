package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Account extends AppCompatActivity {
Spinner accountType;
LinearLayout accountLayout, createAccountLayout;
ImageView createAccount,showAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountType = findViewById(R.id.account_category);
        accountLayout = findViewById(R.id.account_layout);
        createAccountLayout = findViewById(R.id.create_account_layout);
        createAccount = findViewById(R.id.show_create_account);
        showAccount = findViewById(R.id.show_accounts);

        List<String> accountTypeList = new ArrayList<>();
        accountTypeList.add("Select Account Category");
        accountTypeList.add("Bank");
        accountTypeList.add("Online");
        accountTypeList.add("Mobile");
        accountTypeList.add("Insurance");
        accountTypeList.add("Group");
        accountTypeList.add("Other");

        ArrayAdapter<String> accountTypeListAdapter = new ArrayAdapter<>(Account.this, android.R.layout.simple_spinner_item, accountTypeList);
        accountTypeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType.setAdapter(accountTypeListAdapter);
        // add select event to the spinner
        accountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = accountTypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // show/hide layout
        createAccountLayout.setVisibility(View.GONE);
        showAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountLayout.setVisibility(View.GONE);
                accountLayout.setVisibility(View.VISIBLE);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountLayout.setVisibility(View.GONE);
                createAccountLayout.setVisibility(View.VISIBLE);
            }
        });


    }
}