package com.example.gitnb.api.retrofit;

import com.example.gitnb.model.search.ReposSearch;
import com.example.gitnb.model.search.UsersSearch;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Bernat on 08/08/2014.
 */
public interface SearchService {

    @GET("search/repositories")
    Call<ReposSearch> repos(@Query("q") String query);

    @GET("search/repositories")
    Call<ReposSearch> reposPaginated(@Query("q") String query, @Query("page") int page);    
    
    @GET("search/repositories")
    Call<ReposSearch> reposPaginated(@Query("q") String query, @Query("sort") String sort, @Query("order") String order, @Query("page") int page);


    @GET("search/users")
    Call<UsersSearch> users(@Query("q") String query);
    
    @GET("search/users")
    Call<UsersSearch> usersPaginated(@Query("q") String query, @Query("page") int page);
    
    @GET("search/users")
    Call<UsersSearch> usersPaginated(@Query("q") String query, @Query("sort") String sort, @Query("order") String order, @Query("page") int page);
    
}
