package com.rrooaarr.werkstueck.booking;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rrooaarr.werkstueck.experimental.Word;
import com.rrooaarr.werkstueck.experimental.WordRepository;

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

    private WordRepository mRepository;

    private MutableLiveData<Word> mutableLiveData;

    private String navtitel = "Werkstückauswahl";
    private String titel = "Werkstücke";
    private String bottomtitel = "KG Nellingen";

    public BookingViewModel(Application application) {
        super(application);
        mRepository = new WordRepository(application);
//        mAllWords = mRepository.getAllWords();
//        fetchWordOverApi();
    }

    public void insert(Word word) { mRepository.insert(word); }

    private void fetchWordOverApi(){
        if (mutableLiveData != null){
            return;
        }
        mutableLiveData = mRepository.getWordOverAPI("33");
    }

    public LiveData<Word> getWordData() {
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

}