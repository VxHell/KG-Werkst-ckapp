package com.example.roomwordsample;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Creating Instances of LiveData usually here
 */
public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;

    private LiveData<List<Word>> mAllWords;

    private MutableLiveData<Word> mutableLiveData;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
        fetchWordOverApi();
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
}
