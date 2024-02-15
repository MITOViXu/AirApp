package com.example.airapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.airapp.ApiClient;
import com.example.airapp.LogIn.InterfaceWeather;
import com.example.airapp.R;
import com.example.airapp.RepondWeather;
import com.example.airapp.URL;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainDashboard extends AppCompatActivity {
    public class WeatherSuperIdol {
        @SerializedName("main")
        private String main;

        @SerializedName("description")
        private String description;

        public String getMainWeatherSuperIdol() {
            return main;
        }

        public void setMainWeatherSuperIdol(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
    InterfaceWeather interfaceWeather;
    TextView textView, huminityTxt, speed, condition, today, user;
    Button mapBtn;
    String access_token;
    private static final String BASE_URL = "https://uiot.ixxc.dev/api/master/";
    private static final String assetId = "4EqQeQ0L4YNWNNTzvTOqjy";

    private static String authorization = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        textView = findViewById(R.id.textView19);
        huminityTxt = findViewById(R.id.huminityText);
        speed = findViewById(R.id.windTxt);
        condition = findViewById(R.id.conditionTxt);
        today = findViewById(R.id.textView17);
        user = findViewById(R.id.textView16);
        mapBtn = findViewById(R.id.mapBtn);
        //access_token
        Intent receivedIntent = getIntent();
        String userName="Tiếng Việt";
        userName = receivedIntent.getStringExtra("username");
        Intent receivedIntent2 = getIntent();
        access_token = receivedIntent2.getStringExtra("access_token");
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainDashboard.this, Map.class);
                intent.putExtra("access_token", access_token);
                startActivity(intent);
            }
        });




        authorization = "Bearer "+access_token;
        user.setText(userName);
        Retrofit retrofit = ApiClient.getClient(URL.mainURL);
        interfaceWeather = retrofit.create(InterfaceWeather.class);
        Call<RepondWeather> call = interfaceWeather.getWeather(assetId, authorization);
        call.enqueue(new Callback<RepondWeather>() {
            @Override
            public void onResponse(Call<RepondWeather> call, Response<RepondWeather> response) {
                if (response.isSuccessful()) {
                    RepondWeather repondWeather = response.body();
                    if (repondWeather != null && repondWeather.getAttributeWeather() != null
                            && repondWeather.getAttributeWeather().getDataWeather() != null
                            && repondWeather.getAttributeWeather().getDataWeather().getValueWeather() != null
                            && repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getMainWeather() != null) {

                        // Lấy giá trị nhiệt độ và độ ẩm từ phản hồi
                        double temperature = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getMainWeather().getTemperature();
                        int humidity = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getMainWeather().getHumidity();
                        double wind = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getWindSuperIdol().getSpeed();
                        RepondWeather.AttributeWeather.DataWeather.ValueWeather.WeatherSuperIdol firstWeather = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getWeatherSuperIdol().get(0);
                        String description = firstWeather.getDescription();
                        RepondWeather.AttributeWeather.DataWeather.ValueWeather.WeatherSuperIdol todayWeather = repondWeather.getAttributeWeather().getDataWeather().getValueWeather().getWeatherSuperIdol().get(0);
                        String todayWeather2 = firstWeather.getMainWeatherSuperIdol();
                        // Hiển thị giá trị lên TextView
                        textView.setText(String.valueOf(temperature));
                        huminityTxt.setText(String.valueOf(humidity)+"%");
                        speed.setText(String.valueOf(wind)+"m/s");
                        condition.setText(description);
                        today.setText(todayWeather2);
                    }
                }
            }

            @Override
            public void onFailure(Call<RepondWeather> call, Throwable t) {
                Log.d("suscess", "deny");
            }
        });

        ImageButton returnBtn = findViewById(R.id.imageButton);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = new Intent();
        intent.setClass(MainDashboard.this, Graph.class);
        LottieAnimationView cloud = findViewById(R.id.lottieAnimationView);
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}
