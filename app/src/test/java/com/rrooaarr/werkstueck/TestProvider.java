package com.rrooaarr.werkstueck;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class TestProvider implements LifecycleObserver {

    public boolean wasFired;

    public void bindToLifeCycle(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onClear() {
        wasFired = true;
    }
}
