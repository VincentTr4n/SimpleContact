package com.example.vincenttran.testcontactapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExampleAdapter adapter;
    private List<ExampleItem> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);

        fillExampleList();
        setUpRecyclerView();

        ImageView showDialpad = findViewById(R.id.main_dialpad_button);

        showDialpad.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DialpadActivity.class);
            startActivity(intent);
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
        });
    }

    public static void requestPermission(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 0);
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new ExampleAdapter(exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.getFilter().filter("");

        adapter.setClickListener(new ExampleAdapter.ClickListener() {
            @Override
            public void onItemClick(ExampleItem item) {
                Log.i("vincent-tran-log-1", "On Click " + item.toString());
            }

            @Override
            public void onItemLongClick(ExampleItem item) {
                Log.i("vincent-tran-log-1", "Long click " +  item.toString());
            }
        });
    }

    private void fillExampleList() {
//        exampleList = new ArrayList<>();
//        exampleList.add(new ExampleItem("One", "Ten"));
//        exampleList.add(new ExampleItem("Two", "Eleven"));
//        exampleList.add(new ExampleItem("Three", "Twelve"));
//        exampleList.add(new ExampleItem("Four", "Thirteen"));
//        exampleList.add(new ExampleItem("Five", "Fourteen"));
//        exampleList.add(new ExampleItem("Six", "Fifteen"));
//        exampleList.add(new ExampleItem("Seven", "Sixteen"));
//        exampleList.add(new ExampleItem("Eight", "Seventeen"));
//        exampleList.add(new ExampleItem("Nine", "Eighteen"));
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
