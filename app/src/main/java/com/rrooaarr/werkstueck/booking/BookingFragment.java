package com.rrooaarr.werkstueck.booking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.rrooaarr.werkstueck.BuildConfig;
import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.api.errorhandling.DataErrorWrapper;
import com.rrooaarr.werkstueck.booking.api.errorhandling.ErrorHelper;
import com.rrooaarr.werkstueck.permission.RequestUserPermission;
import com.rrooaarr.werkstueck.setting.UserSetting;
import com.rrooaarr.werkstueck.util.StringValidationRules;
import com.rrooaarr.werkstueck.util.Utils;
import com.rrooaarr.werkstueck.view.FragmentBase;
import com.rrooaarr.werkstueck.wsinfo.WerkstückinfoFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.rrooaarr.werkstueck.booking.model.AppDefaults.ACTION;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.BOOK;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.PK;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.SETTINGS;
import static com.rrooaarr.werkstueck.booking.model.AppDefaults.WST;

public class BookingFragment extends Fragment implements FragmentBase, ZXingScannerView.ResultHandler, View.OnClickListener {

    public static String TAG = BookingFragment.class.getSimpleName();
    private ZXingScannerView scannerView;
    private BookingViewModel model;
    private EditText number;
    private EditText subProjectNumber;
    private EditText plantNumber;
    private UserSetting setting;

    public BookingFragment() {
    }

    public static BookingFragment newInstance() {
        return new BookingFragment();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FragmentActivity fragmentActivity = getActivity();
        model = new ViewModelProvider(fragmentActivity).get(BookingViewModel.class);
        model.setNavtitel("Werkstückauswahl");

        model.getSetting().observe(this, new Observer<UserSetting>() {
            @Override
            public void onChanged(UserSetting settingLoaded) {
                setting = settingLoaded;
            }
        });

        Serializable booking = getArguments().getSerializable(BOOK);

        if (booking != null) {
            String pk = (String) getArguments().getSerializable(PK);
            if (pk != null) {
                model.bookWorkpieceAction(pk, model.getAction());

                model.getBookresult().observe(this, new Observer<DataErrorWrapper<Boolean>>() {
                    @Override
                    public void onChanged(DataErrorWrapper<Boolean> dataWrapper) {
                        if (dataWrapper != null) {
//                            final Boolean bookresult = dataWrapper.getData();
                            if (dataWrapper.getStatus() == DataErrorWrapper.APIStatus.SUCCESS) {
                                ErrorHelper.makeToast(getContext(), " Buchung: " + (model.getAction() != null ? model.getAction().getLocale() : "") + " erfolgreich");
                            } else {
                                ErrorHelper.doDefaultApiErrorHandling(dataWrapper, getContext());
                            }
                        } else {
                            ErrorHelper.makeToast(getContext(), " Buchung: " + (model.getAction() != null ? model.getAction().getLocale() : "") + " nicht erfolgreich!");
                        }
                    }
                });
            } else {
                ErrorHelper.makeToast(getContext(), " Buchung nicht zuordbar.");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_booking, container, false);
        initViews(fragmentView);
        return fragmentView;
    }

    private void initViews(View view) {
        initScanner(view);

        number = view.findViewById(R.id.number);
        subProjectNumber = view.findViewById(R.id.sub_project_number);
        plantNumber = view.findViewById(R.id.plant_number);

        final Button button = view.findViewById(R.id.button_select);
        button.setOnClickListener(this);
    }

    private void initScanner(View view) {
        RequestUserPermission requestUserPermission = new RequestUserPermission(BookingFragment.this.getActivity());
        requestUserPermission.verifyCameraPermissionsActivity();

        this.scannerView = view.findViewById(R.id.scanner_container_fragment);

        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.QR_CODE);
        this.scannerView.setFormats(list);
        this.scannerView.setResultHandler(this);
    }

    private void onSelect() {

        RequestUserPermission requestUserPermission = new RequestUserPermission(getActivity());
        boolean alreadyPermitted = requestUserPermission.verifyInternetPermissionsActivity();

        if (!alreadyPermitted) {
            Toast.makeText(getContext(), R.string.no_internet_permisssion, Toast.LENGTH_SHORT).show();
        }

        final String plant = plantNumber.getText().toString();
        final String sub = subProjectNumber.getText().toString();
        final String num = number.getText().toString();

        StringBuilder sb = new StringBuilder(plant).append("-").append(sub).append("-").append(num);
//
        Fragment frag = WerkstückinfoFragment.newInstance();
        Bundle bundle = new Bundle();

        if (setting != null) {
            ArrayList<String> settingsString = new ArrayList<>();
            settingsString.add(setting.getServer());
            settingsString.add(setting.getPort());
            settingsString.add(setting.getUsername());
            settingsString.add(setting.getPassword());
            bundle.putStringArrayList(SETTINGS, settingsString);
        }

        bundle.putSerializable(ACTION, model.getAction());
        bundle.putString(WST, sb.toString());

        frag.setArguments(bundle);
        final FragmentManager manager = getActivity().getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 0) {
            final FragmentManager.BackStackEntry backStackEntryAt = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1);
            manager.popBackStack(backStackEntryAt.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        Utils.replaceFragmentBooking(frag, true, getActivity().getSupportFragmentManager(), R.id.master_booking_fragment);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_select) {
            onSelect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.scannerView.setResultHandler(this);
        this.scannerView.startCamera();
        if (BuildConfig.DEBUG) {
            Log.d(getClass().getSimpleName(), "onStart()");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // stopping Camera takes 2 secondso
//        this.scannerView.stopCamera();
        if (BuildConfig.DEBUG) {
            Log.d(getClass().getSimpleName(), "onPause()");
        }
    }

    @Override
    public void handleResult(Result result) {
        // TODO beispielresult ist: "https://wst.pas2.de/?wst=183565"
        this.scannerView.stopCamera();

        if (result != null) {
            final String resultText = result.getText();
            Log.d(getClass().getSimpleName(), resultText + " " + new String(result.getRawBytes()));
            if (resultText.contains("wst=")) {
                final String[] strings = resultText.split("wst=");
                Log.d(TAG, "handleResult:" + strings[1]);

                Fragment frag = WerkstückinfoFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ACTION, model.getAction());
                bundle.putString(WST, strings[1]);

                frag.setArguments(bundle);
                Utils.replaceFragmentBooking(frag, true, getActivity().getSupportFragmentManager(), R.id.master_booking_fragment);

            } else {
                Toast.makeText(
                        getContext(),
                        "QR-Code beschädigt oder falsch",
                        Toast.LENGTH_LONG).show();
                onResume();
            }
        } else {
            Toast.makeText(
                    getContext(),
                    "QR-Code nicht lesbar",
                    Toast.LENGTH_LONG).show();
            onResume();
        }
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}