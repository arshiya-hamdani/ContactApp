package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView contactrv;
    private DBHelper dbHelper;
    private Adaptercontact adaptercontact;
    private ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact App");

        dbHelper = new DBHelper(this);

        fab = findViewById(R.id.fab);
        contactrv = findViewById(R.id.contactrv);
        contactrv.setHasFixedSize(true);
        contactrv.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Addeditcontact.class);
                startActivity(i);
            }
        });

        loaddata();
    }

    private void loaddata() {
        adaptercontact = new Adaptercontact(this, dbHelper.getcontacts());
        contactrv.setAdapter(adaptercontact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaddata();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_menu, menu);
        MenuItem item = menu.findItem(R.id.searchcontact);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchcontact(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchcontact(newText);
                return true;
            }
        });
        return true;
    }

    private void searchcontact(String query) {
        adaptercontact = new Adaptercontact(this, dbHelper.getsearchcontact(query));
        contactrv.setAdapter(adaptercontact);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteall) {
            dbHelper.deleteallcontacts();
            return true;
        } else if (item.getItemId() == R.id.action_sort_name) {
            adaptercontact.sortContactsByName();
            return true;
        } else if (item.getItemId() == R.id.action_sort_phone) {
            adaptercontact.sortContactsByPhone();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
