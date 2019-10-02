package com.rrooaarr.werkstueck.booking;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rrooaarr.werkstueck.BuildConfig;
import com.rrooaarr.werkstueck.booking.api.BookingWebservice;
import com.rrooaarr.werkstueck.booking.api.RetrofitServiceGenerator;
import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.booking.model.Workpiece;
import com.rrooaarr.werkstueck.setting.UserSetting;
import com.rrooaarr.werkstueck.setting.UserSettingDao;
import com.rrooaarr.werkstueck.setting.UserSettingsRoomDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rrooaarr.werkstueck.util.retrofitHelper.logRetrofitError;

public class BookingRepository {

    private static final String TAG = BookingRepository.class.getSimpleName() ;
    private BookingWebservice api;
    private UserSettingDao mSettingsDao;
    private LiveData<UserSetting> settingLiveData;
    private static volatile BookingRepository INSTANCE;

    private BookingRepository(Application application) {
        UserSettingsRoomDatabase db = UserSettingsRoomDatabase.getDatabase(application);

        mSettingsDao = db.settingDao();
        settingLiveData = mSettingsDao.getSetting();
        final UserSetting userSetting = settingLiveData.getValue();

        api = RetrofitServiceGenerator.createService(BookingWebservice.class, null, "Fertigung", "1e604b570f9466c5924bdb37cf3eb00d01fb49f11aecccd01a761e6c513465e823f0e1f7b9126965fa6a8e0a562773dbee4b6dd768f310af095ef63b17764638");
    }

    public static BookingRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (UserSettingsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookingRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public MutableLiveData<Workpiece> fetchWorkpieceInfo(String workpieceNumber){
        MutableLiveData<Workpiece> workpieceMutableLiveData = new MutableLiveData<>();

        api.getWorkpieceInfo(workpieceNumber).enqueue(new Callback<Workpiece>() {
            @Override
            public void onResponse(Call<Workpiece> call, Response<Workpiece> response) {
                if (response.isSuccessful()){
                    workpieceMutableLiveData.setValue(response.body());
                } else {
                    workpieceMutableLiveData.setValue(null);
                    if(BuildConfig.DEBUG ) {
                        logRetrofitError(TAG, response);
                    }
                }
            }

            @Override
            public void onFailure(Call<Workpiece> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                workpieceMutableLiveData.setValue(null);
            }
        });

        if(BuildConfig.DEBUG) {
            if (workpieceMutableLiveData.getValue() != null) {
                Log.d("test", "fetchWorkpieceInfo: it worked!");
            }
        }

        return workpieceMutableLiveData;
    }

    public MutableLiveData<Boolean> bookWorkpieceAction(String pk, Action action){
        String actionStr = "";
        final MutableLiveData<Boolean> bookresult = new MutableLiveData<>();
        switch (action){
            case FINISHING:
                actionStr = "veredelung";
                break;
            case SHIPPING:
                actionStr = "versand";
                break;
            case PACKAGING:
                actionStr = "verpackung";
                break;
        }

        api.bookWorkpieceAction(pk, actionStr).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    bookresult.setValue(true);
                } else {
                    bookresult.setValue(false);
                    if(BuildConfig.DEBUG ) {
                        logRetrofitError(TAG, response);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                bookresult.setValue(false);
            }
        });

        if(BuildConfig.DEBUG) {
            if (bookresult.getValue() != null && bookresult.getValue()) {
                Log.d("test", "fetchWorkpieceInfo: it worked!");
            }
        }

        return bookresult;
    }

    LiveData<UserSetting> getSetting() {
        return settingLiveData;
    }

    LiveData<UserSetting> loadSettings() {
        return settingLiveData;
    }

    public void insert (UserSetting setting) {
        new BookingRepository.insertAsyncTask(mSettingsDao).execute(setting);
    }

    public void update (UserSetting setting) {
        new BookingRepository.UpdateAsyncTask(mSettingsDao).execute(setting);
    }

    private static class insertAsyncTask extends AsyncTask<UserSetting, Void, Void> {

        private UserSettingDao mAsyncTaskDao;

        insertAsyncTask(UserSettingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserSetting... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<UserSetting, Void, Void> {

        private UserSettingDao mAsyncTaskDao;

        UpdateAsyncTask(UserSettingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserSetting... params) {
            UserSetting set = params[0];
            mAsyncTaskDao.updateProperties(set.getId(), set.getServer(), set.getPort(), set.getUsername(), set.getPassword());
            return null;
        }
    }

}