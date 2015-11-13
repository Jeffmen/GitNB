package com.example.gitnb.module.trending;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.TrendingReposRequest;
import com.example.gitnb.api.TrendingReposRequest.Condition;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.repos.HotReposAdapter;
import com.example.gitnb.module.repos.ReposDetailActivity;
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

public class TrendingReposFragment extends Fragment implements HandlerInterface<ArrayList<Repository>>, TextWatcher{
	private String TAG = "TrendingReposFragment";
	public static String REPOS_KEY = "repos_key";
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
	                isLoadingMore = true;
	                requestTrendingRepos(true, null);
	            }
			}
		});        
        adapter.SetOnSearchClickListener(new HotReposAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
	        	mSwipeRefreshLayout.setRefreshing(true);
	        	requestTrendingRepos(adapter.getSearchText().isEmpty() ? false : true, adapter.getSearchText());
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
            	requestTrendingRepos(true, null);
            }
        });
        requestTrendingRepos(false, null);
        return view;
    }
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){
		if(s.length() > 0){
        	requestTrendingRepos(true, s.toString());
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
    	adapter.update(data);
    }

	@Override
    public void onFailure(String error){
    	mSwipeRefreshLayout.setRefreshing(false);
    	adapter.reset();
        MessageUtils.showErrorMessage(getActivity(), error);
    }
    
    private void requestTrendingRepos(boolean refresh, String key){
    	if(currentRequest != null) currentRequest.cancelRequest();
    	TrendingReposRequest request = new TrendingReposRequest(getActivity());
    	Condition condition = request.new Condition();
    	condition.SetSince("daily");
    	//condition.SetLanguage("java");
    	condition.SetRefresh(refresh);
    	request.SetHandler(this);
    	request.SetSearchCondition(condition);
    	RequestManager.getInstance(getActivity()).addRequest(request);
    	currentRequest = request;
    }
}