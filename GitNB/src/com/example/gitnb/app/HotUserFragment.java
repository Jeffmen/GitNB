package com.example.gitnb.app;

import java.util.ArrayList;
import java.util.List;

import com.example.gitnb.R;
import com.example.gitnb.api.HandlerInterface;
import com.example.gitnb.api.UserRequest;
import com.example.gitnb.api.UserRequest.UserCondition;
import com.example.gitnb.model.User;
import com.example.gitnb.utils.MessageUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HotUserFragment extends Fragment {
	
	private int page = 1;
    private ListView listView;
	private boolean mIsLoading;
    private HotUserAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_data_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new HotUserAdapter(getActivity());
        listView.setAdapter(adapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(
        		android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	if(!mIsLoading){
                	requestHotUser(true);
            	}
            }
        });
        requestHotUser(false);
        return view;
    }
    
    public void onSuccess(ArrayList<User> data){

        mIsLoading = false;
    	mSwipeRefreshLayout.setRefreshing(false);
    }

    public void onSuccess(ArrayList<User> data, int totalPages, int currentPage){

        mIsLoading = false;
    	mSwipeRefreshLayout.setRefreshing(false);
    	//adapter.insertAtBack(data, currentPage != 1);
    }

    public void onFailure(String error){
    	mSwipeRefreshLayout.setRefreshing(false);
        mIsLoading = false;
        MessageUtils.showErrorMessage(getActivity(), error);
    }
    
    private void requestHotUser(boolean refresh){
    	UserRequest request = new UserRequest(getActivity());
    	UserCondition condition = request.new UserCondition();
    	condition.SetLanguage("java");
    	condition.SetLocation("china");
    	condition.SetRefresh(refresh);
    	condition.SetPage(page);
    	request.SetSearchCondition(condition);
    	mIsLoading = true;
    }
    
    private class HotUserAdapter extends BaseAdapter{

    	private Context mContext;
        protected final LayoutInflater mInflater;
        private ArrayList<User> mUsers;
        
        public HotUserAdapter(Context context) {
        	mContext = context;
        	mInflater = LayoutInflater.from(mContext);
		}
        
		public int getCount() {
			return mUsers == null ? 0 : mUsers.size();
		}

		public Object getItem(int position) {
			return mUsers == null ? null : mUsers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
            	view = mInflater.inflate(R.layout.hot_user_list_item,parent,false);
                ViewHolder viewHolder = new ViewHolder();
        		viewHolder.ivAvatar = (ImageView) view.findViewById(R.id.id_user_avatar);
        		viewHolder.tvLogin = (TextView) view.findViewById(R.id.user_login);
        		viewHolder.tvRank = (TextView) view.findViewById(R.id.user_rank);
        		view.setTag(viewHolder);
            } else {
            	view = convertView;
            }
            bindView(view, position);
            return view;
        }

        private void bindView(View view, int position) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();

    	    viewHolder.ivAvatar.setImageDrawable(mUsers.get(position).getAvatar_url());
    		viewHolder.tvLogin.setText(mUsers.get(position).getLogin());
    		viewHolder.tvRank.setText("rank: " + String.valueOf(position + 1));
        }
        
    }
    
	final static class ViewHolder {
		TextView tvLogin;
		TextView tvRank;
		ImageView ivAvatar;
	}
}
