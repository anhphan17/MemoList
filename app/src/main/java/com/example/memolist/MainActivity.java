package com.example.memolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

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

        }
        catch (Exception e) {
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
            if (memos.size() > 0) {
                RecyclerView memoList = findViewById(R.id.rvMemos);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                memoList.setLayoutManager(layoutManager);
                memoAdapter = new MemoAdapter(memos, this);
                memoList.setAdapter(memoAdapter);

                memoAdapter.setOnItemClickListener(onItemClickListener);
            }
            else {
                Intent intent = new Intent(MainActivity.this, memoActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
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
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
        });
    }





}