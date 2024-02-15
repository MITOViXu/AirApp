package com.example.airapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.graphics.Color;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.example.airapp.LogIn.LoadingAlert;
import com.example.airapp.R;

public class SignUp extends AppCompatActivity {
    String usr ;
    String email ;
    String pwd ;
    String rePwd ;
    Button btnSignUp,btnSignIn;
    TextView txtToken;
    WebView webViewSignUp;
    String selectedLanguage="Tiếng Việt";
    LoadingAlert loading;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ImageButton button = findViewById(R.id.btn_back_register);
        webViewSignUp = findViewById(R.id.webView);
        webViewSignUp.setVisibility(View.INVISIBLE);
        cardView = findViewById(R.id.cardView);

        ConstraintLayout constraintLayout = findViewById(R.id.sign_up);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        loading = new LoadingAlert(SignUp.this);

        TextView emailTitle = findViewById(R.id.emailTitleSignUp);
        TextView userTitle = findViewById(R.id.userTitleSignUp);
        TextView passTitle = findViewById(R.id.passTitleSignUp);
        TextView repassTitle = findViewById(R.id.rePassTitleSignUp);
        EditText userName = findViewById(R.id.userNameSignUp);
        EditText passWord = findViewById(R.id.passwordSignUp);
        EditText email1 = findViewById(R.id.emailSignUp);
        EditText rePwd1 = findViewById(R.id.confirmPassWord);
        Button button1 = findViewById(R.id.btnSignUp);

        Intent receivedIntent = getIntent();

        webViewSignUp.clearCache(true);
        webViewSignUp.clearHistory();
        webViewSignUp.clearFormData();
        webViewSignUp.loadUrl("about:blank");

        // Clear cookies
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);

        // Flush the changes to persist them
        cookieManager.flush();

        // Clear DOM storage
        webViewSignUp.getSettings().setDomStorageEnabled(true);
        if (receivedIntent != null) {
            selectedLanguage = receivedIntent.getStringExtra("selectedLanguage");
            // Sử dụng giá trị selectedLanguage ở đây theo nhu cầu của bạn.
//            Toast.makeText(getApplicationContext(), "Ngôn ngữ:" + selectedLanguage, Toast.LENGTH_SHORT).show();
            if (selectedLanguage.equals("Tiếng Việt")) {
                emailTitle.setText(R.string.email_title_Vi);
                userTitle.setText(R.string.userName_title_Vi);
                passTitle.setText(R.string.pass_title_Vi);
                repassTitle.setText(R.string.rePass_title_Vi);
                userName.setHint(R.string.placeuser_Vi);
                email1.setHint(R.string.email_title_Vi);
                passWord.setHint(R.string.placepass_Vi);
                rePwd1.setHint(R.string.placepass_Vi);
                button1.setText(R.string.btn_SignUp_Vi);
            } else if (selectedLanguage.equals("English")) {
                emailTitle.setText(R.string.email_title_En);
                userTitle.setText(R.string.userName_title_En);
                passTitle.setText(R.string.pass_title_En);
                repassTitle.setText(R.string.rePass_title_En);
                userName.setHint(R.string.placeuser_Eng);
                email1.setHint(R.string.email_title_En);
                passWord.setHint(R.string.placepass_Eng);
                rePwd1.setHint(R.string.placepass_Eng);
                button1.setText(R.string.btn_SignUp_En);
            } else if (selectedLanguage.equals("中国")) {
                emailTitle.setText(R.string.email_title_Chinese);
                userTitle.setText(R.string.userName_title_Chinese);
                passTitle.setText(R.string.pass_title_Chinese);
                repassTitle.setText(R.string.rePass_title_Chinese);
                userName.setHint(R.string.placeuser_Chinese);
                email1.setHint(R.string.email_title_Chinese);
                passWord.setHint(R.string.placepass_Chinese);
                rePwd1.setHint(R.string.placepass_Chinese);
                button1.setText(R.string.btn_SignUp_Chinese);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                cardView.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutWebView();
//                #DAFFFFFF
                int color = Color.parseColor("#B7123147");
                cardView.setBackgroundColor(color);
                SignUp();
            }
        });


    }
    private void logoutWebView() {
        webViewSignUp.clearCache(true);
        webViewSignUp.loadUrl("about:blank");
    }
    private void SignUp(){
        webViewSignUp.getSettings().setJavaScriptEnabled(true);
        webViewSignUp.clearCache(true);
        webViewSignUp.clearHistory();
        webViewSignUp.clearFormData();
        loading.StartAlertDialog();
        webViewSignUp.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("SignUp", "URL: " + url);
                if (url.contains("uiot.ixxc.dev/manager/")) {
                    loading.CloseAlertDialog();
                    // Handle successful registration and navigate to a new activity
                    Toast.makeText(SignUp.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    intent.putExtra("selectedLanguage", selectedLanguage);
                    cardView.setBackgroundColor(Color.TRANSPARENT);
                    startActivity(intent);
                }else{
                    cardView.setBackgroundColor(Color.TRANSPARENT);
                    loading.CloseAlertDialog();
                    Toast.makeText(SignUp.this, "Tên người dùng hoăc email đã tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if(url.contains("openid-connect/registrations")){

                    EditText userName = findViewById(R.id.userNameSignUp);
                    EditText passWord = findViewById(R.id.passwordSignUp);
                    EditText email1 = findViewById(R.id.emailSignUp);
                    EditText rePwd1 = findViewById(R.id.confirmPassWord);

                    usr = userName.getText().toString();
                    email = email1.getText().toString();
                    pwd = passWord.getText().toString();
                    rePwd = rePwd1.getText().toString();


                    String usrScript =  "document.getElementById('username').value ='" + usr +"';";
                    String emailScript =  "document.getElementById('email').value ='" + email +"';";
                    String pwdScript =  "document.getElementById('password').value ='" + pwd +"';";
                    String rePwdScript =  "document.getElementById('password-confirm').value ='" + rePwd +"';";
                    String submitFormScript = "document.querySelector('form').submit();";

                    webViewSignUp.evaluateJavascript(usrScript, null);
                    webViewSignUp.evaluateJavascript(emailScript, null);
                    webViewSignUp.evaluateJavascript(pwdScript, null);
                    webViewSignUp.evaluateJavascript(rePwdScript, null);
                    webViewSignUp.evaluateJavascript(submitFormScript,null);

                }
            }
        });
        String url = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/registrations?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&response_mode=fragment&response_type=code&scope=openid";
        webViewSignUp.loadUrl(url);
    }
}