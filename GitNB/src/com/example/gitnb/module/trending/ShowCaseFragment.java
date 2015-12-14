package com.example.gitnb.module.trending;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.api.retrofit.TrendingClient;
import com.example.gitnb.model.ShowCase;
import com.example.gitnb.module.MainActivity.UpdateLanguageListener;
import com.example.gitnb.module.repos.ReposListActivity;
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

public class ShowCaseFragment extends Fragment implements RetrofitNetworkAbs.NetworkListener<ArrayList<ShowCase>>, UpdateLanguageListener{
	private String TAG = "TrendingReposFragment";
	public static String SHOWCASE = "showcase_key";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private ShowCaseAdapter adapter;
    private RecyclerView recyclerView;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        adapter = new ShowCaseAdapter(getActivity());
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        adapter.SetOnItemClickListener(new ShowCaseAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {

				Intent intent = new Intent(getActivity(), ReposListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(SHOWCASE, adapter.getItem(position));
				intent.putExtras(bundle);
				intent.putExtra(ReposListActivity.REPOS_TYPE, ReposListActivity.REPOS_TYPE_SHOWCASE);
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
            	requeseShowCase();
            }
        });
        requeseShowCase();
        return view;
    }
	
	@Override
	public void onOK(ArrayList<ShowCase> list) {  
    	adapter.update(list);
    	mSwipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onError(String Message) {
    	mSwipeRefreshLayout.setRefreshing(false);
		MessageUtils.showErrorMessage(getActivity(), Message);
	}
	
    private void requeseShowCase(){
    	mSwipeRefreshLayout.setRefreshing(true);
    	TrendingClient.getNewInstance().setNetworkListener(this).trendingShowCase();
    }

	@Override
	public Void updateLanguage(String language) {
		return null;
	}
}
