package com.example.myapplication;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CreatePurpose extends AppCompatActivity {
Spinner transaction_Category;
Button savePurpose;
    private RecyclerView recyclerView;
    ArrayList<Record> list;
    PurposeAdapter purposeAdapter;
    DatabaseReference databaseReference;

    EditText txtPurpose,txtPurposeCode,txtDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purpose);

        transaction_Category = findViewById(R.id.transaction_item_category);
        savePurpose = findViewById(R.id.btn_save_transaction_item);
        txtPurpose = findViewById(R.id.txt_transaction_item_name);
        txtDescription = findViewById(R.id.txt_transaction_item_description);
        txtPurposeCode = findViewById(R.id.txt_transaction_item_code);
        recyclerView = findViewById(R.id.recycler_view_records);


          /// call all methods here


        selectPurposeForDb();

        // add array item to category items
        List<String> itemList = new ArrayList<>();
        itemList.add("Select Type");
        itemList.add("Transaction IN");
        itemList.add("Transaction OUT");
        itemList.add("On HOLD");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatePurpose.this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transaction_Category.setAdapter(adapter);

        // get the selected value from the spinner
        transaction_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedText =  itemList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showMessage("Error","Please select type");

            }
        });
        // end of select picking


        // insert purpose to db
        // Write a message to the database
        savePurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction_Category.getSelectedItemPosition() == 0 || txtDescription.getText().toString().trim().isEmpty() || txtPurposeCode.getText().toString().trim().isEmpty() || txtPurpose.getText().toString().trim().isEmpty()) {
                   showMessage("Error", "Please select purpose , Description or Code");
                } else {
                    savePurposeToDatabase();
                }
            }
        });

        // uppercase text or name
        txtPurpose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtPurpose.removeTextChangedListener(this);
                String newUppercase = s.toString().toUpperCase();
                txtPurpose.setText(newUppercase);
                txtPurpose.setSelection(newUppercase.length());
                txtPurpose.addTextChangedListener(this);
            }
        });
        // end of the uppercase

        // end of the main class
    }
// methode to save to db
    private void savePurposeToDatabase() {
        String purpose = txtPurpose.getText().toString();
        String description = txtDescription.getText().toString();
        String code = txtPurposeCode.getText().toString();
        String type = transaction_Category.getSelectedItem().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Purpose").child(code); // Change here

        myRef.setValue(new Purpose(purpose, code, type, description))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data successfully saved
                        showMessage("Success","New Purpose saved");
                        resetForm();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error saving data
                        showMessage("Failed", "Failed to save purpose there was an error");
                    }
                });
    }

    // create purpose class to get data
    public static class Purpose {
        private String purpose;
        private String code;
        private String type;
        private String description;

        public Purpose(String purpose, String code, String type, String description) {
            this.purpose = purpose;
            this.code = code;
            this.type = type;
            this.description = description;
        }
        public String getPurpose() {
            return purpose;
        }

        public String getCode() {
            return code;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }
    }

    // pop up message
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
    public void selectPurposeForDb(){
        list = new ArrayList<Record>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        purposeAdapter = new PurposeAdapter(this,list);
        recyclerView.setAdapter(purposeAdapter);

        // Retrieve the reference to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Purpose");

// Attach a listener for retrieving data
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the records
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve the record data
                    String purpose = snapshot.child("purpose").getValue(String.class);
                    String code = snapshot.child("code").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);

                    // Create a record object and add it to the list
                    Record record = new Record(purpose, code, type, description);
                    // Add the record to your list or adapter to display it in the UI
                    list.add(record);

                }
                purposeAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

    }

    public class Record {
        public String getPurpose() {
            return purpose;
        }

        public String getCode() {
            return code;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

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

        // Add getter and setter methods for each field
        // Getter and setter methods...
    }

    // form reset
    public void resetForm(){
        txtPurpose.setText("");
        txtDescription.setText("");
        txtPurposeCode.setText("");
        transaction_Category.setSelection(0);
    }


}