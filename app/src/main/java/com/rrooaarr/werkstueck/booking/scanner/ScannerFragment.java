package com.rrooaarr.werkstueck.booking.scanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.rrooaarr.werkstueck.BuildConfig;
import com.rrooaarr.werkstueck.R;
import com.rrooaarr.werkstueck.permission.RequestUserPermission;
import com.rrooaarr.werkstueck.view.FragmentBase;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler, FragmentBase {

    private ZXingScannerView scannerView;

    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        this.scannerView.stopCamera();
        Log.d(getClass().getSimpleName(), result.getText() + " " + new String(result.getRawBytes()));
    }

    @Override
    public String getFragmentTag() {
        return "ScannerFragment";
    }
}
