package com.example.belajarlogin.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitAPIRegister {
    // passing a parameter as login.php
    @FormUrlEncoded
    @POST("insert_user.php")

//a method to post our data.
    Call<String> STRING_CALL(
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("password") String password
    );
}
