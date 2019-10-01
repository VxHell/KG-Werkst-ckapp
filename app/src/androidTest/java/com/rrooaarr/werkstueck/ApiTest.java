package com.rrooaarr.werkstueck;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;
import com.rrooaarr.werkstueck.booking.api.BookingWebservice;
import com.rrooaarr.werkstueck.booking.api.RetrofitServiceGenerator;
import com.rrooaarr.werkstueck.booking.model.Workpiece;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Response;

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

}