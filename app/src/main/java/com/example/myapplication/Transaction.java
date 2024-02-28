package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class Transaction extends AppCompatActivity {
EditText pickDate;
CardView inTransaction, outTransaction, openCategory;
Button saveTransaction;
int transactionTypeState = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        pickDate = findViewById(R.id.txt_transaction_date);
        inTransaction = findViewById(R.id.in_transaction);
        outTransaction = findViewById(R.id.out_transaction);
        saveTransaction = findViewById(R.id.btn_save_transaction);
        openCategory = findViewById(R.id.btn_open_category);




        inTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionTypeState = 1;
               saveTransaction.setBackgroundTintList(ContextCompat.getColorStateList(Transaction.this, R.color.green));
            }
        });
        outTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionTypeState = 2;
                saveTransaction.setBackgroundTintList(ContextCompat.getColorStateList(Transaction.this, R.color.crimson));
            }
        });
        openCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenCategories = new Intent(Transaction.this,CreatePurpose.class);
                startActivity(intentOpenCategories);
            }
        });

        // do transaction
        saveTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // check state of the transaction type
                if (transactionTypeState == 0){
                    showMessage("Transaction Not Selected","Select transaction");
                } else if (transactionTypeState == 2) {
                    showMessage("Transaction Out","Transaction type outgoing amount");
                } else if (transactionTypeState == 1) {
                    showMessage("Transaction In","Transaction type incoming amount");
                }else {
                    showMessage("Unknown Error","Transaction type error try selecting again");
                }
            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

    }// end of onCreate
    // date picker select date time of transaction
    private void showDateTimePicker() {
        // Get current date and time
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    // Set selected date to EditText
                    String selectedDate = dayOfMonth1 + "/" + (monthOfYear + 1) + "/" + year1;

                    // Create time picker dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (view1, hourOfDay1, minute1) -> {
                                // Set selected time to EditText
                                String selectedTime = " " + hourOfDay1 + ":" + minute1;
                                pickDate.setText(selectedDate + selectedTime);
                            },
                            hourOfDay,
                            minute,
                            true // 24-hour time format
                    );

                    // Show time picker dialog after date is selected
                    timePickerDialog.show();
                },
                year,
                month,
                dayOfMonth
        );

        // Show date picker dialog
        datePickerDialog.show();
    }
    // create pop up message template
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

}