package com.example.letic.cinqproject.api;

public class ApiUtils {

    static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static RestAPI getSOService(){
        return RetrofitClient.getClient(BASE_URL).create(RestAPI.class);
    }
}