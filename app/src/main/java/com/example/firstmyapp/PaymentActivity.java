package com.example.firstmyapp;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class PaymentActivity extends AppCompatActivity {
    EditText etCardNumber, etCardExpiry, etCardCVV;
    Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        etCardNumber = findViewById(R.id.etCardNumber);
        etCardExpiry = findViewById(R.id.etCardExpiry);
        etCardCVV = findViewById(R.id.etCardCVV);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        btnConfirmPayment.setOnClickListener(v -> {
            String cardNumber = etCardNumber.getText().toString().trim();
            String cardExpiry = etCardExpiry.getText().toString().trim();
            String cardCVV = etCardCVV.getText().toString().trim();
            if (cardNumber.isEmpty() || cardExpiry.isEmpty() || cardCVV.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isValidCard(cardNumber, cardExpiry, cardCVV)) {
                Toast.makeText(PaymentActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(PaymentActivity.this, "Payment failed. Invalid card details", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isValidCard(String cardNumber, String cardExpiry, String cardCVV){
        return cardNumber.length() == 16 && cardExpiry.length() == 5 && cardCVV.length() == 3;
    }
}
