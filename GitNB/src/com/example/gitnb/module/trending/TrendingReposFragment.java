package com.example.gitnb.module.trending;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.TrendingReposRequest;
import com.example.gitnb.api.TrendingReposRequest.Condition;
import com.example.gitnb.api.RequestManager;
import com.example.gitnb.api.RequestManager.WebRequest;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.MainActivity.UpdateLanguageListener;
import com.example.gitnb.module.repos.HotReposFragment;
import com.example.gitnb.module.repos.ReposDetailActivity;
import com.example.gitnb.module.viewholder.HorizontalDividerItemDecoration;
import com.example.gitnb.utils.MessageUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TrendingReposFragment extends Fragment implements HandlerInterface<ArrayList<Repository>>, UpdateLanguageListener{
	private String TAG = "TrendingReposFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private TrendingReposAdapter adapter;
    private RecyclerView recyclerView;
    private WebRequest currentRequest;
    private String language;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        adapter = new TrendingReposAdapter(getActivity());
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        adapter.SetOnItemClickListener(new TrendingReposAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(getActivity(), ReposDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS, adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
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
            	requestTrendingRepos(true);
            }
        });
        requestTrendingRepos(true);
        return view;
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
    
    private void requestTrendingRepos(boolean refresh){
    	if(currentRequest != null) currentRequest.cancelRequest();
    	TrendingReposRequest request = new TrendingReposRequest(getActivity());
    	Condition condition = request.new Condition();
    	condition.SetSince("daily");
    	condition.SetLanguage(language);
    	condition.SetRefresh(refresh);
    	request.SetHandler(this);
    	request.SetSearchCondition(condition);
    	RequestManager.getInstance(getActivity()).addRequest(request);
    	currentRequest = request;
    }

	@Override
	public Void updateLanguage(String language) {
		this.language = language;
    	mSwipeRefreshLayout.setRefreshing(true);
		requestTrendingRepos(true);
		return null;
	}
}
