package com.rrooaarr.werkstueck;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

/**
 * Creating Instances of LiveData usually here
 * Views - Fragments and Activities - shouldn’t be able of updating LiveData and thus their own state because that’s the responsibility of ViewModels. Views should be able to only observe LiveData.
 * we should encapsulate access to MutableLiveData with eg. getters or backing properties:
 *
 * Caution: A ViewModel must never reference a view, Lifecycle, or any class that may hold a reference to the activity context.
 *
 * There should be no imports of anything from theandroid.view or android.widgetpackages.
 */
public class MainViewModel extends AndroidViewModel {

    private String navtitel = "Menü";
    private String titel = "Werkstücke";
    private String bottomtitel = "KG Nellingen";

    public MainViewModel(Application application) {
        super(application);
//        fetchWordOverApi();
    }

    public String getNavtitel() {
        return navtitel;
    }


    public String getTitel() {
        return titel;
    }


    public String getBottomtitel() {
        return bottomtitel;
    }

}