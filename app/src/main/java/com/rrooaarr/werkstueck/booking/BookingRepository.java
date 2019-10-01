package com.rrooaarr.werkstueck.booking;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rrooaarr.werkstueck.booking.api.RetrofitServiceGenerator;
import com.rrooaarr.werkstueck.booking.api.BookingWebservice;
import com.rrooaarr.werkstueck.booking.model.Workpiece;
import com.rrooaarr.werkstueck.setting.UserSetting;
import com.rrooaarr.werkstueck.setting.UserSettingDao;
import com.rrooaarr.werkstueck.setting.UserSettingsRoomDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingRepository {

    private BookingWebservice api;
    private UserSettingDao mSettingsDao;
    private LiveData<UserSetting> settingLiveData;

    public BookingRepository(Application application) {
        UserSettingsRoomDatabase db = UserSettingsRoomDatabase.getDatabase(application);

        mSettingsDao = db.settingDao();
        settingLiveData = mSettingsDao.getSetting();
        final UserSetting userSetting = settingLiveData.getValue();

        api = RetrofitServiceGenerator.createService(BookingWebservice.class, null, "Fertigung", "1e604b570f9466c5924bdb37cf3eb00d01fb49f11aecccd01a761e6c513465e823f0e1f7b9126965fa6a8e0a562773dbee4b6dd768f310af095ef63b17764638");
    }

    public MutableLiveData<Workpiece> getWorkpieceInfo(String workpieceNumber){
        final MutableLiveData<Workpiece> workpieceMutableLiveData = new MutableLiveData<>();
        Call<Workpiece> callAsync = api.getWorkpieceInfo(workpieceNumber);

        callAsync.enqueue(new Callback<Workpiece>() {
            @Override
            public void onResponse(Call<Workpiece> call, Response<Workpiece> response) {
                if (response.isSuccessful()){
                    workpieceMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Workpiece> call, Throwable throwable) {
//                TODO add logging
                workpieceMutableLiveData.setValue(null);
            }
        });
        return workpieceMutableLiveData;
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