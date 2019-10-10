package com.rrooaarr.werkstueck;

import android.os.SystemClock;

import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;
import com.rrooaarr.werkstueck.booking.api.BookingWebservice;
import com.rrooaarr.werkstueck.booking.api.RetrofitServiceGenerator;
import com.rrooaarr.werkstueck.booking.model.Action;
import com.rrooaarr.werkstueck.booking.model.WorkpieceContainer;

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
            final Response<WorkpieceContainer> workpieceInfo = api.getWorkpieceInfo("17935-11-719").execute();
            JSONObject jsonObject = new JSONObject(new Gson().toJson(workpieceInfo.body()));
            assertTrue(workpieceInfo.isSuccessful());
        }

        @Test
        public void testWorkpieceNumberOverTripple() throws Exception {
            final MutableLiveData<WorkpieceContainer> workpieceMutableLiveData = new MutableLiveData<>();
            Call<WorkpieceContainer> callAsync = api.getWorkpieceInfo("17935-11-719");

            callAsync.enqueue(new Callback<WorkpieceContainer>() {
                @Override
                public void onResponse(Call<WorkpieceContainer> call, Response<WorkpieceContainer> response) {
                    if (response.isSuccessful()) {
                        workpieceMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<WorkpieceContainer> call, Throwable throwable) {
                    workpieceMutableLiveData.setValue(null);
                }
            });
            SystemClock.sleep(2000);
            assertNotNull(workpieceMutableLiveData.getValue());
        }

    @Test
    public void testWorkpieceNumberOverPk() throws Exception {
        final MutableLiveData<WorkpieceContainer> workpieceMutableLiveData = new MutableLiveData<>();
        Call<WorkpieceContainer> callAsync = api.getWorkpieceInfo("183565");

        callAsync.enqueue(new Callback<WorkpieceContainer>() {
            @Override
            public void onResponse(Call<WorkpieceContainer> call, Response<WorkpieceContainer> response) {
                if (response.isSuccessful()) {
                    workpieceMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<WorkpieceContainer> call, Throwable throwable) {
                workpieceMutableLiveData.setValue(null);
            }
        });
        SystemClock.sleep(2000);
        assertNotNull(workpieceMutableLiveData.getValue());
    }

    @Test
    public void testBookingOverPk() throws Exception {
        // TODO make tests async
        MutableLiveData<Boolean> finishingBooking = doBookingAction("183565", Action.FINISHING);
        SystemClock.sleep(2000);
        assertNotNull(finishingBooking.getValue());

        MutableLiveData<Boolean> shippingBooking = doBookingAction("183565", Action.SHIPPING);
        SystemClock.sleep(2000);
        assertNotNull(shippingBooking.getValue());

        MutableLiveData<Boolean> packagingBooking = doBookingAction("183565", Action.PACKAGING);
        SystemClock.sleep(2000);
        assertNotNull(packagingBooking.getValue());
    }

    @Test
    public void testBookingOverPkFailAction() throws Exception {
        // TODO make tests async
        MutableLiveData<Boolean> packagingBooking = doBookingAction("9999999999999999999", Action.PACKAGING);
        SystemClock.sleep(2000);
        assertNotNull(packagingBooking.getValue());

        MutableLiveData<Boolean> packagingBooking3 = doBookingAction("0", Action.PACKAGING);
        SystemClock.sleep(2000);
        assertNotNull(packagingBooking3.getValue());

        MutableLiveData<Boolean> packagingBooking2 = doBookingAction(null, Action.PACKAGING);
        SystemClock.sleep(2000);
        assertNotNull(packagingBooking2.getValue());

        MutableLiveData<Boolean> finishingBooking = doBookingAction("alles", Action.FINISHING);
        SystemClock.sleep(2500);
        assertNotNull(finishingBooking.getValue());

        MutableLiveData<Boolean> shippingBooking = doBookingAction("010101010", Action.SHIPPING);
        SystemClock.sleep(2000);
        assertNotNull(shippingBooking.getValue());
    }

    private MutableLiveData<Boolean> doBookingAction(String pk , Action action){
        String actionStr = "";
        final MutableLiveData<Boolean> bookresult = new MutableLiveData<>();
        switch (action){
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
                if (response.isSuccessful()){
                    bookresult.setValue(true);
                } else {
                    bookresult.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                bookresult.setValue(false);
            }
        });

        return bookresult;
    }

    @Test
    public void testJson() throws Exception {

        final WorkpieceContainer workpieceContainer = new WorkpieceContainer();
        workpieceContainer.setPk("1898394");
        List<String> wst_list = Arrays.asList("Werkstuecknummer", "017935/011/0719", "ProjektID","017935/011", "FANummer", "43203", "LeitungMitarbeiterName", "Gerd Müller" );
//        workpieceContainer.setWst_infos(wst_list);
//
//        try {
//            JSONObject jsonObject = new JSONObject(new Gson().toJson(workpieceContainer));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

}