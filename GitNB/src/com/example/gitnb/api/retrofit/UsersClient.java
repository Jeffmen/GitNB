package com.example.gitnb.api.retrofit;

public class UsersClient extends RetrofitNetworkAbs{

	private UsersService usersService;
	
	private UsersClient(){
		usersService = ApiRetrofit.getRetrofit().create(UsersService.class);
	}
	
    public static UsersClient getNewInstance() {
        return new UsersClient();
    }
    
	public void getSingleUser(String username){
		execute(usersService.getSingleUser(username));
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
	
	public void following(String username, int page){
		execute(usersService.following(username, page));
	}
	
	public void followers(String username, int page){
		execute(usersService.followers(username, page));
	}
	
	public void userReposList(String sort, int page){
		execute(usersService.userReposList(sort, page));
	}
	
	public void userReposList(String username, String sort, int page){
		execute(usersService.userReposList(username, sort, page));
	}
	
	@Override
	public UsersClient setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
	}

}
