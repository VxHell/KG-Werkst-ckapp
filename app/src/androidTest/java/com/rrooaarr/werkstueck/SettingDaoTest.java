package com.rrooaarr.werkstueck;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rrooaarr.werkstueck.setting.UserSetting;
import com.rrooaarr.werkstueck.setting.UserSettingDao;
import com.rrooaarr.werkstueck.setting.UserSettingsRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4.class)
public class SettingDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private UserSettingDao settingDao;
    private UserSettingsRoomDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        mDb = Room.inMemoryDatabaseBuilder(context, UserSettingsRoomDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        settingDao = mDb.settingDao();
        // init Mockito
//        MockitoAnnotations.initMocks(this);
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void insertAndGetSetting() {
        String server = "server";
        String port = "port";
        String username = "username";
        String passwort = "passwort";
        UserSetting setting = new UserSetting(server, port, username, passwort);
        settingDao.insert(setting);
        UserSetting settingLoaded = settingDao.getSettingSync();

        assertEquals(server, settingLoaded.getServer());
        assertEquals(port, settingLoaded.getPort());
        assertEquals(username, settingLoaded.getUsername());
        assertEquals(passwort, settingLoaded.getPassword());

    }

    @Test
    public void insertAndUpdateSetting() throws Exception {
        UserSetting setting = new UserSetting("test", "test", "test", "test");
        settingDao.insert(setting);
        UserSetting settingUpdate = new UserSetting("test2", "test2", "test2", "test2");
        settingDao.update(settingUpdate);
        List<UserSetting> allsettings = LiveDataTestUtil.getValue(settingDao.getAlphabetizedSettings());
        assertEquals(allsettings.get(0).getServer(), settingUpdate.getServer());
        assertEquals(allsettings.get(0).getPassword(), settingUpdate.getPassword());
        assertEquals(allsettings.get(0).getPort(), settingUpdate.getPort());
        assertEquals(allsettings.get(0).getUsername(), settingUpdate.getUsername());
    }

    @Test
    public void insertAndUpdatePropertiesSetting() throws Exception {
        UserSetting setting = new UserSetting("test", "test", "test", "test");
        long primary = settingDao.insert(setting);
        UserSetting settingUpdate = new UserSetting("test2", "test2", "test2", "test2");
        settingDao.updateProperties(primary, settingUpdate.getServer(), settingUpdate.getPort(), settingUpdate.getUsername(), settingUpdate.getPassword());
        List<UserSetting> allsettings = LiveDataTestUtil.getValue(settingDao.getAlphabetizedSettings());
        assertEquals(allsettings.get(0).getServer(), settingUpdate.getServer());
        assertEquals(allsettings.get(0).getPassword(), settingUpdate.getPassword());
        assertEquals(allsettings.get(0).getPort(), settingUpdate.getPort());
        assertEquals(allsettings.get(0).getUsername(), settingUpdate.getUsername());
    }

}
