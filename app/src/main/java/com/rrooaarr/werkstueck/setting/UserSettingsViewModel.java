package com.rrooaarr.werkstueck.setting;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * Creating Instances of LiveData usually here
 * Views - Fragments and Activities - shouldn’t be able of updating LiveData and thus their own state because that’s the responsibility of ViewModels. Views should be able to only observe LiveData.
 * we should encapsulate access to MutableLiveData with eg. getters or backing properties:
 *
 * Caution: A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
 *
 * There should be no imports of anything from theandroid.view or android.widgetpackages.
 */
public class UserSettingsViewModel extends AndroidViewModel {

    private SettingsRepository mRepository;

    private LiveData<UserSetting> setting;

    private LiveData<String> server;
    private LiveData<Integer> port;
    private LiveData<String> username;
    private LiveData<String> password;

    private String navtitel = "Einstellungen";
    private String titel = "Werkstücke";
    private String bottomtitel = "KG Nellingen";

    public UserSettingsViewModel(Application application) {
        super(application);
        mRepository = new SettingsRepository(application);
        setting = mRepository.getSetting();
//        fetchWordOverApi();
    }

    public void insert(UserSetting setting) { mRepository.insert(setting); }

    public void update(UserSetting setting) { mRepository.update(setting); }

    public String getNavtitel() {
        return navtitel;
    }

    public String getTitel() {
        return titel;
    }

    public String getBottomtitel() {
        return bottomtitel;
    }

    public LiveData<String> getServer() {
        return server;
    }

    public UserSettingsViewModel setServer(LiveData<String> server) {
//        setting.getValue().setServer(server.getValue());
        this.server = server;
        return this;
    }

    public LiveData<Integer> getPort() {
        return port;
    }

    public UserSettingsViewModel setPort(LiveData<Integer> port) {
//        setting.getValue().setPort(port.getValue());
        this.port = port;
        return this;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public UserSettingsViewModel setUsername(LiveData<String> username) {
//        setting.getValue().setUsername(username.getValue());
        this.username = username;
        return this;
    }

    public LiveData<String> getPassword() {
        return password;
    }

    public UserSettingsViewModel setPassword(LiveData<String> password) {
//        setting.getValue().setPassword(password.getValue());
        this.password = password;
        return this;
    }

    public LiveData<UserSetting> getSetting() {
        return setting;
    }

    public UserSettingsViewModel setSetting(LiveData<UserSetting> setting) {
        this.setting = setting;
        return this;
    }

    public void loadSetting(){
        setting = mRepository.loadSettings();
    }
}