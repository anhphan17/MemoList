package com.example.memolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initAddMemoButton();
        initMemoList();
        initSettingsBtn();
    }

    private void initAddMemoButton(){
        Button newMemo = findViewById(R.id.buttonAddMemo);
        newMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, memoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initMemoList() {
        ImageButton ibMemo = findViewById(R.id.ibMemoList);
        ibMemo.setEnabled(false);
    }
    private void initSettingsBtn() {
        ImageButton ibSettings = findViewById(R.id.ibSettings);
        ibSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
        });
    }
}