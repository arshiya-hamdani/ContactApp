package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class contactdetails extends AppCompatActivity {
    private TextView nametv, phonetv, emailtv, addedtv, updatedtv;
    private ImageView imageView;
    private String id;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactdetails);

        dbHelper = new DBHelper(this);

        // Get contactId from intent
        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        // Initialize views

        nametv = findViewById(R.id.nametv);
        phonetv = findViewById(R.id.phonetv);
        emailtv = findViewById(R.id.emailtv);
        addedtv = findViewById(R.id.addedtv);
        updatedtv = findViewById(R.id.updatedtv);
        imageView = findViewById(R.id.profileimg);

        // Load contact details
        loaddatabyid();
    }

    private void loaddatabyid() {
        // Query database for contact details using id
        String selectquery = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.C_ID + " = ?";
        SQLiteDatabase mydb = dbHelper.getReadableDatabase();
        Cursor cursor = mydb.rawQuery(selectquery, new String[]{id});
        if (cursor.moveToFirst()) {
            String nm = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.C_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.C_PHONE));
            String em = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.C_EMAIL));
            String at = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.C_ADDED_TIME));
            String ut = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.C_UPDATED_TIME));

            // Format dates
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                Date dateAdded = sdf.parse(at);
                Date dateUpdated = sdf.parse(ut);

                calendar.setTimeInMillis(dateAdded.getTime());
                String ta = String.format("%s", DateFormat.format("dd/MM/yy hh:mm:aa", calendar));


                calendar.setTimeInMillis(dateUpdated.getTime());
                String tu = String.format("%s", DateFormat.format("dd/MM/yy hh:mm:aa", calendar));

                // Set text to TextViews
                nametv.setText(nm);
                phonetv.setText(phone);
                emailtv.setText(em);
                addedtv.setText(ta);
                updatedtv.setText(tu);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
    }
}
