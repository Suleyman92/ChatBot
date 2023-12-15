package com.example.asistant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {

    @GET
    Call<MsgModal> getMEssage(@Url String url);

}
