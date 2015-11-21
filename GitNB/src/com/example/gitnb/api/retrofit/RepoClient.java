package com.example.gitnb.api.retrofit;

import retrofit.http.Path;

public class RepoClient extends RetrofitNetworkAbs{

	private RepoService repoService;
	
	private RepoClient(){
		repoService = ApiRetrofit.getInstance().getRetrofit().create(RepoService.class);
	}
	
    public static RepoClient getNewInstance() {
        return new RepoClient();
    }	
    
    public void get(String owner, String repo){
		execute(repoService.get(owner, repo));
	}
    
    public void contents(String owner, String repo){
		execute(repoService.contents(owner, repo));
	}
    
    public void readme(String owner, String repo){
		execute(repoService.readme(owner, repo));
	}
    
    public void contents(String owner, String repo, String path){
		execute(repoService.contents(owner, repo, path));
	}
    
    public void contributors(String owner, String repo){
		execute(repoService.contributors(owner, repo));
	}
    
    public void stargazers(String owner, String repo){
		execute(repoService.stargazers(owner, repo));
	}
    
    public void delete(String owner, String repo){
		execute(repoService.delete(owner, repo));
	}
    
	@Override
	public RepoClient setNetworkListener(NetworkListener networkListener) {

        return setNetworkListener(networkListener, this);
	}

}
