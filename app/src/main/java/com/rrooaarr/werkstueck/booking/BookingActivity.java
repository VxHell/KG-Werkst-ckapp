package com.rrooaarr.werkstueck.booking;

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

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.databinding.ActivityBookingBinding;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener  {

    private BookingViewModel model;

    private EditText number;
    private EditText subProjectNumber;
    private EditText plantNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        setupBindings();
    }

    private void setupBindings() {
        // TODO consider to make viewmodel singelton for instance Dagger
        model = new ViewModelProvider(this).get(BookingViewModel.class);
        ActivityBookingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_booking);
//        model.loadSetting();
        binding.setModel(model);

        initViews(binding.getRoot());
    }

    private void initViews(View view) {

        number = findViewById(R.id.number);
        subProjectNumber = findViewById(R.id.sub_project_number);
        plantNumber = findViewById(R.id.plant_number);

        final Button button = findViewById(R.id.button_select);
        button.setOnClickListener(this);


//        model.getSetting().observe(this, new Observer<UserSetting>() {
//            @Override
//            public void onChanged(UserSetting setting) {
//                server.setText(setting != null ? setting.getServer() : null);
//                port.setText(setting != null ? setting.getPort() : null);
//                username.setText(setting != null ? setting.getUsername() : null);
//                password.setText(setting != null ? setting.getPassword() : null);
//            }
//        });

    }

    private void onSelect(){
        Intent replyIntent = new Intent();

        if (TextUtils.isEmpty(plantNumber.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_server,
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(subProjectNumber.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_port,
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(number.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_username,
                    Toast.LENGTH_LONG).show();
        } else {

            String mServer = plantNumber.getText().toString();
            String mPort = subProjectNumber.getText().toString();
            String mUsername = number.getText().toString();

//            UserSetting setting = new UserSetting(mServer, mPort, mUsername, mPasswort);
//            model.update(setting);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_select) {
            onSelect();
        }
    }
}