package com.example.airapp.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainDashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainDashBoardFragment extends Fragment {
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
    String username="";
    private static final String BASE_URL = "https://uiot.ixxc.dev/api/master/";
    private static final String assetId = "4EqQeQ0L4YNWNNTzvTOqjy";

    private static String authorization = "";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String accessToken="";
    String userName="Tiếng Việt";

    public MainDashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainDashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainDashBoardFragment newInstance(String accessToken, String userName) {
        MainDashBoardFragment fragment = new MainDashBoardFragment();
        Bundle args = new Bundle();
        args.putString("access_token", accessToken);
        args.putString("user_name", userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("Hello", "Fragment MainDash");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Lấy access_token từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            accessToken = bundle.getString("access_token");
            // Sử dụng access_token theo cách bạn cần
            Log.d("MainDashBoardFragment", "Received access_token: " + accessToken);
            access_token=accessToken;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Lấy access_token từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String accessToken = bundle.getString("access_token");
            username = bundle.getString("user_name");
            // Sử dụng access_token theo cách bạn cần
            Log.d("MainDashBoardFragment", "Received access_token: " + accessToken);
            access_token=accessToken;
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_dash_board, container, false);

        // Sử dụng findViewById từ view để tìm các phần tử trong layout của fragment
        textView = view.findViewById(R.id.textView19);
        huminityTxt = view.findViewById(R.id.huminityText);
        speed = view.findViewById(R.id.windTxt);
        condition = view.findViewById(R.id.conditionTxt);
        today = view.findViewById(R.id.textView17);
        user = view.findViewById(R.id.textView16);


        authorization = "Bearer "+access_token;
        user.setText(username);
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

        return view;

    }
}