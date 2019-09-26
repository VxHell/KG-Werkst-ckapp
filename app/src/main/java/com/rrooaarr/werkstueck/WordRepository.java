package com.rrooaarr.werkstueck;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordRepository {

    private WordWebservice wordWebservice = null;
    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
        wordWebservice = RetrofitServiceGenerator.createService(WordWebservice.class);
    }

    public MutableLiveData<Word> getWordOverAPI(String wordId){
        final MutableLiveData<Word> wordData = new MutableLiveData<>();
        Call<Word> callAsync = wordWebservice.getWord(wordId);

        callAsync.enqueue(new Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                if (response.isSuccessful()){
                    wordData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Word> call, Throwable throwable) {
//                TODO add logging
                wordData.setValue(null);
            }
        });
        return wordData;
    }

    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }
    // dont call it on the UI thread!
    public void insert (Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
