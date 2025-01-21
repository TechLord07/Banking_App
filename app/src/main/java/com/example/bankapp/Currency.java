package com.example.bankapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bankapp.databinding.ActivityCurrencyBinding;
import com.example.bankapp.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Currency extends AppCompatActivity {

    ActivityCurrencyBinding binding;

    private List<String> units= Arrays.asList(
            "AUD","EUR","INR","USD"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_currency);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityCurrencyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initComponents();

        binding.backCon.setOnClickListener(v -> {
            Intent intent = new Intent(Currency.this, HomeScreen.class);
            startActivity(intent);
        });

        binding.convert.setOnClickListener(v -> {
            String inputValue = binding.input.getText().toString().trim();
            if (inputValue.isEmpty()) {
                Toast.makeText(this, "Please enter a value to convert", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double value = Double.parseDouble(inputValue);
                String baseCurrency = binding.baseCur.getSelectedItem().toString();
                String targetCurrency = binding.targetCur.getSelectedItem().toString();
                api_convertor(baseCurrency, targetCurrency, value);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid input value", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void initComponents(){
        Collections.sort(units);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,units);
        binding.baseCur.setAdapter(arrayAdapter);
        binding.targetCur.setAdapter(arrayAdapter);
    }
    public void api_convertor(String baseCurrency, String targetCurrency, Double value){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/WoXy-Sensei/currency-api/main/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrencyApiService apiService = retrofit.create(CurrencyApiService.class);
        String apiPath = baseCurrency + "_" + targetCurrency + ".json";

        apiService.getExchangeRates(apiPath).enqueue(new Callback<CurrencyResponse>() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<CurrencyResponse> call, @NonNull Response<CurrencyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CurrencyResponse currencyResponse = response.body();

                    if (currencyResponse.getRate() != null) {
                        double convertedValue = value / currencyResponse.getRate();
                        binding.output.setText(String.format("%.2f", convertedValue));
                    } else {
                        binding.output.setText("Exchange rate unavailable.");
                    }
                } else {
                    binding.output.setText("Failed to fetch exchange rate. Response Code: " + response.code());
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call<CurrencyResponse> call, @NonNull Throwable t) {
                binding.output.setText("Error occurred: " + t.getMessage());
            }
        });
}

interface CurrencyApiService {
    @GET
    Call<CurrencyResponse> getExchangeRates(@retrofit2.http.Url String url);
}

static class CurrencyResponse {
    private String fromCurrency;
    private String toCurrency;
    private Double rate;
    private long lastUpdate;

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public Double getRate() {
        return rate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}

}
