package com.rrooaarr.werkstueck.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_SAVE_REPLY = "com.example.android.savesql.REPLY";

    private UserSettingsViewModel settingsViewModel;

    private EditText server;
    private EditText port;
    private EditText username;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO consider to make viewmodel singelton for instance Dagger
        settingsViewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsViewModel.loadSetting();
        // Specify the current activity as the lifecycle owner.
//        binding.setLifecycleOwner(this);

        binding.setSettingsViewModel(settingsViewModel);
        initViews(binding.getRoot());
        server = findViewById(R.id.edit_server);
        port = findViewById(R.id.edit_port);
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);

        settingsViewModel.getSetting().observe(this, new Observer<UserSetting>() {
            @Override
            public void onChanged(UserSetting setting) {
                server.setText(setting != null ? setting.getServer() : null);
                port.setText(setting != null ? setting.getPort() : null);
                username.setText(setting != null ? setting.getUsername() : null);
                password.setText(setting != null ? setting.getPassword() : null);
            }
        });

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
        } else if (TextUtils.isEmpty(password.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_password,
                    Toast.LENGTH_LONG).show();
        } else {
            String mServer = server.getText().toString();
            String mPort = port.getText().toString();
            String mUsername = username.getText().toString();
            String mPasswort = password.getText().toString();

            UserSetting setting = new UserSetting(mServer, mPort, mUsername, mPasswort);
            settingsViewModel.update(setting);

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