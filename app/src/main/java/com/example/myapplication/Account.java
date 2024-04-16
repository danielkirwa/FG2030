package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Account extends AppCompatActivity {
Spinner accountType;
LinearLayout accountLayout, createAccountLayout;
ImageView createAccount,showAccount;
DatabaseReference databaseReference;
Button openAccount;
EditText  accountProvider,accountNumber,otherCode,accountDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountType = findViewById(R.id.account_category);
        accountLayout = findViewById(R.id.account_layout);
        createAccountLayout = findViewById(R.id.create_account_layout);
        createAccount = findViewById(R.id.show_create_account);
        showAccount = findViewById(R.id.show_accounts);
        accountProvider = findViewById(R.id.txt_account_provider);
        accountNumber = findViewById(R.id.txt_account_number);
        otherCode = findViewById(R.id.txt_account_code);
        accountDescription = findViewById(R.id.txt_account_description);
        openAccount = findViewById(R.id.btn_open_new_account);

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

        openAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccountToDatabase();
            }
        });

     // end of main method
    }
    // out main method
    @SuppressLint("UseCompatLoadingForDrawables")
    public  void showMessage(String popUpTitle, String popupMessage){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(popUpTitle);
        builder.setMessage(popupMessage);
        builder.setIcon(getResources().getDrawable(R.drawable.money));
        builder.setPositiveButton("OK",null);
        builder.show();
    }
    public static  class Accounts{
        private String sector;
        private String provider;
        private String account;
        private String code;
        private String description;

        public Accounts(String sector, String provider, String account, String code, String description) {
            this.sector = sector;
            this.provider = provider;
            this.account = account;
            this.code = code;
            this.description = description;
        }

        public String getSector() {
            return sector;
        }

        public String getProvider() {
            return provider;
        }

        public String getAccount() {
            return account;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    // add data to database
    private  void addAccountToDatabase (){
   String sector = accountType.getSelectedItem().toString();
   String number = accountNumber.getText().toString();
   String provider = accountProvider.getText().toString();
   String moreCode = otherCode.getText().toString();
   String description = accountDescription.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Accounts").child(number);
        databaseReference.setValue(new Accounts(sector,provider,number,moreCode,description))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showMessage("Success","New account opened");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Failed", "New account not open error");
                    }
                });
    }

}