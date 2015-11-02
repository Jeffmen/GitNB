package com.example.gitnb.module.user;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.api.UserSearchRequest;
import com.example.gitnb.api.UserSearchRequest.UserCondition;
import com.example.gitnb.model.HotUser;
import com.example.gitnb.utils.MessageUtils;

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
import android.widget.Toast;

public class HotUserFragment extends Fragment implements HandlerInterface<ArrayList<HotUser>>, TextWatcher{
	private String TAG = "HotUserFragment";
	private int page = 1;
    private RecyclerView recyclerView;
	private boolean isLoadingMore;
    private HotUserAdapter adapter;
    private RequestManager requestManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private WebRequest currentRequest;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        adapter = new HotUserAdapter(getActivity());
        //adapter.SetSearchTextWatcher(this);
        adapter.SetOnItemClickListener(new HotUserAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				Toast.makeText(getActivity(), "item:"+position, Toast.LENGTH_SHORT).show();
			}
		});
        adapter.SetOnLoadMoreClickListener(new HotUserAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
                if(isLoadingMore){
	                Log.d(TAG,"ignore manually update!");
	            } else{
	             	page++;
	             	requestHotUser(true, null);
	                isLoadingMore = true;
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
        requestManager = RequestManager.getInstance(getActivity());
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
    public void onSuccess(ArrayList<HotUser> data){
		onSuccess(data, 0, 1);
    }

	@Override
    public void onSuccess(ArrayList<HotUser> data, int totalPages, int currentPage){
    	mSwipeRefreshLayout.setRefreshing(false);
    	if(page == 1){
        	adapter.update(data);
    	}
    	else{
        	adapter.insertAtBack(data);
            isLoadingMore = false;
    	}
    }

	@Override
    public void onFailure(String error){
    	mSwipeRefreshLayout.setRefreshing(false);
    	adapter.reset();
        MessageUtils.showErrorMessage(getActivity(), error);
    }
    
    private void requestHotUser(boolean refresh, String key){
    	if(currentRequest != null) currentRequest.cancelRequest();
    	UserSearchRequest request = new UserSearchRequest(getActivity());
    	UserCondition condition = request.new UserCondition();
    	condition.SetLanguage("java");
    	condition.SetLocation("china");
    	condition.SetRefresh(refresh);
    	condition.SetPage(page);
        condition.SetKey(key);
    	request.SetHandler(this);
    	request.SetSearchCondition(condition);
    	requestManager.addRequest(request);
    	currentRequest = request;
    }
}
