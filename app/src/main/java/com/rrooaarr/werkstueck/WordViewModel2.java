package com.rrooaarr.werkstueck;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Creating Instances of LiveData usually here
 * Views - Fragments and Activities - shouldn’t be able of updating LiveData and thus their own state because that’s the responsibility of ViewModels. Views should be able to only observe LiveData.
 * we should encapsulate access to MutableLiveData with eg. getters or backing properties:
 *
 * Caution: A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
 *
 * There should be no imports of anything from theandroid.view or android.widgetpackages.
 */
public class WordViewModel2 extends AndroidViewModel {

    private WordRepository mRepository;

    private LiveData<List<Word>> mAllWords;

    private MutableLiveData<Word> mutableLiveData;

    private String navtitel = "Menü";

    public WordViewModel2(Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
//        fetchWordOverApi();
    }

    public LiveData<List<Word>> getAllWords() { return mAllWords; }

    public void insert(Word word) { mRepository.insert(word); }

    private void fetchWordOverApi(){
        if (mutableLiveData != null){
            return;
        }
        mutableLiveData = mRepository.getWordOverAPI("33");
    }

    public LiveData<Word> getNewsRepository() {
        return mutableLiveData;
    }

    public String getNavtitel() {
        return navtitel;
    }

    public WordViewModel2 setNavtitel(String navtitel) {
        this.navtitel = navtitel;
        return this;
    }
}
