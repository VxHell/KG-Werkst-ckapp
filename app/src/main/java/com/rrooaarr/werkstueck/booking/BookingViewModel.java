package com.rrooaarr.werkstueck.booking;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.booking.model.WorkpieceContainer;

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

    private MutableLiveData<WorkpieceContainer> mutableLiveData;

    private MutableLiveData<Boolean> bookresult;

    private String navtitel = "Werkstückauswahl";
    private String titel = "Werkstücke";
    private String bottomtitel = "KG Nellingen";
    private String pk;
    private Action action;
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

    public void bookWorkpieceAction(String pk, Action action){
        if (mutableLiveData != null){
            return;
        }
        bookresult = mRepository.bookWorkpieceAction(pk, action);
    }

    public LiveData<WorkpieceContainer> getWorkpieceInfoData(){
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setPK(String pk) {
        this.pk = pk;
    }

    public String getPk() {
        return pk;
    }

    public MutableLiveData<String> getQrResult() {
        return qrResult;
    }

    public void setQrResult(MutableLiveData<String> qrResult) {
        this.qrResult = qrResult;
    }

    public MutableLiveData<Boolean> getBookresult() {
        return bookresult;
    }

    public void setBookresult(MutableLiveData<Boolean> bookresult) {
        this.bookresult = bookresult;
    }
}