package com.sygn.xeapp.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("xrider-login-registerPhoneNum/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
