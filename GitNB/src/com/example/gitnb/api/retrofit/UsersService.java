package com.example.gitnb.api.retrofit;

import java.util.List;

import com.example.gitnb.model.Email;
import com.example.gitnb.model.User;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Bernat on 12/07/2014.
 */
public interface UsersService {

	@GET("users/{user}")
	Call<User> getSingleUser(@Path("user") String user);

	@GET("user/emails")
	Call<List<Email>> userEmails();

	// Followers
	@GET("user/followers")
	Call<List<User>> followers();

	@GET("users/{username}/followers")
	Call<List<User>> followers(@Path("username") String username);

	@GET("user/followers")
	Call<List<User>> followers(@Query("page") int page);

	@GET("users/{username}/followers")
	Call<List<User>> followers(@Path("username") String username, @Query("page") int page);

	// Following
	@GET("user/following")
	Call<List<User>> following();

	@GET("users/{username}/following")
	Call<List<User>> following(@Path("username") String username);

	@GET("user/following")
	Call<List<User>> following(@Query("page") int page);

	@GET("users/{username}/following")
	List<User> following(@Path("username") String username, @Query("page") int page);

	@GET("user")
	Call<User> me();

	// FOLLOWING USER

	@GET("user/following/{username}")
	Call<Object> checkFollowing(@Path("username") String username);

    @Headers("Content-Length: 0")
	@PUT("user/following/{username}")
	Call<Object> followUser(@Body String empty, @Path("username") String username);

	@DELETE("user/following/{username}")
	Call<Object> unfollowUser(@Path("username") String username);


	//ORGS MEMBERS

	@GET("orgs/{org}/members")
	Call<List<User>> orgMembers(@Path("org") String org);

	@GET("orgs/{org}/members")
	Call<List<User>> orgMembers(@Path("org") String org, @Query("page") int page);
}
