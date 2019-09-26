package com.rrooaarr.werkstueck;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_SAVE_REPLY = "com.example.android.savesql.REPLY";
    public static final String EXTRA_CANCEL_REPLY = "com.example.android.cancel.REPLY";

    public static final int SETTINGS_2_MAINACTIVITY_REQUEST_CODE = 1;

    private EditText server;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        server = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(this);

    }

    private void onSave(){
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(server.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String word = server.getText().toString();
            replyIntent.putExtra(EXTRA_SAVE_REPLY, word);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    private void onCancel(){
        Intent replyIntent = new Intent();
            String word = server.getText().toString();
            replyIntent.putExtra(EXTRA_CANCEL_REPLY, word);
            setResult(RESULT_OK, replyIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save:
                onSave();
                break;
            case R.id.button_settings:
                onCancel();
                break;
        }
    }
}
