package com.example.vincenttran.testcontactapp;

import android.Manifest;
import android.app.Activity;
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

public class DialpadActivity extends AppCompatActivity {

    private ExampleAdapter adapter;
    private List<ExampleItem> exampleList;

    private TextView btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6,
            btnNumber7, btnNumber8, btnNumber9, btnAsterisk, btnHashtag;
    private ImageView btnClear;
    private RelativeLayout btnNumber0;
    private EditText txtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialpad);

        requestPermission(DialpadActivity.this, Manifest.permission.READ_CONTACTS);

        fillExampleList();
        setUpRecyclerView();
        initView();
    }

    public static void requestPermission(Activity activity, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, 0);
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_dialpad);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new ExampleAdapter(exampleList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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

    private void initView(){
        btnNumber0  = findViewById(R.id.dialpad_0_holder);
        btnNumber1  = findViewById(R.id.dialpad_1);
        btnNumber2  = findViewById(R.id.dialpad_2);
        btnNumber3  = findViewById(R.id.dialpad_3);
        btnNumber4  = findViewById(R.id.dialpad_4);
        btnNumber5  = findViewById(R.id.dialpad_5);
        btnNumber6  = findViewById(R.id.dialpad_6);
        btnNumber7  = findViewById(R.id.dialpad_7);
        btnNumber8  = findViewById(R.id.dialpad_8);
        btnNumber9  = findViewById(R.id.dialpad_9);
        btnAsterisk = findViewById(R.id.dialpad_asterisk);
        btnHashtag  = findViewById(R.id.dialpad_hashtag);
        btnClear    = findViewById(R.id.dialpad_clear_char);
        txtInput    = findViewById(R.id.dialpad_input);

        txtInput.setShowSoftInputOnFocus(false);

        btnNumber0.setOnClickListener(v -> { keyPress('0', v); });
        btnNumber1.setOnClickListener(v -> { keyPress('1', v); });
        btnNumber2.setOnClickListener(v -> { keyPress('2', v); });
        btnNumber3.setOnClickListener(v -> { keyPress('3', v); });
        btnNumber4.setOnClickListener(v -> { keyPress('4', v); });
        btnNumber5.setOnClickListener(v -> { keyPress('5', v); });
        btnNumber6.setOnClickListener(v -> { keyPress('6', v); });
        btnNumber7.setOnClickListener(v -> { keyPress('7', v); });
        btnNumber8.setOnClickListener(v -> { keyPress('8', v); });
        btnNumber9.setOnClickListener(v -> { keyPress('9', v); });
        btnAsterisk.setOnClickListener(v -> { keyPress('*', v); });
        btnHashtag.setOnClickListener(v -> { keyPress('#', v); });

        btnClear.setOnClickListener(v -> {
            txtInput.dispatchKeyEvent(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL, 0));
        });

        btnClear.setOnLongClickListener(v -> {
            txtInput.setText("");
            return true;
        });

        btnNumber0.setOnLongClickListener(v -> {
            keyPress('+', v);
            return true;
        });

        txtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void keyPress(char c, View view) {
        txtInput.dispatchKeyEvent(new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, getCharKeyCode(c), 0));
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_PRESS);
    }

    private char getCharKeyCode(char c){
        if(c == '0') return KeyEvent.KEYCODE_0;
        if(c == '1') return KeyEvent.KEYCODE_1;
        if(c == '2') return KeyEvent.KEYCODE_2;
        if(c == '3') return KeyEvent.KEYCODE_3;
        if(c == '4') return KeyEvent.KEYCODE_4;
        if(c == '5') return KeyEvent.KEYCODE_5;
        if(c == '6') return KeyEvent.KEYCODE_6;
        if(c == '7') return KeyEvent.KEYCODE_7;
        if(c == '8') return KeyEvent.KEYCODE_8;
        if(c == '9') return KeyEvent.KEYCODE_9;
        if(c == '*') return KeyEvent.KEYCODE_STAR;
        if(c == '+') return KeyEvent.KEYCODE_PLUS;
        return KeyEvent.KEYCODE_POUND;
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
