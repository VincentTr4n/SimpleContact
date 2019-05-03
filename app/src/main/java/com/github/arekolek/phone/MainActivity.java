package com.github.arekolek.phone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private ExampleAdapter adapter;
    private List<Contact> exampleList;

    private static final int REQUEST_APP_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        requestPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);

        init();

    }

    private void init(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestAppPermission();
            }
            else {
                fillExampleList();
                setUpRecyclerView();

                ImageView showDialpad = findViewById(R.id.main_dialpad_button);

                showDialpad.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, DialpadActivity.class);
                    startActivity(intent);
                    v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
                });
            }
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestAppPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS,
                             Manifest.permission.WRITE_CONTACTS, }, REQUEST_APP_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_APP_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new ExampleAdapter(exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //adapter.getFilter().filter("");

        adapter.setClickListener(new ExampleAdapter.ClickListener() {
            @Override
            public void onItemClick(Contact item) {
                Log.i("vincent-tran-log-1", "On Click " + item.toString());
            }

            @Override
            public void onItemLongClick(Contact item) {
                Log.i("vincent-tran-log-1", "Long click " +  item.toString());
            }
        });
    }

    private void fillExampleList() {
        ContactUtils utils = new ContactUtils(this);
        exampleList = utils.getAllContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.example_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("vincent-tran-log", newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
