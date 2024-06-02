package com.example.firstmyapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText etDescription, etAddress;
    Button btnOrder,btnPay;
    ListView lvOrders;
    String username;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        etDescription = findViewById(R.id.etDescription);
        etAddress = findViewById(R.id.btnOrder);
        btnPay = findViewById(R.id.btnPay);
        lvOrders = findViewById(R.id.lvOrder);

        username = getIntent().getStringExtra("USERNAME");
        userId = myDb.getUserIdByUsername(username);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                if (description.isEmpty() || address.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isInserted = myDb.insertOrder(description, address, userId);
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    loadOrder();
                } else {
                    Toast.makeText(MainActivity.this, "Order placement failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Payment functionally is not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
        loadOrder();
    }
    private void loadOrder(){
        Cursor cursor = myDb.getOrdersByUser(userId);
        String[] form = new String[]{"DESCRIPTION", "ADDRESS"};
        int[] to = new int[]{R.id.tvOrderDescription, R.id.tvOrderAddress};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.order_item, cursor, form, to, 0);
        lvOrders.setAdapter(adapter);
    }
}