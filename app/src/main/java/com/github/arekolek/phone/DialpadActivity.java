package com.github.arekolek.phone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DialpadActivity extends AppCompatActivity {

    private ExampleAdapter adapter;
    private List<Contact> exampleList;

    private static final int REQUEST_APP_CODE = 111;

    private TextView btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6,
            btnNumber7, btnNumber8, btnNumber9, btnAsterisk, btnHashtag;
    private ImageView btnClear, btn_call;
    private RelativeLayout btnNumber0;
    private EditText txtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialpad);

        fillExampleList();
        setUpRecyclerView();
        initView();
    }

    private void makePhoneCall(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if(result == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + txtInput.getText().toString().trim()));
            startActivity(intent);
        }
        else requestAppPermission();
    }

    private void requestAppPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE }, REQUEST_APP_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_APP_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            makePhoneCall();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        btn_call    = findViewById(R.id.dialpad_call_button);

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

        btn_call.setOnClickListener(v -> {
            makePhoneCall();
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
}
