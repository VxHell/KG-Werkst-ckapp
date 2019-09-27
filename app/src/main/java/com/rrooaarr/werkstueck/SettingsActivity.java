package com.rrooaarr.werkstueck;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_SAVE_REPLY = "com.example.android.savesql.REPLY";

    private WordViewModel2 mainViewModel;

    private EditText server;
    private EditText port;
    private EditText username;
    private EditText passwort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO consider to make viewmodel singelton for instance Dagger
        mainViewModel = new ViewModelProvider(this).get(WordViewModel2.class);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setMainViewModel(mainViewModel);
        initViews(binding.getRoot());

        server = findViewById(R.id.edit_server);
        port = findViewById(R.id.edit_port);
        username = findViewById(R.id.edit_username);
        passwort = findViewById(R.id.edit_password);

    }

    private void initViews(View view) {
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(this);

        final Button button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);
    }

    private void onSave(){
        Intent replyIntent = new Intent();

        if (TextUtils.isEmpty(server.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_server,
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(port.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_port,
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(username.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_username,
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(passwort.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_password,
                    Toast.LENGTH_LONG).show();
        } else {
            String word = server.getText().toString();
            replyIntent.putExtra(EXTRA_SAVE_REPLY, word);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    private void onCancel(){
        Intent replyIntent = new Intent();
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save:
                onSave();
                break;
            case R.id.button_cancel:
                onCancel();
                break;
        }
    }
}
