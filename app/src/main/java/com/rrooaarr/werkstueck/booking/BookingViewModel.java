package com.rrooaarr.werkstueck.booking;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rrooaarr.werkstueck.booking.model.Workpiece;

/**
 * Creating Instances of LiveData usually here
 * Views - Fragments and Activities - shouldn’t be able of updating LiveData and thus their own state because that’s the responsibility of ViewModels. Views should be able to only observe LiveData.
 * we should encapsulate access to MutableLiveData with eg. getters or backing properties:
 *
 * Caution: A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
 *
 * There should be no imports of anything from theandroid.view or android.widgetpackages.
 */
public class BookingViewModel extends AndroidViewModel {

    private BookingRepository mRepository;

    private MutableLiveData<Workpiece> mutableLiveData;

    private String navtitel = "Werkstückauswahl";
    private String titel = "Werkstücke";
    private String bottomtitel = "KG Nellingen";
    private Enum action = null;
    private MutableLiveData<String> qrResult = null;

    public BookingViewModel(Application application) {
        super(application);
        mRepository = BookingRepository.getInstance(application);
    }

    public void fetchWorkpieceInfo(String workpieceNumber){
        if (mutableLiveData != null){
            return;
        }
        mutableLiveData = mRepository.fetchWorkpieceInfo(workpieceNumber);
    }

    public LiveData<Workpiece> getWorkpieceInfoData(){
        return mutableLiveData;
    }

    public String getNavtitel() {
        return navtitel;
    }


    public String getTitel() {
        return titel;
    }


    public String getBottomtitel() {
        return bottomtitel;
    }

    public Enum getAction() {
        return action;
    }

    public BookingViewModel setAction(Enum action) {
        this.action = action;
        return this;
    }

    public MutableLiveData<String> getQrResult() {
        return qrResult;
    }

    public BookingViewModel setQrResult(MutableLiveData<String> qrResult) {
        this.qrResult = qrResult;
        return this;
    }
}