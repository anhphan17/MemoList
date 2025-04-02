package com.example.memolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    private ArrayList<Memo> filteredMemoList = new ArrayList<>(memoList);

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

    // Search function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.searchView) {
            SearchView searchView = (SearchView) item.getActionView();
            assert searchView != null;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Handle search query submission
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Handle text change in search view
                    filterMemos(newText);
                    return false;
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterMemos(String query) {
        filteredMemoList.clear();
        if (query.isEmpty()) {
            filteredMemoList.addAll(memoList);
        } else {
            for (Memo memo : memoList) {
                if (memo.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredMemoList.add(memo);
                }
            }
        }
        // Update the RecyclerView adapter with the filtered list
        memoAdapter.updateMemos(filteredMemoList);
    }

}