package com.puce.pairplans;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {
    Retrofit retrofit;

    public RetrofitAdapter() {

    }
    public Retrofit getAdapter(){
        String URL = "https://pairplansbackend-production.up.railway.app/";
        //String URL = "http://10.0.2.2:8000/";
        //String URL = "http://192.168.100.11:8000/";

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return  retrofit;
    }
}