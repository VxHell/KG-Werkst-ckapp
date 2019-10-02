package com.rrooaarr.werkstueck;

import android.os.SystemClock;

import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;
import com.rrooaarr.werkstueck.booking.api.BookingWebservice;
import com.rrooaarr.werkstueck.booking.api.RetrofitServiceGenerator;
import com.rrooaarr.werkstueck.booking.model.Workpiece;
import com.rrooaarr.werkstueck.booking.model.Workpiece2;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;


@RunWith(AndroidJUnit4.class)
public class ApiTest {

        private BookingWebservice api;

        @Before
        public void setUp() throws Exception {
//            api = RetrofitServiceGenerator.createService(BookingWebservice.class, "https://kg-entwicklung:443/rest/service/wst/", "Fertigung", "1e604b570f9466c5924bdb37cf3eb00d01fb49f11aecccd01a761e6c513465e823f0e1f7b9126965fa6a8e0a562773dbee4b6dd768f310af095ef63b17764638");
            api = RetrofitServiceGenerator.createService(BookingWebservice.class, "https://192.168.2.104:443/rest/service/wst/", "Fertigung", "1e604b570f9466c5924bdb37cf3eb00d01fb49f11aecccd01a761e6c513465e823f0e1f7b9126965fa6a8e0a562773dbee4b6dd768f310af095ef63b17764638");
        }

        @Test
        public void testWorkpieceNumberSync() throws Exception {
            final Response<Workpiece> workpieceInfo = api.getWorkpieceInfo("17935-11-719").execute();
            JSONObject jsonObject = new JSONObject(new Gson().toJson(workpieceInfo.body()));
            assertTrue(workpieceInfo.isSuccessful());
        }

        @Test
        public void testWorkpieceNumberOverTrippleAsync() throws Exception {
            final MutableLiveData<Workpiece> workpieceMutableLiveData = new MutableLiveData<>();
            Call<Workpiece> callAsync = api.getWorkpieceInfo("17935-11-719");

            callAsync.enqueue(new Callback<Workpiece>() {
                @Override
                public void onResponse(Call<Workpiece> call, Response<Workpiece> response) {
                    if (response.isSuccessful()) {
                        workpieceMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Workpiece> call, Throwable throwable) {
                    workpieceMutableLiveData.setValue(null);
                }
            });
            SystemClock.sleep(2000);
            assertNotNull(workpieceMutableLiveData.getValue());
        }

    @Test
    public void testWorkpieceNumberOverPkAsync() throws Exception {
        final MutableLiveData<Workpiece> workpieceMutableLiveData = new MutableLiveData<>();
        Call<Workpiece> callAsync = api.getWorkpieceInfo("183565");

        callAsync.enqueue(new Callback<Workpiece>() {
            @Override
            public void onResponse(Call<Workpiece> call, Response<Workpiece> response) {
                if (response.isSuccessful()) {
                    workpieceMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Workpiece> call, Throwable throwable) {
                workpieceMutableLiveData.setValue(null);
            }
        });
        SystemClock.sleep(2000);
        assertNotNull(workpieceMutableLiveData.getValue());
    }

    @Test
    public void testJson() throws Exception {

        final Workpiece2 workpiece2 = new Workpiece2();
        workpiece2.setPk("1898394");
        List<String> wst_list = Arrays.asList("Werkstuecknummer", "017935/011/0719", "ProjektID","017935/011", "FANummer", "43203", "LeitungMitarbeiterName", "Gerd Müller" );
        workpiece2.setWst_infos(wst_list);

        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(workpiece2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}