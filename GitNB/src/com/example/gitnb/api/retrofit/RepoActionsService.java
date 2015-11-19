package com.example.gitnb.api.retrofit;

import com.example.gitnb.model.Repository;

import retrofit.Call;
import retrofit.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Bernat on 07/08/2014.
 */
public interface RepoActionsService {

    @GET("user/starred/{owner}/{name}")
    Call<Response> checkIfRepoIsStarred(@Path("owner") String owner, @Path("name") String repo);

    @Headers("Content-Length: 0")
    @PUT("user/starred/{owner}/{name}")
    Call<Response> starRepo(@Path("owner") String owner, @Path("name") String repo, @Body String empty);

    @DELETE("/user/starred/{owner}/{name}")
    Call<Response> unstarRepo(@Path("owner") String owner, @Path("name") String repo);

    @GET("user/subscriptions/{owner}/{name}")
    Call<Response> checkIfRepoIsWatched(@Path("owner") String owner, @Path("name") String repo);

    @Headers("Content-Length: 0")
    @PUT("user/subscriptions/{owner}/{name}")
    Call<Response> watchRepo(@Path("owner") String owner, @Path("name") String repo, @Body String empty);

    @DELETE("user/subscriptions/{owner}/{name}")
    Call<Response> unwatchRepo(@Path("owner") String owner, @Path("name") String repo);

    @Headers("Content-Length: 0")
    @POST("repos/{owner}/{name}/forks")
    Call<Repository> forkRepo(@Path("owner") String owner, @Path("name") String repo, @Body Object empty);

    @Headers("Content-Length: 0")
    @POST("repos/{owner}/{name}/forks")
    Call<Repository> forkRepo(@Path("owner") String owner, @Path("name") String repo, @Query("organization") String org, @Body Object empty);
}
