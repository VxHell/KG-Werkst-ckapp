package com.rrooaarr.werkstueck.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.BuildConfig;
import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.booking.scanner.ScannerFragment;
import com.rrooaarr.werkstueck.databinding.ActivityBookingBinding;
import com.rrooaarr.werkstueck.permission.RequestUserPermission;
import com.rrooaarr.werkstueck.wsinfo.WerkstückinfoActivity;

import static com.rrooaarr.werkstueck.booking.model.AppDefaults.ACTION;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.WST;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    private BookingViewModel model;
    private EditText number;
    private EditText subProjectNumber;
    private EditText plantNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        setupBindings();

        initViews();
    }

    private void setupBindings() {
        // TODO consider to make viewmodel singelton for instance Dagger
        // Note: ViewmodelProvider(this).get(class) gives per fragment/activity an own instance of ViewModel
        model = new ViewModelProvider(this).get(BookingViewModel.class);
        ActivityBookingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_booking);

        Action action = (Action) getIntent().getSerializableExtra(ACTION);
        model.setAction(action);
        binding.setModel(model);
    }

    private void initViews() {

        showScannerFragment();

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

    public void showScannerFragment() {
        if (BuildConfig.DEBUG) {
            Log.d(getClass().getSimpleName(), "showScannerFragment");
        }
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.scanner_container_fragment, ScannerFragment.newInstance(model.getAction())).commit();
    }

    private void onSelect() {
        Intent replyIntent = new Intent();

        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        boolean alreadyPermitted = requestUserPermission.verifyInternetPermissionsActivity();

        if (!alreadyPermitted) {
            Toast.makeText(this, R.string.no_internet_permisssion, Toast.LENGTH_SHORT).show();
        }

        final String plant = plantNumber.getText().toString();
        final String sub = subProjectNumber.getText().toString();
        final String num = number.getText().toString();

        StringBuilder sb = new StringBuilder(plant).append("-").append(sub).append("-").append(num);

        final Intent intent = new Intent(BookingActivity.this, WerkstückinfoActivity.class);
        intent.putExtra(ACTION, model.getAction());
        intent.putExtra(WST, sb.toString());

        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_select) {
            onSelect();
        }
    }
}
