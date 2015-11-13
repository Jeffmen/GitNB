package com.example.gitnb.module.repos;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.ReposSearchRequest;
import com.example.gitnb.api.ReposSearchRequest.Condition;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HotReposFragment extends Fragment implements HandlerInterface<ArrayList<Repository>>, TextWatcher{
	private String TAG = "HotReposFragment";
	public static String REPOS_KEY = "repos_key";
	private int page = 1;
    private RecyclerView recyclerView;
	private boolean isLoadingMore;
    private HotReposAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private WebRequest currentRequest;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        adapter = new HotReposAdapter(getActivity());
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        adapter.SetOnItemClickListener(new HotReposAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				//Toast.makeText(getActivity(), "item:"+position, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getActivity(), ReposDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(REPOS_KEY, adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
        adapter.SetOnLoadMoreClickListener(new HotReposAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
                if(isLoadingMore){
	                Log.d(TAG,"ignore manually update!");
	            } else{
	             	page++;
	                isLoadingMore = true;
	                requestHotRepos(true, null);
	            }
			}
		});        
        adapter.SetOnSearchClickListener(new HotReposAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
	        	page = 1;
	        	mSwipeRefreshLayout.setRefreshing(true);
	        	requestHotRepos(adapter.getSearchText().isEmpty() ? false : true, adapter.getSearchText());
			}
		});
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
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
            	requestHotRepos(true, null);
            }
        });
        requestHotRepos(false, null);
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
        	requestHotRepos(true, s.toString());
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
	
	@Override
    public void onSuccess(ArrayList<Repository> data){
		onSuccess(data, 0, 1);
    }

	@Override
    public void onSuccess(ArrayList<Repository> data, int totalPages, int currentPage){
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
    
    private void requestHotRepos(boolean refresh, String key){
    	if(currentRequest != null) currentRequest.cancelRequest();
    	ReposSearchRequest request = new ReposSearchRequest(getActivity());
    	Condition condition = request.new Condition();
    	condition.SetLanguage("java");
    	condition.SetRefresh(refresh);
    	condition.SetPage(page);
        condition.SetKey(key);
    	request.SetHandler(this);
    	request.SetSearchCondition(condition);
    	RequestManager.getInstance(getActivity()).addRequest(request);
    	currentRequest = request;
    }
}
