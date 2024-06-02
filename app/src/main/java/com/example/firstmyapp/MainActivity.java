package com.example.firstmyapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText etDescription, etAddress;
    Button btnOrder, btnPay;
    ListView lvOrders;
    LinearLayout llItems;
    String username;
    int userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        etDescription = findViewById(R.id.etDescription);
        etAddress = findViewById(R.id.etAddress);
        btnPay = findViewById(R.id.btnPay);
        lvOrders = findViewById(R.id.LvOrder);
        llItems = findViewById(R.id.llItems);
        btnOrder = findViewById(R.id.btnOrder);

        username = getIntent().getStringExtra("USERNAME");
        userId = myDb.getUserIdByUsername(username);
        btnPay.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }
//    public void loadOrder() {
//        Cursor cursor = myDb.getOrdersByUser(userId);
//        String[] form = new String[]{"DESCRIPTION", "ADDRESS"};
//        int[] to = new int[]{R.id.tvOrderDescription, R.id.tvOrderAddress};
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.order_item, cursor, form, to, 0);
//        lvOrders.setAdapter(adapter);
//    }
    }

