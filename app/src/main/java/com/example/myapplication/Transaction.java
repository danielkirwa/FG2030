package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Transaction extends AppCompatActivity {
EditText pickDate;
CardView inTransaction, outTransaction, openCategory;
Button saveTransaction;
Spinner selectPurpose;
    List<Record> recordList = new ArrayList<>();
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
        selectPurpose = findViewById(R.id.transaction_item);


// call methods here
        selectPurposeForDb();

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


    // retriev data and put in a list
    public void selectPurposeForDb() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Purpose");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String purpose = snapshot.child("purpose").getValue(String.class);
                    String code = snapshot.child("code").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);

                    // Create a record object and add it to the list
                    Record record = new Record(purpose, code, type, description);
                    recordList.add(record); // Add record to list
                }

                // Create a list of purposes to display in the Spinner
                List<String> purposeList = new ArrayList<>();
                for (Record record : recordList) {
                    purposeList.add(record.getPurpose());
                }

                // Create ArrayAdapter and set it to the Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Transaction.this, android.R.layout.simple_spinner_item, purposeList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectPurpose.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                showMessage("Error","Could not find any Purpose");
            }
        });
    }

    public class Record {
        private String purpose;
        private String code;
        private String type;
        private String description;

        public Record(String purpose, String code, String type, String description) {
            this.purpose = purpose;
            this.code = code;
            this.type = type;
            this.description = description;
        }

        public String getPurpose() {
            return purpose;
        }

        // Add getter and setter methods for each field
        // Getter and setter methods...
    }

}