package com.rrooaarr.werkstueck.booking;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rrooaarr.werkstueck.BuildConfig;
import com.rrooaarr.werkstueck.booking.api.BookingWebservice;
import com.rrooaarr.werkstueck.booking.api.RetrofitServiceGenerator;
import com.rrooaarr.werkstueck.booking.api.errorhandling.DataErrorWrapper;
import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.booking.model.WorkpieceContainer;
import com.rrooaarr.werkstueck.setting.UserSetting;
import com.rrooaarr.werkstueck.setting.UserSettingDao;
import com.rrooaarr.werkstueck.setting.UserSettingsRoomDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rrooaarr.werkstueck.util.Utils.sha512;
import static com.rrooaarr.werkstueck.util.retrofitHelper.logRetrofitError;

public class BookingRepository {

    private static final String TAG = BookingRepository.class.getSimpleName();
    private static volatile BookingRepository INSTANCE;
    private BookingWebservice api;
    private UserSettingDao mSettingsDao;

    private BookingRepository(Application application) {
        UserSettingsRoomDatabase db = UserSettingsRoomDatabase.getDatabase(application);
        mSettingsDao = db.settingDao();
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

    public void initApi(String loadedUrl, String username, String password) {
        if (api == null) {
            final String crypted = sha512(password);
            api = RetrofitServiceGenerator.createService(BookingWebservice.class, loadedUrl, username, crypted);
        }
    }

    public MutableLiveData<DataErrorWrapper<WorkpieceContainer>> fetchWorkpieceInfo(String workpieceNumber) {
        MutableLiveData<DataErrorWrapper<WorkpieceContainer>> workpieceMutableLiveData = new MutableLiveData<>();

        api.getWorkpieceInfo(workpieceNumber).enqueue(new Callback<WorkpieceContainer>() {
            @Override
            public void onResponse(@NonNull Call<WorkpieceContainer> call, @NonNull Response<WorkpieceContainer> response) {
                final DataErrorWrapper<WorkpieceContainer> workpieceContainerDataErrorWrapper = new DataErrorWrapper<>();
                if (response.isSuccessful()) {
                    workpieceContainerDataErrorWrapper.setData(response.body());
                    workpieceContainerDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.SUCCESS);

                } else if (response.code() == 404) {
                    workpieceContainerDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.NOTFOUND);
                    workpieceContainerDataErrorWrapper.setData(null);

                } else if (response.code() == 403) {
                    workpieceContainerDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.UNAUTHORIZED);
                    workpieceContainerDataErrorWrapper.setData(null);
                } else {
                    workpieceContainerDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.ERROR);
                    workpieceContainerDataErrorWrapper.setData(null);
                    if (BuildConfig.DEBUG) {
                        logRetrofitError(TAG, response);
                    }
                }
                workpieceMutableLiveData.setValue(workpieceContainerDataErrorWrapper);
            }

            @Override
            public void onFailure(Call<WorkpieceContainer> call, Throwable throwable) {
                final DataErrorWrapper<WorkpieceContainer> workpieceContainerDataErrorWrapper = new DataErrorWrapper<>(null);
                workpieceContainerDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.EXCEPTION);
                workpieceMutableLiveData.setValue(workpieceContainerDataErrorWrapper);

                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "onFailure: " + throwable.getMessage());
                    Log.e(TAG, "onFailure Stacktrace:" + Log.getStackTraceString(throwable));
                }
            }
        });

        if (BuildConfig.DEBUG) {
            if (workpieceMutableLiveData.getValue() != null) {
                Log.d("test", "fetchWorkpieceInfo: it worked!");
            }
        }

        return workpieceMutableLiveData;
    }


    public MutableLiveData<DataErrorWrapper<Boolean>> bookWorkpieceAction(String pk, Action action) {
        String actionStr = "";
        final MutableLiveData<DataErrorWrapper<Boolean>> bookresult = new MutableLiveData<>();

        switch (action) {
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
                final DataErrorWrapper<Boolean> booleanDataErrorWrapper = new DataErrorWrapper<>();
                if (response.isSuccessful()) {
                    booleanDataErrorWrapper.setData(true);
                    booleanDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.SUCCESS);
                } else if (response.code() == 404) {
                    booleanDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.NOTFOUND);
                    booleanDataErrorWrapper.setData(null);

                } else if (response.code() == 403) {
                    booleanDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.UNAUTHORIZED);
                    booleanDataErrorWrapper.setData(null);
                } else {
                    booleanDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.ERROR);
                    booleanDataErrorWrapper.setData(null);
                    if (BuildConfig.DEBUG) {
                        logRetrofitError(TAG, response);
                    }
                }
                bookresult.setValue(booleanDataErrorWrapper);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                final DataErrorWrapper<Boolean> booleanDataErrorWrapper = new DataErrorWrapper<>(null);
                booleanDataErrorWrapper.setStatus(DataErrorWrapper.APIStatus.EXCEPTION);
                bookresult.setValue(booleanDataErrorWrapper);
                Log.e(TAG, "onFailure: " + throwable.getMessage());
            }
        });

//        if (BuildConfig.DEBUG) {
//            if (bookresult.getValue() != null && bookresult.getValue()) {
//                Log.d("test", "fetchWorkpieceInfo: it worked!");
//            }
//        }

        return bookresult;
    }

    LiveData<UserSetting> getSetting() {
        return mSettingsDao.getSetting();
    }

    public void insert(UserSetting setting) {
        new BookingRepository.insertAsyncTask(mSettingsDao).execute(setting);
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

}