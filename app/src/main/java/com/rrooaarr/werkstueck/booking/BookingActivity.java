package com.rrooaarr.werkstueck.booking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.databinding.ActivityBookingBinding;
import com.rrooaarr.werkstueck.permission.RequestUserPermission;
import com.rrooaarr.werkstueck.util.Utils;
import com.rrooaarr.werkstueck.wsinfo.WerkstückinfoFragment;

import static com.rrooaarr.werkstueck.booking.model.AppDefaults.ACTION;

public class BookingActivity extends AppCompatActivity {

    private BookingViewModel model;
    private ActivityBookingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        setupBindings();

        final BookingFragment fragment = new BookingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        Utils.replaceFragment(fragment, false, getSupportFragmentManager(), R.id.master_booking_fragment);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof WerkstückinfoFragment) {
//            BookingFragment bookingFragment = ((BookingFragment) fragment);
            initWSTIFragment();
        }
    }

    private void setupBindings() {
        // TODO consider to make viewmodel singelton for instance Dagger
        // Note: ViewmodelProvider(this).get(class) gives per fragment/activity an own instance of ViewModel
        model = new ViewModelProvider(this).get(BookingViewModel.class);
        model.setNavtitel("Werkstückauswahl");
        Action action = (Action) getIntent().getSerializableExtra(ACTION);
        model.setAction(action);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking);

//        Serializable booking = getIntent().getSerializableExtra(BOOK);

        binding.setModel(model);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestUserPermission.REQUEST_CAMERA) {
            if (grantResults.length != 1 ||
                    grantResults[0] != RequestUserPermission.REQUEST_CAMERA) {
//                            Toast.makeText(this, R.string.no_camera_permisssion, Toast.LENGTH_SHORT).show();
                    }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            final FragmentManager.BackStackEntry backStackEntryAt = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
        }
        super.onBackPressed();
    }

    public void initWSTIFragment() {
        model.setNavtitel("Werkstückinfo");
        binding.setModel(model);
    }
}
