package com.example.firstmyapp;


import android.annotation.SuppressLint;
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
        btnOrder.setOnClickListener(v -> {
            String description = etDescription.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            ArrayList<Integer> itemIds = new ArrayList<>();

            for (int i = 0; i < llItems.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) llItems.getChildAt(i);
                if (checkBox.isChecked()) {
                    itemIds.add((Integer) checkBox.getTag());
                }
            }
            if (description.isEmpty() || address.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            int[] itemIdsArray = new int[itemIds.size()];
            for (int i = 0; i < itemIds.size(); i++) {
                itemIdsArray[i] = itemIds.get(i);
            }
            boolean isInserted = myDb.insertOrder(description, address, userId, itemIdsArray);
            if (isInserted) {
                Toast.makeText(MainActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                loadOrder();
            } else {
                Toast.makeText(MainActivity.this, "Order placement failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadOrder() {
        Cursor cursor = myDb.getOrdersByUser(userId);
        String[] form = new String[]{"DESCRIPTION", "ADDRESS"};
        int[] to = new int[]{R.id.tvOrderDescription, R.id.tvOrderAddress};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.order_item, cursor, form, to, 0);
        lvOrders.setAdapter(adapter);
    }
    }
