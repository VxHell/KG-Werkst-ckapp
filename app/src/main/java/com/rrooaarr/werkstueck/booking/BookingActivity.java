package com.rrooaarr.werkstueck.booking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.databinding.ActivityBookingBinding;
import com.rrooaarr.werkstueck.setting.UserSetting;
import com.rrooaarr.werkstueck.util.Utils;

import java.io.Serializable;

import static com.rrooaarr.werkstueck.booking.model.AppDefaults.ACTION;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.BOOK;

public class BookingActivity extends AppCompatActivity {

    private BookingViewModel model;
    private UserSetting setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        setupBindings();

        final BookingFragment fragment = new BookingFragment();
        Bundle bundle = new Bundle();
//
        fragment.setArguments(bundle);
        Utils.replaceFragment(fragment, false, getSupportFragmentManager(), R.id.master_booking_fragment);

    }

    private void setupBindings() {
        // TODO consider to make viewmodel singelton for instance Dagger
        // Note: ViewmodelProvider(this).get(class) gives per fragment/activity an own instance of ViewModel
        model = ViewModelProviders.of(this).get(BookingViewModel.class);
        model.setNavtitel("Werkstückauswahl");
        Action action = (Action) getIntent().getSerializableExtra(ACTION);
        model.setAction(action);

        ActivityBookingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_booking);

        Serializable booking = getIntent().getSerializableExtra(BOOK);

        binding.setModel(model);
    }

    private void initViews() {

//        showScannerFragment();

//        number = findViewById(R.id.number);
//        subProjectNumber = findViewById(R.id.sub_project_number);
//        plantNumber = findViewById(R.id.plant_number);
//
//        final Button button = findViewById(R.id.button_select);
//        button.setOnClickListener(this);

    }


//    public void showScannerFragment() {
//        if (BuildConfig.DEBUG) {
//            Log.d(getClass().getSimpleName(), "showScannerFragment");
//        }
//        FragmentManager fm = this.getSupportFragmentManager();
//        fm.beginTransaction().replace(R.id.scanner_container_fragment, ScannerFragment.newInstance(model.getAction())).commit();
//    }

//    private void onSelect() {
//
//        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
//        boolean alreadyPermitted = requestUserPermission.verifyInternetPermissionsActivity();
//
//        if (!alreadyPermitted) {
//            Toast.makeText(this, R.string.no_internet_permisssion, Toast.LENGTH_SHORT).show();
//        }
//
//        final String plant = plantNumber.getText().toString();
//        final String sub = subProjectNumber.getText().toString();
//        final String num = number.getText().toString();
//
//        StringBuilder sb = new StringBuilder(plant).append("-").append(sub).append("-").append(num);
////
//        Fragment frag = WerkstückinfoFragment.newInstance();
//        Bundle bundle = new Bundle();
//
//        if(setting!= null) {
//            ArrayList<String> settingsString = new ArrayList<>();
//            settingsString.add(setting.getServer());
//            settingsString.add(setting.getPort());
//            settingsString.add(setting.getUsername());
//            settingsString.add(setting.getPassword());
//            bundle.putStringArrayList(SETTINGS, settingsString);
//        }
//
//        bundle.putSerializable(ACTION, model.getAction());
//        bundle.putString(WST, sb.toString());
//
//        frag.setArguments(bundle);
//
//        Utils.replaceFragment(frag,true, getSupportFragmentManager(), R.id.master_booking_fragment);
//    }

//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.button_select) {
//            onSelect();
//        }
//    }
}
