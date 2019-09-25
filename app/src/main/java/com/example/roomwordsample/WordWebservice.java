package com.example.roomwordsample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WordWebservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("word") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/word/{word}")
    Call<Word> getWord(@Path("word") String wordId);

}