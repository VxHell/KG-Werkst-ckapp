package com.rrooaarr.werkstueck.booking.scanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.rrooaarr.werkstueck.BuildConfig;
import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.booking.BookingViewModel;
import com.rrooaarr.werkstueck.booking.model.Workpiece;
import com.rrooaarr.werkstueck.permission.RequestUserPermission;
import com.rrooaarr.werkstueck.view.FragmentBase;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler, FragmentBase {

    private static final String TAG = ScannerFragment.class.getSimpleName();
    private ZXingScannerView scannerView;
    private BookingViewModel model;

    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(BookingViewModel.class);
        model.setQrResult(new MutableLiveData<>());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RequestUserPermission requestUserPermission = new RequestUserPermission(ScannerFragment.this.getActivity());
        boolean alreadyPermitted = requestUserPermission.verifyCameraPermissionsActivity();

        if(!alreadyPermitted){
            Toast.makeText(ScannerFragment.this.getActivity(), R.string.no_camera_permisssion, Toast.LENGTH_SHORT).show();
        }

        this.scannerView = new ZXingScannerView(this.getContext());
        List<BarcodeFormat> list = new ArrayList<>();
        list.add(BarcodeFormat.QR_CODE);
        this.scannerView.setFormats(list);
        this.scannerView.setResultHandler(this);
        return this.scannerView;
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
        this.scannerView.stopCamera();
        if (BuildConfig.DEBUG) {
            Log.d(getClass().getSimpleName(), "onPause()");
        }
    }

    @Override
    public void handleResult(Result result) {
        // TODO beispielresult ist: "https://wst.pas2.de/?wst=183565"
        this.scannerView.stopCamera();

        final String resultText = result.getText();
        Log.d(getClass().getSimpleName(), resultText + " " + new String(result.getRawBytes()));
        model.setQrResult(new MutableLiveData<>(resultText));
        final String[] strings = resultText.split("wst=");
        Log.d(TAG, "handleResult:" +strings[1]);
        final MutableLiveData<Workpiece> workpieceInfo = model.getWorkpieceInfo(strings[1]);

        if(workpieceInfo != null && workpieceInfo.getValue() != null) {
            final String projektId = workpieceInfo.getValue().getProjektId();
            Toast.makeText(
                    getContext(),
                    "ProjektId from Webservice: "+projektId,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    getContext(),
                   " Webservice call mit null result für workpieceInfo",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String getFragmentTag() {
        return "ScannerFragment";
    }
}
