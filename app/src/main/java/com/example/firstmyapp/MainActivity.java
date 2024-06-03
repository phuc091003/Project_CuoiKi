package com.example.firstmyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText etDescription, etAddress;
    Button btnOrder, btnPay;
    ListView lvOrders;
    RecyclerView rvItems;
    LinearLayout llItems;
    String username;
    int userId;
    List<Item> itemList;

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
        rvItems = findViewById(R.id.rvItems);
        llItems = findViewById(R.id.llItems);
        btnOrder = findViewById(R.id.btnOrder);

        username = getIntent().getStringExtra("USERNAME");
        userId = myDb.getUserIdByUsername(username);

        // Khởi tạo danh sách các mặt hàng
        itemList = new ArrayList<>();
        itemList.add(new Item(1, "drink", 10.0));
        itemList.add(new Item(2, "food", 15.0));
        itemList.add(new Item(3, "clothes", 20.0));
        itemList.add(new Item(4, "fastfood", 5.0));

        // Thiết lập RecyclerView và Adapter
        ItemAdapter itemAdapter = new ItemAdapter(itemList);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(itemAdapter);

        btnPay.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }
}
