package com.example.gitnb.api.retrofit;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

import com.example.gitnb.model.Repository;

public interface TrendingService {

	@GET("v2/trending?")
	Call<List<Repository>> trendingReposList(@Query("language") String language, @Query("since") String since);
}
