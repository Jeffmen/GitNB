package com.example.gitnb.api.retrofit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class UsersClient extends RetrofitNetworkAbs{

	private UsersService usersService;
	
	private UsersClient(){
		usersService = ApiRetrofit.getRetrofit().create(UsersService.class);
	}
	
    public static UsersClient getNewInstance() {
        return new UsersClient();
    }
	
	public void followUser(String username){
		execute(usersService.followUser(username));
	}
	
	public void checkFollowing(String username){
		execute(usersService.checkFollowing(username));
	}
	
	public void unfollowUser(String username){
		execute(usersService.checkFollowing(username));
	}
	
	@Override
	public UsersClient setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
	}

}
