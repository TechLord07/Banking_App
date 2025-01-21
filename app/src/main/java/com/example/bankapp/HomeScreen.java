package com.example.bankapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeScreen extends AppCompatActivity {
    Button back_wel,loan_pg,cc_pg,loc_pg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back_wel=(Button)findViewById(R.id.back_wel);
        back_wel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
        loan_pg=(Button)findViewById(R.id.loan_pg);
        loan_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeScreen.this, Loan.class);
                startActivity(intent);
            }
        });
        cc_pg=(Button)findViewById(R.id.cc_pg);
        cc_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeScreen.this, Currency.class);
                startActivity(intent);
            }
        });
        loc_pg=(Button)findViewById(R.id.loc_pg);
        loc_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeScreen.this, Locator.class);
                startActivity(intent);
            }
        });
    }
}