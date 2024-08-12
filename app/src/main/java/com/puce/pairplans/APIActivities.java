package com.puce.pairplans;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIActivities {
    @GET("activities/")
    Call<List<Activity>> getActivitiesI(
            @Query("person_id") String person
    );

    @POST("activities/")
    Call<Void> addActivtyI(
            @Body Activity activity
    );

    @DELETE("activities/{id}/")
    Call<Void> removeActivityI(
            @Path("id") String id
    );

    @PUT("activities/{id}/")
    Call<Void> updateActivityI(
            @Path("id") String id,
            @Body Activity activity
    );

}
