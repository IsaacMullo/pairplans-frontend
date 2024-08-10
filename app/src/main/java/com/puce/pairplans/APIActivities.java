package com.puce.pairplans;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIActivities {
    @GET("activities/")
    Call<List<Activity>> getActivitiesI(
            @Query("person_id") String person
    );

    @GET("activities/")
    Call<Activity> getActvityI(
            @Query("id") String id
    );

    @DELETE("activities/")
    Call<Void> removeActivitiesI(
            @Query("person_id") String person
    );

}
