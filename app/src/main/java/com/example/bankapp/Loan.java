package com.example.bankapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class Loan extends AppCompatActivity {

    Button back_home,loan_calc;
    EditText principal,rate,time;
    TextView amt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back_home=(Button)findViewById(R.id.back_home);
        loan_calc=(Button)findViewById((R.id.loan_calc));
        principal=(EditText)findViewById(R.id.principal);
        rate=(EditText)findViewById(R.id.rate);
        time=(EditText)findViewById(R.id.time);
        amt=(TextView)findViewById(R.id.amt);
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Loan.this, HomeScreen.class);
                startActivity(intent);
            }
        });
        loan_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText(v);
                calculate_loan(v);
            }
        });
    }

    public void updateText(View view){
        principal.setText(principal.getText());
        rate.setText(rate.getText());
        time.setText(time.getText());
    }
    public void calculate_loan(View view){
        int prin_val=Integer.parseInt(String.valueOf(principal.getText()));
        double rate_val=Double.parseDouble(String.valueOf((rate.getText())));
        double time_val=Double.parseDouble((String.valueOf(time.getText())));
        double amount=prin_val+(prin_val*rate_val*time_val)/100;
        amt.setText("Amount to be repaid: "+ amount);
    }
}