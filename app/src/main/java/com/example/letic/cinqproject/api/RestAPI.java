package com.example.letic.cinqproject.api;

import com.example.letic.cinqproject.models.ItemList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by letic on 07/09/2018.
 */

public interface RestAPI {

    @GET("/photos")
    Call<List<ItemList>> getList();
}
