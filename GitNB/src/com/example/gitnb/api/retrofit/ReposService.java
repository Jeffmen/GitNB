package com.example.gitnb.api.retrofit;

import com.example.gitnb.model.Repository;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Bernat on 08/07/2014.
 */
public interface ReposService {

	//Async
	// User repositories
	@GET("/user/repos?type=owner")
	void userReposList(@Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/user/repos?type=owner")
	void userReposList(@Query("page") int page, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/users/{username}/repos?type=owner")
	void userReposList(@Path("username") String username, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/users/{username}/repos?type=owner")
	void userReposList(@Path("username") String username, @Query("page") int page, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/user/repos?affiliation=organization_member")
	void userReposListFromOrgs(@Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/user/repos?affiliation=organization_member")
	void userReposListFromOrgs(@Query("page") int page, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/orgs/{org}/repos?type=all")
	void orgsReposList(@Path("org") String org, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/orgs/{org}/repos?type=all")
	void orgsReposList(@Path("org") String org, @Query("page") int page, @Query("sort") String sort, Callback<List<Repository>> callback);

	// Starred repos
	@GET("/user/starred?sort=updated")
	void userStarredReposList(@Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/user/starred?sort=updated")
	void userStarredReposList(@Query("page") int page, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/users/{username}/starred?sort=updated")
	void userStarredReposList(@Path("username") String username, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/users/{username}/starred?sort=updated")
	void userStarredReposList(@Path("username") String username, @Query("page") int page, @Query("sort") String sort, Callback<List<Repository>> callback);

	// Wathched repos
	@GET("/user/subscriptions")
	void userSubscribedReposList(@Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/user/subscriptions")
	void userSubscribedReposList(@Query("page") int page, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/users/{username}/subscriptions")
	void userSubscribedReposList(@Path("username") String username, @Query("sort") String sort, Callback<List<Repository>> callback);

	@GET("/users/{username}/subscriptions")
	void userSubscribedReposList(@Path("username") String username, @Query("page") int page, @Query("sort") String sort, Callback<List<Repository>> callback);

	// Member
	@GET("/user/repos?type=member")
	void userMemberRepos(Callback<List<Repository>> callback);
	// Member
	@GET("/user/repos?type=member")
	void userMemberRepos(@Query("page") int page, Callback<List<Repository>> callback);


	//Sync
	// User repositories
	@GET("/user/repos?type=owner")
	List<Repository> userReposList(@Query("sort") String sort);

	@GET("/user/repos?type=owner")
	List<Repository> userReposList(@Query("page") int page, @Query("sort") String sort);

	@GET("/users/{username}/repos?type=owner")
	List<Repository> userReposList(@Path("username") String username, @Query("sort") String sort);

	@GET("/users/{username}/repos?type=owner")
	List<Repository> userReposList(@Path("username") String username, @Query("page") int page, @Query("sort") String sort);

	@GET("/orgs/{org}/repos?type=all")
	List<Repository> orgsReposList(@Path("org") String org, @Query("sort") String sort);

	@GET("/orgs/{org}/repos?type=all")
	List<Repository> orgsReposList(@Path("org") String org, @Query("page") int page, @Query("sort") String sort);

	@GET("/user/repos?affiliation=organization_member")
	List<Repository> userReposListFromOrgs(@Query("sort") String sort);

	@GET("/user/repos?affiliation=organization_member")
	List<Repository> userReposListFromOrgs(@Query("page") int page, @Query("sort") String sort);

	// Starred repos
	@GET("/user/starred?sort=updated")
	List<Repository> userStarredReposList(@Query("sort") String sort);

	@GET("/user/starred?sort=updated")
	List<Repository> userStarredReposList(@Query("page") int page, @Query("sort") String sort);

	@GET("/users/{username}/starred?sort=updated")
	List<Repository> userStarredReposList(@Path("username") String username, @Query("sort") String sort);

	@GET("/users/{username}/starred?sort=updated")
	List<Repository> userStarredReposList(@Path("username") String username, @Query("page") int page, @Query("sort") String sort);

	// Wathched repos
	@GET("/user/subscriptions")
	List<Repository> userSubscribedReposList(@Query("sort") String sort);

	@GET("/user/subscriptions")
	List<Repository> userSubscribedReposList(@Query("page") int page, @Query("sort") String sort);

	@GET("/users/{username}/subscriptions")
	List<Repository> userSubscribedReposList(@Path("username") String username, @Query("sort") String sort);

	@GET("/users/{username}/subscriptions")
	List<Repository> userSubscribedReposList(@Path("username") String username, @Query("page") int page, @Query("sort") String sort);

	// Member
	@GET("/user/repos?type=member")
	List<Repository> userMemberRepos();
	// Member
	@GET("/user/repos?type=member")
	List<Repository> userMemberRepos(@Query("page") int page);
}
