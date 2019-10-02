package com.rrooaarr.werkstueck.booking.api;

import com.rrooaarr.werkstueck.booking.model.WorkpieceContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookingWebservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("word") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/rest/service/wst/{pk}")
    Call<WorkpieceContainer> getWorkpieceInfo(@Path("pk") String workpieceNumber);

    @PUT("/rest/service/wst/{pk}/{action}")
    Call<Void> bookWorkpieceAction(@Path("pk") String pk, @Path("action") String action);
}