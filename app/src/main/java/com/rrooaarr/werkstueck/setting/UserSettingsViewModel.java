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

    private String navtitel = "Einstellungen";
    private String titel = "Werkstücke";
    private String bottomtitel = "KG Nellingen";

    public UserSettingsViewModel(Application application) {
        super(application);
        mRepository = new SettingsRepository(application);
        setting = mRepository.getSetting();
//        fetchWordOverApi();
    }

    // TODO refactor to IsSettingViewValid
    public boolean onSaveButtonClick() {
       boolean result = false;
//        stringRule.validate(editText.getText())
        return result;
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