package com.rrooaarr.werkstueck;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.bookingsql.REPLY";

    private EditText number;
    private EditText subProjectNumber;
    private EditText plantNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        number = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(number.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = number.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
