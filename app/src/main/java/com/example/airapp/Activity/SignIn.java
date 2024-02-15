package com.example.airapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.airapp.Fragment.MainDashBoardFragment;

import com.example.airapp.ApiClient;
import com.example.airapp.LogIn.InterfaceLogin;
import com.example.airapp.LogIn.Asset;
import com.example.airapp.R;
import com.example.airapp.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignIn extends AppCompatActivity {
    String usr ;
    String email ;
    String pwd ;
    String rePwd ;
    Button btnSignUp,btnSignIn;
    TextView txtToken;
    WebView webViewSignUp;
    InterfaceLogin apiInterface;

    EditText userName;
    EditText password;
    TextView signInTitle;
    Button signInButton;
    TextView forgotPass;
    TextView another;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        Cài đặt ngôn ngữ
        userName = findViewById(R.id.userNameSignIn);
        password = findViewById(R.id.passwordSignIn);

        forgotPass = findViewById(R.id.textView8);
        another = findViewById(R.id.textView9);
        signInButton = findViewById(R.id.buttonLogin);
        Intent receivedIntent = getIntent();
        String selectedLanguage="Tiếng Việt";
        if (receivedIntent != null) {
            selectedLanguage = receivedIntent.getStringExtra("selectedLanguage");
            // Sử dụng giá trị selectedLanguage ở đây theo nhu cầu của bạn.
//            Toast.makeText(getApplicationContext(), "Ngôn ngữ:" + selectedLanguage, Toast.LENGTH_SHORT).show();
            if (selectedLanguage.equals("Tiếng Việt")) {
                userName.setHint(R.string.placeuser_Vi);
                password.setHint(R.string.placepass_Vi);
                forgotPass.setText(R.string.forget_viet);
                another.setText(R.string.another_signIn_vi);
                signInButton.setText(R.string.signin_viet);
            } else if (selectedLanguage.equals("English")) {
                userName.setHint(R.string.placeuser_Eng);
                password.setHint(R.string.placepass_Eng);
                forgotPass.setText(R.string.forget_english);
                another.setText(R.string.another_signIn_Eng);
                signInButton.setText(R.string.signin_english);
            } else if (selectedLanguage.equals("中国")) {
                userName.setHint(R.string.placeuser_Chinese);
                password.setHint(R.string.placepass_Chinese);
                forgotPass.setText(R.string.forget_chinese);
                another.setText(R.string.another_signIn_Chinese);
                signInButton.setText(R.string.signin_chinese);
            }
        }




        ConstraintLayout constraintLayout = findViewById(R.id.sign_in);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        ImageButton button = findViewById(R.id.btnBack_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = new Intent();
        intent.setClass(SignIn.this, MainDashboard.class);
        Button login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });
    }
    private void SignIn(){
        Retrofit retrofit = ApiClient.getClient(URL.mainURL);
        apiInterface = retrofit.create(InterfaceLogin.class);

        usr = userName.getText().toString();
        pwd = password.getText().toString();
        Call<Asset> call = apiInterface.authenticate("openremote", usr, pwd, "password", "minhtoan@gmail.com");
        call.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                if (response.isSuccessful()) {
                    Asset result = response.body();
                    String token = result.getAccessToken();
                    Log.d("Access Token", token);
                    Toast.makeText(SignIn.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    String userName11 = userName.getText().toString();
                    Intent intent = new Intent();
                    intent.setClass(SignIn.this, Fragment.class);
//                    intent.putExtra("username", userName11);
//                    intent.putExtra("access_token", token);
                    // Tạo Intent và đính kèm access_token vào Bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("access_token", token);
                    Bundle bundle2 = new Bundle();
                    bundle.putString("user_name", usr);
                    intent.putExtras(bundle);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignIn.this, "Sai tên tài khoản hoặc mật khẩu.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Asset> call, Throwable t) {
                Log.e("RetrofitError", t.getMessage());
                Toast.makeText(SignIn.this, "Không nhận API", Toast.LENGTH_SHORT).show();
            }
        });


    }
}