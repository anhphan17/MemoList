package com.example.memolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class memoActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener{

    private Memo currentMemo = new Memo();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dateButton();
        toggleButton();
        saveButton();
        initPriority();
        initTextChangedEvents();
        initSettingsBtn();
        initMemoList();

    }

    private void dateButton(){
        Button date = findViewById(R.id.buttonDate);
        date.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            DatePickerDialog datePickerDialog = new DatePickerDialog();
            datePickerDialog.show(fm, "DatePick");
        });
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView date = findViewById(R.id.textViewMemoDate);
        date.setText(DateFormat.format( "MM/dd/yyyy", selectedTime.getTime()));
        currentMemo.setDate(selectedTime);
    }


    private void setForEditing(boolean enabled) {
        EditText editMemoName = findViewById(R.id.editTextMemo);
        EditText editInput = findViewById(R.id.editInput);
        Button buttonDate = findViewById(R.id.buttonDate);
        RadioButton rbL1 = findViewById(R.id.radioButtonL1);
        RadioButton rbL2 = findViewById(R.id.radioButtonL2);
        RadioButton rbL3 = findViewById(R.id.radioButtonL3);
        Button saveBtn = findViewById(R.id.buttonSave);
        Button listBtn = findViewById(R.id.buttonList);
        Button settingsBtn = findViewById(R.id.buttonSettings);


        editMemoName.setEnabled(enabled);
        editInput.setEnabled(enabled);
        buttonDate.setEnabled(enabled);
        rbL1.setEnabled(enabled);
        rbL2.setEnabled(enabled);
        rbL3.setEnabled(enabled);
        saveBtn.setEnabled(enabled);
        listBtn.setEnabled(enabled);
        settingsBtn.setEnabled(enabled);


    }

    private void toggleButton(){
        final ToggleButton toggleBtn = findViewById(R.id.toggleButton);
        setForEditing(false);
        toggleBtn.setOnClickListener(v -> {
            setForEditing(toggleBtn.isChecked());
        });
    }

    private void initTextChangedEvents() {
        final EditText etMemoName = findViewById(R.id.editTextMemo);
        etMemoName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setMemoTitle(etMemoName.getText().toString());
            }
        });

        final EditText etMemoInput = findViewById(R.id.editInput);
        etMemoInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentMemo.setMemoDescription(etMemoInput.getText().toString());
            }
        });

    }




    private void initPriority() {
        RadioGroup priority = findViewById(R.id.rgLevels);
        priority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbL1 = findViewById(R.id.radioButtonL1);
                RadioButton rbL2 = findViewById(R.id.radioButtonL2);
                if(rbL1.isChecked()) {
                    currentMemo.setPrioritySelection("Level 1");
                }
                else if(rbL2.isChecked()){
                    currentMemo.setPrioritySelection("Level 2");
                }
                else {
                    currentMemo.setPrioritySelection("Level 3");
                }
            }
        });
    }

    private void saveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(v -> {
            boolean wasSuccessful;
            MemoDataSource ds = new MemoDataSource(memoActivity.this);

            try {
                ds.open();

                if(currentMemo.getMemoID() == -1) {
                    wasSuccessful = ds.insertMemo(currentMemo);
                    int newId = ds.getLastMemoID();
                    currentMemo.setMemoID(newId);

                }



                else {
                    wasSuccessful = ds.updateMemo(currentMemo);
                }
                ds.close();
            }

            catch (Exception e) {
                wasSuccessful = false;
            }

            if(wasSuccessful){
                ToggleButton editToggle = findViewById(R.id.toggleButton);
                editToggle.toggle();
                setForEditing(false);

            }


        });
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



}