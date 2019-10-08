package com.rrooaarr.werkstueck;

import androidx.appcompat.app.AppCompatActivity;

import com.rrooaarr.werkstueck.setting.SettingsActivity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml")
public class Robotest {

    @Test
    public void shouldFireOnClear(){

        //Grab the Activity controller
        ActivityController controller = Robolectric.buildActivity(SettingsActivity.class).create().start();
        AppCompatActivity activity = (AppCompatActivity) controller.get();

        //Instanciate our Observer
        TestProvider testProvider = new TestProvider();
        testProvider.bindToLifeCycle(activity);

        //Fire the expected event
        controller.stop();

        //Assert
        Assert.assertTrue(testProvider.wasFired);
    }
}

