package com.example.memolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initMemoList();
        initSettingsBtn();
        initSettings();
        initSortByClick();
        initOrderByClick();
    }

    private void initMemoList() {
        ImageButton ibMemo = findViewById(R.id.ibMemoList);
        ibMemo.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
    private void initSettingsBtn() {
        ImageButton ibSettings = findViewById(R.id.ibSettings);
        ibSettings.setEnabled(false);
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MyMemoListPreferences",
                MODE_PRIVATE).getString("sortfield", "memotitle");
        String sortOrder = getSharedPreferences("MyMemoListPreferences",
                MODE_PRIVATE).getString("sortorder", "ASC");

        RadioButton rbTitle = findViewById(R.id.rbTitle);
        RadioButton rbDate = findViewById(R.id.rbDate);
        RadioButton rbPriority = findViewById(R.id.rbPriority);

        if (sortBy.equalsIgnoreCase("memotitle")) {
            rbTitle.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("date")) {
            rbDate.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);
        }

        RadioButton rbAsc = findViewById(R.id.rbASC);
        RadioButton rbDesc = findViewById(R.id.rbDESC);

        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAsc.setChecked(true);
        } else {
            rbDesc.setChecked(true);
        }
    }

    private void initSortByClick() {
        RadioGroup rgSort = findViewById(R.id.rgSort);
        rgSort.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbTitle = findViewById(R.id.rbTitle);
            RadioButton rbDate = findViewById(R.id.rbDate);
            if (rbTitle.isChecked()){
                getSharedPreferences("MyMemoListPreferences", MODE_PRIVATE)
                        .edit().putString("sortfield", "memotitle").apply();
            } else if (rbDate.isChecked()) {
                getSharedPreferences("MyMemoListPreferences", MODE_PRIVATE)
                        .edit().putString("sortfield", "date").apply();
            } else {
                getSharedPreferences("MyMemoListPreferences", MODE_PRIVATE)
                        .edit().putString("sortfield", "priorityselection").apply();
            }
        });
    }

    private void initOrderByClick() {
        RadioGroup rgSort = findViewById(R.id.rgOrder);
        rgSort.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbAsc = findViewById(R.id.rbASC);
            if (rbAsc.isChecked()) {
                getSharedPreferences("MyMemoListPreferences", MODE_PRIVATE)
                        .edit().putString("sortorder", "ASC").apply();
            } else {
                getSharedPreferences("MyMemoListPreferences", MODE_PRIVATE)
                        .edit().putString("sortorder", "DESC").apply();
            }
        });
    }

}