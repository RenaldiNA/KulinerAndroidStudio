package com.example.belajarlogin.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitAPIKuliner {
    // passing a parameter as login.php
    @FormUrlEncoded
    @POST("lokasi.php")

//a method to post our data.
    Call<String> STRING_CALL(
            @Field("aksi") String simpan,
            @Field("nama") String nama,
            @Field("keterangan") String keterangan,
            @Field("kontributor") String kontributor,
            @Field("lat") String lat,
            @Field("lon") String lon
    );
}
