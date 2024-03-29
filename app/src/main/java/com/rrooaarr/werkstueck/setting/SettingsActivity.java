package com.rrooaarr.werkstueck.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.api.errorhandling.ErrorHelper;
import com.rrooaarr.werkstueck.databinding.ActivitySettingsBinding;
import com.rrooaarr.werkstueck.util.StringValidationRules;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private UserSettingsViewModel settingsViewModel;

    private EditText server;
    private EditText port;
    private EditText username;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupBindings();
    }

    private void setupBindings() {
        // TODO consider to make viewmodel singelton for instance Dagger
        settingsViewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setModel(settingsViewModel);

        initViews(binding.getRoot());
    }

    private void initViews(View view) {
        server = findViewById(R.id.edit_server);
        port = findViewById(R.id.edit_port);
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(this);

        final Button button_cancel = findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(this);

        settingsViewModel.getSetting().observe(this, new Observer<UserSetting>() {
            @Override
            public void onChanged(UserSetting setting) {
                if(setting != null ){
                    server.setText(setting.getServer());
                    port.setText(setting.getPort());
                    username.setText(setting.getUsername());
                    password.setText(setting.getPassword());
                } else {
                    UserSetting insertDefault = new UserSetting("", "", "", "");
                    settingsViewModel.insert(insertDefault);
                }
            }
        });

    }

    @BindingAdapter({"validation", "errorMsg"})
    public static void setErrorEnable(EditText editText, StringValidationRules.StringRule stringRule, final String errorMsg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (stringRule.validate(editText.getText())) {
                    editText.setError(errorMsg);
                } else {
                    editText.setError(null);
                }
            }
        });
    }

    private void onSave(){
        Intent replyIntent = new Intent();

        if (TextUtils.isEmpty(server.getText())) {
            ErrorHelper.onShowErrorDialog(getSupportFragmentManager(), getResources().getString(R.string.errTitle), getResources().getString( R.string.empty_server));
        } else if (TextUtils.isEmpty(port.getText())) {
            ErrorHelper.onShowErrorDialog(getSupportFragmentManager(), getResources().getString(R.string.errTitle), getResources().getString( R.string.empty_port));
        } else if (TextUtils.isEmpty(username.getText())) {
            ErrorHelper.onShowErrorDialog(getSupportFragmentManager(), getResources().getString(R.string.errTitle), getResources().getString( R.string.empty_username));
        } else if (TextUtils.isEmpty(password.getText())) {
            ErrorHelper.onShowErrorDialog(getSupportFragmentManager(), getResources().getString(R.string.errTitle), getResources().getString( R.string.empty_password));
        } else if(StringValidationRules.PASSWORD.validate(password.getText())) {
            ErrorHelper.onShowErrorDialog(getSupportFragmentManager(), getResources().getString(R.string.errTitle), getResources().getString( R.string.passwordNotMatching));
        } else if (StringValidationRules.SERVER.validate(server.getText())){
            ErrorHelper.onShowErrorDialog(getSupportFragmentManager(), getResources().getString(R.string.errTitle), getResources().getString( R.string.invalid_validation_server));
        }  else{

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
        setResult(RESULT_CANCELED, replyIntent);
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