package com.example.gitnb.module.user;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.api.retrofit.SearchClient;
import com.example.gitnb.model.User;
import com.example.gitnb.model.search.UsersSearch;
import com.example.gitnb.module.MainActivity.UpdateLanguageListener;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HotUserFragment extends Fragment implements HandlerInterface<ArrayList<User>>, UpdateLanguageListener{
	private String TAG = "HotUserFragment";
	public static String USER = "user_key";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private UserListAdapter adapter;
	private boolean isLoadingMore;
    private String language;
    private String location;
	private int page;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        page = 1;
        language = "java";
        adapter = new UserListAdapter(getActivity());
        adapter.setShowSearch(true);
        adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(getActivity(), UserDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(USER, adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
        adapter.setOnLoadMoreClickListener(new UserListAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
                if(isLoadingMore){
	                Log.d(TAG,"ignore manually update!");
	            } else{
	             	page++;
	                isLoadingMore = true;
	             	requestHotUser(null);
	            }
			}
		});        
        adapter.setOnSearchClickListener(new UserListAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
	        	page = 1;
	        	mSwipeRefreshLayout.setRefreshing(true);
	        	requestHotUser(adapter.getSearchText());
			}
		});
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        /*
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = adapter.getItemCount();

                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if(isLoadingMore){
                        Log.d(TAG,"ignore manually update!");
                    } else{
	                   	page++;
	                   	requestHotUser(true);
                        isLoadingMore = true;
                    }
                }
            }
        });*/
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(
        		android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	page = 1;
            	requestHotUser(null);
            }
        });
        requestHotUser(null);
        return view;
    }
	
	@Override
    public void onSuccess(ArrayList<User> data){
		onSuccess(data, 0, 1);
    }

	@Override
    public void onSuccess(ArrayList<User> data, int totalPages, int currentPage){
    	mSwipeRefreshLayout.setRefreshing(false);
    	if(page == 1){
        	adapter.update(data);
    	}
    	else{
            isLoadingMore = false;
        	adapter.insertAtBack(data);
    	}
    }

	@Override
    public void onFailure(String error){
    	mSwipeRefreshLayout.setRefreshing(false);
    	adapter.reset();
        MessageUtils.showErrorMessage(getActivity(), error);
    }
	
	public void selectLanguage(String language){
		
	}
    
    private void requestHotUser(String key){
    	String q = "";
		if(key != null && !key.isEmpty())
		{
			q += key;
		}
		if(location != null && !location.isEmpty())
		{
			q += "+location:" + location;
		}
		if(language != null && !language.isEmpty())
		{
			q += "+language:" + language;
		}
    	//q += "+followers:%3E200";
    	
    	SearchClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener<UsersSearch>() {

			@Override
			public void onOK(UsersSearch userSearch) {
				onSuccess((ArrayList<User>) userSearch.items);
			}

			@Override
			public void onError(String Message) {
				onFailure(Message);
			}
			
    	}).users(q, "followers", "desc", page);
    }

	@Override
	public Void updateLanguage(String language) {
		this.language = language;
    	mSwipeRefreshLayout.setRefreshing(true);
    	requestHotUser(null);
		return null;
	}
}
