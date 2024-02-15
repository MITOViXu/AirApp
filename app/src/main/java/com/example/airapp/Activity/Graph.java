package com.example.airapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.airapp.R;

public class Graph extends AppCompatActivity {
    String[] items = {"Huminity", "Wind","Sun","Water Level","Temperature" };
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> addapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        addapterItems = new ArrayAdapter<String>(this, R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(addapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), "Item: ", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton logout = findViewById(R.id.imageButton2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}