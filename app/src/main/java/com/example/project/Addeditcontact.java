package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Addeditcontact extends AppCompatActivity {
    FloatingActionButton fab;
    EditText namet, phonet, emailt;
    ActionBar actionBar;
    DBHelper dbHelper;
    String contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addeditcontact);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        dbHelper = new DBHelper(this);

        namet = findViewById(R.id.name);
        phonet = findViewById(R.id.phone);
        emailt = findViewById(R.id.email);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedata();
            }
        });

        Intent intent = getIntent();
        contactId = intent.getStringExtra("contactId");
        if (contactId != null) {
            if (actionBar != null) {
                actionBar.setTitle("Edit contact");
            }
            populateFields(contactId);
        } else {
            if (actionBar != null) {
                actionBar.setTitle("Add contact");
            }
        }
    }

    private void populateFields(String contactId) {
        modelclass mc = dbHelper.getContactById(contactId);
        if (mc != null) {
            namet.setText(mc.getName());
            phonet.setText(mc.getPhone());
            emailt.setText(mc.getEmail());
        }
    }

    private void savedata() {
        String name = namet.getText().toString();
        String phone = phonet.getText().toString();
        String email = emailt.getText().toString();

        if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            long result;
            if (contactId == null) {
                result = dbHelper.insertData(name, phone, email);
            } else {
                result = dbHelper.updateData(contactId, name, phone, email);
            }

            if (result != -1) {
                Toast.makeText(this, "Contact saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save contact", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
