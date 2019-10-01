package com.rrooaarr.werkstueck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.booking.BookingActivity;
import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.databinding.ActivityMainBinding;
import com.rrooaarr.werkstueck.setting.SettingsActivity;
import com.rrooaarr.werkstueck.util.Utils;

import static com.rrooaarr.werkstueck.booking.BookingActivity.ACTION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    public static final int SETTINGS_ACTIVITY_REQUEST_CODE = 1;
    public static final int BOOKING_ACTIVITY_REQUEST_CODE = 2;
    Button btn_book_finishing, btn_settings;
    private MainViewModel mainViewModel;
    private long lastBackPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        final WordListAdapter adapter = new WordListAdapter(this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.setMainViewModel(mainViewModel);
        initViews(binding.getRoot());
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        try {
            if (manager.getBackStackEntryCount() > 1) {
                manager.popBackStackImmediate();
            } else if (manager.getBackStackEntryCount() == 1) {
                manager.popBackStackImmediate();
            } else {
                Toast toast;
                if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
                    toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_SHORT);
                    toast.show();
                    this.lastBackPressTime = System.currentTimeMillis();
                } else {
                    super.onBackPressed();
                }
            }
        } catch (IllegalStateException ise) {
            Utils.doErrorHandling(R.string.errBackPressNavi, getApplicationContext());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE) {

            int responseId = R.string.empty_not_saved;
            if (resultCode == RESULT_OK) {
                responseId = R.string.settings_saved;
            }

            Toast.makeText(
                    getApplicationContext(),
                    responseId,
                    Toast.LENGTH_LONG).show();

        } else if (requestCode == BOOKING_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.settings_saved,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initViews(View view) {
        btn_book_finishing = view.findViewById(R.id.button_book_finishing);
        btn_book_finishing.setOnClickListener(this);
        btn_settings = view.findViewById(R.id.button_settings);
        btn_settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_settings) {
            startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), SETTINGS_ACTIVITY_REQUEST_CODE);
        } else {
            final Intent intent = new Intent(MainActivity.this, BookingActivity.class);

            switch (id) {
                case R.id.button_book_finishing:
                    intent.putExtra(ACTION, Action.FINISHING);
                    break;
                case R.id.button_book_packaging:
                    intent.putExtra(ACTION, Action.PACKAGING);
                    break;
                case R.id.button_book_shipping:
                    intent.putExtra(ACTION, Action.SHIPPING);
                    break;
            }

            startActivity(intent);
        }
    }

}