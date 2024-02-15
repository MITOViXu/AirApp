package com.example.airapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airapp.LogIn.InterfaceLogin;
import com.example.airapp.R;

public class MainActivity extends AppCompatActivity {

    // Chỉnh ngôn ngữ
    String[] items = {"Tiếng Việt", "English","中国"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> addapterItems;
    String language="中国";
    TextView intro ;
    TextView another ;
    TextView forget ;
    TextView contact ;
    TextView textView1;
    Button buttonIn;
    Button buttonUp;
    Button buttonContinue;
    ImageView flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String usr = "user";
        String email = "Name16@gmail.com";
        String pwd = "123";
        String rePwd = "123";
        Button btnSignUp,btnSignIn;
        TextView txtToken;
        WebView webViewSignUp;
        InterfaceLogin apiInterface;


        // Chỉnh ngôn ngữ
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoLanguage);
        addapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(addapterItems);
         textView1 = findViewById(R.id.textView4);
         buttonIn = findViewById(R.id.btn_signInMainActivity);
         buttonUp = findViewById(R.id.btn_signupMainActivity);
         buttonContinue = findViewById(R.id.btn_continueMainActivity);
         intro = findViewById(R.id.textView5);
         another = findViewById(R.id.textView6);
         forget = findViewById(R.id.textView3);
         contact = findViewById(R.id.textView12);
         flag = findViewById(R.id.imageFlag);
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SignIn.class);
        intent.putExtra("selectedLanguage", "Tiếng Việt");
        Intent intent2 = new Intent();
        intent2.setClass(MainActivity.this, SignUp.class);
        intent2.putExtra("selectedLanguage", "Tiếng Việt");
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                String selectedLanguage = adapterView.getItemAtPosition(i).toString();

                intent.putExtra("selectedLanguage", selectedLanguage);
                intent2.putExtra("selectedLanguage", selectedLanguage);
                if (selectedLanguage == "Tiếng Việt") {
                    textView1.setText(R.string.welcome_message);
                    buttonIn.setText(R.string.signin_viet);
                    buttonUp.setText(R.string.signup_viet);
                    intro.setText(R.string.title_viet);
                    another.setText(R.string.another_viet);
                    buttonContinue.setText(R.string.continue_viet);
                    forget.setText(R.string.forget_viet);
                    contact.setText(R.string.contact_viet);
                    flag.setImageResource(R.mipmap.vietnam);
                } else if (selectedLanguage == "English") {
                    textView1.setText(R.string.welcome_message_english);
                    buttonIn.setText(R.string.signin_english);
                    buttonUp.setText(R.string.signup_english);
                    intro.setText(R.string.title_english);
                    another.setText(R.string.another_english);
                    buttonContinue.setText(R.string.continue_english);
                    forget.setText(R.string.forget_english);
                    contact.setText(R.string.contact_english);
                    flag.setImageResource(R.drawable.english);
                } else if (selectedLanguage == "中国") {
                    textView1.setText(R.string.welcome_message_chinese);
                    buttonIn.setText(R.string.signin_chinese);
                    buttonUp.setText(R.string.signup_chinese);
                    intro.setText(R.string.title_chinese);
                    another.setText(R.string.another_chinese);
                    buttonContinue.setText(R.string.continue_chinese);
                    forget.setText(R.string.forget_chinese);
                    contact.setText(R.string.contact_chinese);
                    flag.setImageResource(R.drawable.china);
                }
            }
        });

//        Điều chỉnh màu nền chuyển động
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        //        Điều chỉnh hình nền chuyển động
        buttonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);

            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });



    }
}