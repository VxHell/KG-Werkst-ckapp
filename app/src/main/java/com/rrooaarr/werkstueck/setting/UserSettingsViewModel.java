package com.rrooaarr.werkstueck.setting;

import android.app.Application;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rrooaarr.werkstueck.setting.model.SettingFields;

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

    // TODO Experimental approach
    private SettingFields login;
    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPassword;
    private MutableLiveData<SettingFields> buttonClick = new MutableLiveData<>();

    private String navtitel = "Einstellungen";
    private String titel = "Werkstücke";
    private String bottomtitel = "KG Nellingen";

    public UserSettingsViewModel(Application application) {
        super(application);
        mRepository = new SettingsRepository(application);
        setting = mRepository.getSetting();
//        fetchWordOverApi();
    }

    // Start Experimental approach
    void init() {
        login = new SettingFields();
        onFocusEmail =  new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean focused) {
                EditText et = (EditText) view;
                if (et.getText().length() > 0 && !focused) {
                    login.isEmailValid(true);
                }
            }
        };

        onFocusPassword = new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean focused) {
                EditText et = (EditText) view;
                if (et.getText().length() > 0 && !focused) {
                    login.isPasswordValid(true);
                }
            }
        };
    }

    public SettingFields getSettings() {
        return login;
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener() {
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getPasswordOnFocusChangeListener() {
        return onFocusPassword;
    }

    public void onButtonClick() {
        if (login.isValid()) {
            buttonClick.setValue(login);
        }
    }

    public MutableLiveData<SettingFields> getButtonClick() {
        return buttonClick;
    }

    @BindingAdapter("error")
    public static void setError(EditText editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }

    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText editText, View.OnFocusChangeListener onFocusChangeListener) {
        if (editText.getOnFocusChangeListener() == null) {
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

    // END of Experimental code

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