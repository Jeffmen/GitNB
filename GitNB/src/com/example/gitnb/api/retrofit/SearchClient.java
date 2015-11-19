package com.example.gitnb.api.retrofit;

import retrofit.Callback;
import retrofit.Retrofit;
import com.example.gitnb.model.search.UsersSearch;

public class SearchClient extends RetrofitNetworkAbs{
	
    public static SearchClient getNewInstance() {
        return new SearchClient();
    }
    
	public void users(String query, int page){
		SearchService searchService = ApiRetrofit.getInstance().getRetrofit().create(SearchService.class);
        searchService.usersPaginated(query, page).enqueue(new Callback<UsersSearch>() {

            @Override
            public void onFailure(Throwable t) {
                myOnFailure(t);
            }

			@Override
			public void onResponse(retrofit.Response<UsersSearch> response, Retrofit retrofit) {
                myOnResponse(response);
			}
        });
	}
	
	@Override
	public SearchClient setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
	}

}
