package com.rrooaarr.werkstueck.util;

import android.util.Log;

import retrofit2.Response;

public final class retrofitHelper {

    public static void logRetrofitError(String tag, Response response){
        try {
            Log.e(tag, "onResponse: " + (response.errorBody() != null ? response.errorBody().string() : " empty errorBody"));
            Log.e(tag, "calling url: " + response.raw().request().url().toString());
            Log.e(tag, "calling url: " + response.raw().message());
            Log.e(tag, " calling Host: " + response.raw().request().url().host());
        } catch (Exception e) {
            Log.e(tag, Log.getStackTraceString(e));
        }
    }
}
