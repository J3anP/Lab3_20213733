package com.example.pomodoro;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DummyService {

    @FormUrlEncoded
    @POST("/auth/login")
    Call<UserLogin> LOGIN_CALL(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("/todos/user/{userId}")
    Call<UserLogin> LISTA_TAREAS(
            @Path("userId") int userId
    );

}
