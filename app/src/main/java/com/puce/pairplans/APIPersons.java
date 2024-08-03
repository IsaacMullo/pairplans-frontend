package com.puce.pairplans;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIPersons {
    @GET("persons/")
    Call<List<Person>> getPersonsI();

    @GET("API_REST.php")
    Call<Person> getPersonI(
            @Query("id") String id
    );
}
