package com.joblesscoders.arbook.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestApiHandler {
    private static final String BASE_URL = "https://nawtz1hine.execute-api.ap-south-1.amazonaws.com/Production/";
    private static String SUCCESS = "success";
    private static String FAILURE = "failure";
    private static Retrofit getRetrofitInstance() {

        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory
                        .create(gson))
                .build();
    }

    public static RestApiInterface getAPIService() {
        return getRetrofitInstance().create(RestApiInterface.class);
    }
}

