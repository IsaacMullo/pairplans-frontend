package com.puce.pairplans;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIPersons {
    @GET("persons/")
    Call<List<Person>> getPersonsI();


    @POST("persons/")
    Call<Void> addPersonI(
            @Body Person person
    );

    @DELETE("persons/{id}/")
    Call<Void> removePersonI(
            @Path("id") String id
    );

    @PUT("persons/{id}/")
    Call<Void> updatePersonI(
            @Path("id") String id,
            @Body Person person
    );

}
