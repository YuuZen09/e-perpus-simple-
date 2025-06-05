package com.example.testapi.api;

import com.example.testapi.model.login.Login;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.Call;

public interface APIInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("no_anggota") String no_anggota,
            @Field("password") String password
    );


}
