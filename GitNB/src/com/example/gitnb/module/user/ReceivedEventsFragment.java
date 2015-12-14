package com.example.gitnb.module.user;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.api.retrofit.UsersClient;
import com.example.gitnb.model.Event;
import com.example.gitnb.model.User;
import com.example.gitnb.module.MainActivity.UpdateLanguageListener;
import com.example.gitnb.module.repos.EventListAdapter;
import com.example.gitnb.module.repos.HotReposFragment;
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

public class ReceivedEventsFragment extends Fragment implements RetrofitNetworkAbs.NetworkListener<ArrayList<Event>>, UpdateLanguageListener{
	private String TAG = "HotUserFragment";
	public static String USER = "user_key";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private EventListAdapter adapter;
	private boolean isLoadingMore;
	private User user;
	private int page;
	
	public ReceivedEventsFragment(User user){
		this.user = user;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        page = 1;
        adapter = new EventListAdapter(getActivity());
        adapter.setOnItemClickListener(new EventListAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(getActivity(), UserDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(HotReposFragment.REPOS, adapter.getItem(position).repo);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
        adapter.setOnLoadMoreClickListener(new EventListAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
                if(isLoadingMore){
	                Log.d(TAG,"ignore manually update!");
	            } else{
	             	page++;
	                isLoadingMore = true;
	                receivedEvents();
	            }
			}
		});        
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            	receivedEvents();
            }
        });
        receivedEvents();
        return view;
    }
	
	@Override
	public void onOK(ArrayList<Event> list) {   	
		if(page == 1){
        	adapter.update(list);
    	}
    	else{
            isLoadingMore = false;
        	adapter.insertAtBack(list);
    	}
    	mSwipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onError(String Message) {
    	mSwipeRefreshLayout.setRefreshing(false);
		MessageUtils.showErrorMessage(getActivity(), Message);
	}
	
	public void receivedEvents(){
    	mSwipeRefreshLayout.setRefreshing(true);
		UsersClient.getNewInstance().setNetworkListener(this).events(user.getLogin(), page);
	}

	@Override
	public Void updateLanguage(String language) {
		return null;
	}
}
