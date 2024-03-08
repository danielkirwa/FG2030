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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transaction extends AppCompatActivity {
EditText pickDate;
TextView amountIn,amountOut;
CardView inTransaction, outTransaction, openCategory;
Button saveTransaction;
Spinner selectPurpose;
EditText amount,description;
    List<Record> recordList = new ArrayList<>();
    List<String> purposeList = new ArrayList<>();
int transactionTypeState = 0;
String typeOfTransaction = "Not selected";
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
        amount = findViewById(R.id.txt_transaction_amount);
        description = findViewById(R.id.txt_transaction_description);
        amountIn = findViewById(R.id.lb_total_amount_in);
        amountOut = findViewById(R.id.lb_total_amount_out);


// call methods here
        selectPurposeForDb();
        getAllTransactionTotals();

        inTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionTypeState = 1;
                typeOfTransaction = "Transaction In";
               saveTransaction.setBackgroundTintList(ContextCompat.getColorStateList(Transaction.this, R.color.green));
            }
        });
        outTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionTypeState = 2;
                typeOfTransaction = "Transaction Out";
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
                } else {
                     // check empty fileds
                    if (selectPurpose.getSelectedItemPosition() == 0 || amount.getText().toString().trim().isEmpty() || pickDate.getText().toString().trim().isEmpty() || description.getText().toString().trim().isEmpty()) {
                        showMessage("Error", "Please select purpose , Description , amount or Date ");
                    } else {
                       saveTransactionToDatabase();
                    }

                }
            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });



         // get selected purpose details
        selectPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String transactionPurpose = purposeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    // add default select placeholder
                purposeList.add("Select Purpose");
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

     // method to save
     private void saveTransactionToDatabase() {
         long currentTimestamp = System.currentTimeMillis();
         String transType = typeOfTransaction.toString();
         String transPurpose = selectPurpose.getSelectedItem().toString();
         String transDate = pickDate.getText().toString();
         String transAmount = amount.getText().toString();
         String transDescription = description.getText().toString();

         FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference transactionRef = database.getReference("TransactionDone/" + transType)
                 .child(String.valueOf(currentTimestamp));

         DatabaseReference accountRef = database.getReference("Account");


         DatabaseReference transactionInTotalRef = accountRef.child("TransactionInTotal");
         DatabaseReference transactionOutTotalRef = accountRef.child("TransactionOutTotal");

         // Fetch the current value of TransactionInTotal and update it
         transactionInTotalRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 long transactionInTotal = dataSnapshot.exists() ? dataSnapshot.getValue(Long.class) : 0;
                 transactionInTotal += transType.equals("Transaction In") ? Long.parseLong(transAmount) : 0;

                 // Update TransactionInTotal node with new value
                 transactionInTotalRef.setValue(transactionInTotal);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 showMessage("Error", "Failed to read data.");
             }
         });

         // Fetch the current value of TransactionOutTotal and update it
         transactionOutTotalRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 long transactionOutTotal = dataSnapshot.exists() ? dataSnapshot.getValue(Long.class) : 0;
                 transactionOutTotal += transType.equals("Transaction Out") ? Long.parseLong(transAmount) : 0;

                 // Update TransactionOutTotal node with new value
                 transactionOutTotalRef.setValue(transactionOutTotal);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 showMessage("Error", "Failed to read data.");
             }
         });

         // Save TransactionDone
         Transaction.TransactionDone transactionDone = new Transaction.TransactionDone(transType, transDate, transAmount, transPurpose, transDescription);
         transactionRef.setValue(transactionDone)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         // Data successfully saved
                         showMessage("Success", "New Transaction saved");
                         resetForm();
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         // Error saving data
                         showMessage("Failed", "Failed to save transaction there was an error");
                     }
                 });
     }





    //

    // create transaction helper

    private static class TransactionDone {
        private  String transactionType;
        private  String transactionDate;
        private  String transactionAmount;
        private  String transactionPurpose;
        private String transactionDescription;

        public TransactionDone(String transactionType, String transactionDate, String transactionAmount, String transactionPurpose, String transactionDescription) {
            this.transactionType = transactionType;
            this.transactionDate = transactionDate;
            this.transactionAmount = transactionAmount;
            this.transactionPurpose = transactionPurpose;
            this.transactionDescription = transactionDescription;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public String getTransactionAmount() {
            return transactionAmount;
        }

        public String getTransactionPurpose() {
            return transactionPurpose;
        }

        public String getTransactionDescription() {
            return transactionDescription;
        }




    }

    // get accumulative amount
    public  void getAllTransactionTotals(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference accountRef = database.getReference("Account");

// Retrieve TransactionInTotal value from Firebase and set it to the TextView
        accountRef.child("TransactionInTotal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long transactionInTotal = dataSnapshot.getValue(Long.class);
                if (transactionInTotal != null) {
                    amountIn.setText(String.valueOf(transactionInTotal));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage("Error", "Failed to read TransactionInTotal value.");
            }
        });

// Retrieve TransactionOutTotal value from Firebase and set it to the TextView
        accountRef.child("TransactionOutTotal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long transactionOutTotal = dataSnapshot.getValue(Long.class);
                if (transactionOutTotal != null) {
                    amountOut.setText(String.valueOf(transactionOutTotal));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage("Error", "Failed to read TransactionOutTotal value.");
            }
        });
    }

    // form reset
    public void resetForm(){
        amount.setText("");
        description.setText("");
        pickDate.setText("");
        selectPurpose.setSelection(0);
        transactionTypeState = 0;
        typeOfTransaction = "Not selected";
        saveTransaction.setBackgroundTintList(ContextCompat.getColorStateList(Transaction.this, R.color.blue));
    }

}