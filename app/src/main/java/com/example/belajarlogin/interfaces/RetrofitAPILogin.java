package com.example.belajarlogin.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitAPILogin {
    // passing a parameter as login.php
    @FormUrlEncoded
    @POST("login.php")

//a method to post our data.
    Call<String> STRING_CALL(
            @Field("email") String email,
            @Field("password") String password
    );
}
