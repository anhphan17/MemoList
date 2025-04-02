// MainActivity.java
package com.example.memolist;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Memo> memos;
    private MemoAdapter memoAdapter;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int memoID = memos.get(position).getMemoID();
            Intent intent = new Intent(MainActivity.this, memoActivity.class);
            intent.putExtra("memoID", memoID);
            startActivity(intent);
        }
    };

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
        initDeleteSwitch();

        String sortBy = getSharedPreferences("MyMemoListPreferences",
                MODE_PRIVATE).getString("sortfield", "memotitle");
        String sortOrder = getSharedPreferences("MyMemoListPreferences",
                MODE_PRIVATE).getString("sortorder", "ASC");


        MemoDataSource ds = new MemoDataSource(this);

        try {
            ds.open();
            memos = ds.getMemos(sortBy, sortOrder);
            ds.close();
            RecyclerView memoList = findViewById(R.id.rvMemos);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            memoList.setLayoutManager(layoutManager);
            memoAdapter = new MemoAdapter(memos, this);
            memoList.setAdapter(memoAdapter);

            memoAdapter.setOnItemClickListener(onItemClickListener);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            itemTouchHelper.attachToRecyclerView(memoList);

            String searchQuery = getIntent().getStringExtra("searchQuery");
            if (searchQuery != null && !searchQuery.isEmpty()) {
                filterMemos(searchQuery);
            } else {
                clearSearchQuery();
            }


        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MyMemoListPreferences",
                MODE_PRIVATE).getString("sortfield", "memotitle");
        String sortOrder = getSharedPreferences("MyMemoListPreferences",
                MODE_PRIVATE).getString("sortorder", "ASC");



        MemoDataSource ds = new MemoDataSource(this);
        try {
            ds.open();
            memos = ds.getMemos(sortBy, sortOrder);
            ds.close();

            // Ensure adapter is updated with full list first
            memoAdapter.updateMemos(memos);

            // Retrieve search query from preferences
            String searchQuery = getSharedPreferences("MyMemoListPreferences",
                    MODE_PRIVATE).getString("searchQuery", "");

            // Apply filtering if a search query exists
            if (!searchQuery.isEmpty()) {
                filterMemos(searchQuery);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }

    private void initAddMemoButton() {
        Button newMemo = findViewById(R.id.buttonAddMemo);
        newMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, memoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Boolean status = compoundButton.isChecked();
                memoAdapter.setDelete(status);
                memoAdapter.notifyDataSetChanged();
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
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        });
    }

    private void filterMemos(@NonNull String query) {
        Log.d("MainActivity", "Filtering memos with query: " + query);
        if (query.isEmpty()) {
            Log.d("MainActivity", "Query is empty, resetting to original list");
            memoAdapter.updateMemos(memos);
            return;
        }

        ArrayList<Memo> filteredMemoList = new ArrayList<>();
        for (Memo memo : memos) {
            if (memo.getMemoTitle().toLowerCase().contains(query.toLowerCase()) ||
                    memo.getMemoDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredMemoList.add(memo);
            }
        }
        Log.d("MainActivity", "Filtered list size: " + filteredMemoList.size());
        memoAdapter.updateMemos(filteredMemoList);
    }

    private void clearSearchQuery() {
        getSharedPreferences("MyMemoListPreferences", MODE_PRIVATE)
                .edit().remove("searchQuery").apply();
        filterMemos("");
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            memoAdapter.deleteItem(position); // Remove item from adapter
            memoAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                    .addActionIcon(R.drawable.baseline_delete_sweep_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}