package com.rrooaarr.werkstueck.setting;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SettingsRepository {

    private UserSettingDao mSettingsDao;
    private LiveData<UserSetting> settingLiveData;
    private MutableLiveData<List<UserSetting>> searchResults =
            new MutableLiveData<>();

    private void asyncFinished(List<UserSetting> results) {
        searchResults.setValue(results);
    }

    public SettingsRepository(Application application) {
        UserSettingsRoomDatabase db = UserSettingsRoomDatabase.getDatabase(application);
        mSettingsDao = db.settingDao();
        settingLiveData = mSettingsDao.getSetting();
    }

    LiveData<UserSetting> getSetting() {
        return settingLiveData;
    }

    LiveData<UserSetting> loadSettings() {
        return settingLiveData;
    }

    public void insert (UserSetting setting) {
        new insertAsyncTask(mSettingsDao).execute(setting);
    }

    public void update (UserSetting setting) {
        new UpdateAsyncTask(mSettingsDao).execute(setting);
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
    // Maybe for late use
    public void loadSettings(String name) {
        QueryAsyncTask task = new QueryAsyncTask(mSettingsDao);
        task.delegate = this;
        task.execute(name);
    }
    // Maybe for late use
    private static class QueryAsyncTask extends
            AsyncTask<String, Void, List<UserSetting>> {

        private UserSettingDao asyncTaskDao;
        private SettingsRepository delegate = null;

        QueryAsyncTask(UserSettingDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<UserSetting> doInBackground(final String... params) {
            return asyncTaskDao.getAllSettings2();
        }

        @Override
        protected void onPostExecute(List<UserSetting> result) {
            delegate.asyncFinished(result);
        }
    }

}
