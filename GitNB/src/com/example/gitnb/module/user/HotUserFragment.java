package com.example.gitnb.module.user;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.api.UserSearchRequest;
import com.example.gitnb.api.UserSearchRequest.Condition;
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
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HotUserFragment extends Fragment implements HandlerInterface<ArrayList<User>>, UpdateLanguageListener, TextWatcher{
	private String TAG = "HotUserFragment";
	public static String USER_KEY = "user_key";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private WebRequest currentRequest;
    private RecyclerView recyclerView;
    private HotUserAdapter adapter;
	private boolean isLoadingMore;
    private String language;
	private int page;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        page = 1;
        language = "java";
        adapter = new HotUserAdapter(getActivity());
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        //adapter.SetSearchTextWatcher(this);
        adapter.SetOnItemClickListener(new HotUserAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				//Toast.makeText(getActivity(), "item:"+position, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getActivity(), UserDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(USER_KEY, adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
        adapter.SetOnLoadMoreClickListener(new HotUserAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
                if(isLoadingMore){
	                Log.d(TAG,"ignore manually update!");
	            } else{
	             	page++;
	                isLoadingMore = true;
	             	requestHotUser(true, null);
	            }
			}
		});        
        adapter.SetOnSearchClickListener(new HotUserAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
	        	page = 1;
	        	mSwipeRefreshLayout.setRefreshing(true);
	        	requestHotUser(adapter.getSearchText().isEmpty() ? false : true, adapter.getSearchText());
			}
		});
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
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
            	requestHotUser(true, null);
            }
        });
        requestHotUser(false, null);
        return view;
    }
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){
		if(s.length() > 0){
        	page = 1;
        	requestHotUser(true, s.toString());
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
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
    
    private void requestHotUser(boolean refresh, String key){
    	/*
    	if(currentRequest != null) currentRequest.cancelRequest();
    	UserSearchRequest request = new UserSearchRequest(getActivity());
    	Condition condition = request.new Condition();
    	condition.SetLanguage(language);
    	condition.SetLocation("china");
    	condition.SetRefresh(refresh);
    	condition.SetPage(page);
        condition.SetKey(key);
    	request.SetHandler(this);
    	request.SetSearchCondition(condition);
    	RequestManager.getInstance(getActivity()).addRequest(request);
    	currentRequest = request;*/
    	SearchClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				UsersSearch userSearch = (UsersSearch)ts;
				onSuccess((ArrayList<User>) userSearch.items);
			}

			@Override
			public void onError(String Message) {
				onFailure(Message);
			}
			
    	}).users("location:world", page);
    }

	@Override
	public Void updateLanguage(String language) {
		this.language = language;
    	mSwipeRefreshLayout.setRefreshing(true);
    	requestHotUser(true, null);
		return null;
	}
}
