package com.example.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bankapp.databinding.ActivityCurrencyBinding;
import com.example.bankapp.databinding.ActivityLocatorBinding;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Locator extends AppCompatActivity {

    ActivityLocatorBinding binding;

    private List<String> city= Arrays.asList(
            "MUMBAI","NEW DELHI","KOLKATA"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_locator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityLocatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initComponents();

        binding.backLoc.setOnClickListener(v -> {
            Intent intent = new Intent(Locator.this, HomeScreen.class);
            startActivity(intent);
        });

        binding.search.setOnClickListener(v->{
            String inputCity = binding.cities.getSelectedItem().toString();
            api_locator(inputCity);
        });
    }
    private void initComponents(){
        Collections.sort(city);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,city);
        binding.cities.setAdapter(arrayAdapter);
    }
    public void api_locator(String city){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BankApiService apiService = retrofit.create(BankApiService.class);
        apiService.getBanks(city).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder banksInfo = new StringBuilder();
                    for (Bank bank : response.body()) {
                        banksInfo.append(bank.getAddress()).append("\n\n");
                    }
                    binding.searchResult.setText(banksInfo.toString());
                } else {
                    binding.searchResult.setText("No banks found.");
                }
            }

            @Override
            public void onFailure(Call<List<Bank>> call, Throwable t) {
                binding.searchResult.setText("Error: " + t.getMessage());
            }
        });
    }
}
interface BankApiService {
    @GET("getBanks")
    Call<List<Bank>> getBanks(@Query("city") String city);
}
class Bank {
    private String address;
    public String getAddress() {
        return address;
    }
}
