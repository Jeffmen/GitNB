package com.example.gitnb.api.retrofit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;
import retrofit.http.Query;

import com.example.gitnb.model.search.UsersSearch;
//http://blog.csdn.net/lmj623565791/article/details/48129405
public class SearchClient extends RetrofitNetworkAbs{
	private SearchService searchService;
	
	private SearchClient(){
	    searchService = ApiRetrofit.getInstance().getRetrofit().create(SearchService.class);
	}
	
    public static SearchClient getNewInstance() {
        return new SearchClient();
    }
    
	public void users(String query, int page){
		execute(searchService.usersPaginated(query, page));
	}
	
	public void users(String query, String sort, String order, int page){
		execute(searchService.usersPaginated(query, sort, order, page));
	}
	
	public void reposPaginated(String query, int page){
		execute(searchService.reposPaginated(query, page));
	}
	
	public void reposPaginated(String query, String sort, String order, int page){
		execute(searchService.reposPaginated(query, sort, order, page));
	}
	
	private void execute(Call call){
		call.enqueue(new Callback() {

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }

			@Override
			public void onResponse(retrofit.Response response, Retrofit retrofit) {
                myOnResponse(response);
			}
        });
	}
	
	@Override
	public SearchClient setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
	}

}
