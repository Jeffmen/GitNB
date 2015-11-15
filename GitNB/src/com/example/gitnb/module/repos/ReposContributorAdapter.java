package com.example.gitnb.module.repos;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gitnb.R;
import com.example.gitnb.model.Repository;
import com.example.gitnb.model.User;
import com.example.gitnb.module.user.HotUserFragment;
import com.example.gitnb.module.user.UserDetailActivity;
import com.example.gitnb.module.viewholder.LoadMoreViewHolder;
import com.example.gitnb.module.viewholder.ReposDetailViewHolder;
import com.example.gitnb.module.viewholder.UserViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

public class ReposContributorAdapter extends RecyclerView.Adapter<ViewHolder>{

	private Context mContext;
    private static final int TYPE_HEADER_VIEW = 2;
    private static final int TYPE_FOOTER_VIEW = 1;
    private static final int TYPE_NOMAL_VIEW = 0;
    private static final int PAGE_COUNT = 30;
    private OnItemClickListener mItemClickListener;
    private OnItemClickListener mLoadMoreClickListener;
    protected final LayoutInflater mInflater;
    private boolean isShowLoadMore = true;
    private boolean isLoadingMore = false;
    private ArrayList<User> mUsers;
    private Repository reposInfo;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public ReposContributorAdapter(Context context, Repository reposInfo) {
    	mContext = context;
    	this.reposInfo = reposInfo;
    	mInflater = LayoutInflater.from(mContext);
	}
    
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public void SetOnLoadMoreClickListener(final OnItemClickListener mLastItemClickListener) {
        this.mLoadMoreClickListener = mLastItemClickListener;
    }
    
    public void UpdateUserInfo(Repository value) {
        this.reposInfo = value;
        notifyDataSetChanged();
    }
    
	public User getItem(int position) {
		if(position == 0){
			return null;
		}
		if(isShowLoadMore && position == getItemCount()-1){
			return null;
		}
		return mUsers == null ? null : mUsers.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
    
    public void update(ArrayList<User> data){
    	if(data == null || data.size()<PAGE_COUNT){
    		isShowLoadMore = false;
    	}   
    	else{
    		isShowLoadMore = true;
    	}
//    	if (data != null && data.size() > 0){
        	mUsers= data;
//    	}
    	reset();
    }
    
    public void insertAtBack(ArrayList<User> data){
    	if(data == null || data.size()<PAGE_COUNT){
    		isShowLoadMore = false;
    	}
    	else{
    		isShowLoadMore = true;
    	}
        if (data != null && data.size() > 0){
        	mUsers.addAll(data);
        }
    	reset();
    }

    public void reset(){
    	this.isLoadingMore = false;
        notifyDataSetChanged();
    }
    
	@Override
	public int getItemCount() {
		return (mUsers == null ? 0 : mUsers.size())+(isShowLoadMore ? 2 : 1);
	}
	
    @Override
    public int getItemViewType(int position) {
    	if(position == 0){
    		return TYPE_HEADER_VIEW;
    	}
    	else if (isShowLoadMore && getItemCount() - 1 == position) {
    		return TYPE_FOOTER_VIEW;
    	}
    	return TYPE_NOMAL_VIEW;
    }

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewgroup, int viewType) {
		if(viewType == TYPE_FOOTER_VIEW){
			View v = mInflater.inflate(R.layout.list_data_load_more,viewgroup,false);
			return new LoadMoreView(v);
		}
		else if(viewType == TYPE_HEADER_VIEW){
			View v = mInflater.inflate(R.layout.repos_detail_item,viewgroup,false);
			return new ReposDetailView(v);
		}
		else{
			View v = mInflater.inflate(R.layout.user_list_item,viewgroup,false);
			return new ReposContributorView(v);
		}
	}
	  
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {		
		switch(getItemViewType(position)){
		case TYPE_HEADER_VIEW:
			ReposDetailView reposDetailViewHolder = (ReposDetailView) vh;
			if(this.reposInfo != null){
				reposDetailViewHolder.repos_name.setText(reposInfo.getName());				
				String date = reposInfo.getCreated_at();
				if(date != null && !date.isEmpty()){
					date = date.substring(0, date.indexOf('T'));
				}
				reposDetailViewHolder.repos_created.setText(date);
				reposDetailViewHolder.repos_homepage.setText(reposInfo.getHomepage());
				reposDetailViewHolder.repos_discription.setText(reposInfo.getDescription());
			}
			if(reposInfo.getOwner() != null){
				reposDetailViewHolder.repos_owner.setText(reposInfo.getOwner().getLogin());
				reposDetailViewHolder.user_avatar.setImageURI(Uri.parse(reposInfo.getOwner().getAvatar_url()));
			}
			break;
		case TYPE_FOOTER_VIEW:
			LoadMoreView loadMoreViewHolder = (LoadMoreView) vh;
			Uri uri = (new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.github_loading)).build();
			DraweeController  draweeController= Fresco.newDraweeControllerBuilder()
					.setAutoPlayAnimations(isLoadingMore)
	                .setUri(uri)
	                .build();
			loadMoreViewHolder.loading_gif.setController(draweeController);
			break;
		case TYPE_NOMAL_VIEW:
			ReposContributorView viewHolder = (ReposContributorView) vh;
			User user = getItem(position);
			if(user != null){
			    viewHolder.ivAvatar.setImageURI(Uri.parse(user.getAvatar_url()));
				viewHolder.tvLogin.setText(user.getLogin());
			}
			viewHolder.tvRank.setText(String.valueOf(position)+".");
			break;
		}
	}
	
	private class ReposDetailView extends ReposDetailViewHolder{
		
		public ReposDetailView(View view) {
			super(view);		
			user_avatar.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, UserDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable(HotUserFragment.USER_KEY, reposInfo.getOwner());
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}});
		}
	}
	
	private class ReposContributorView extends UserViewHolder implements View.OnClickListener{

			public ReposContributorView(View view) {
				super(view);
	            view.setOnClickListener(this);
			}
		
			@Override
			public void onClick(View v) {
		        if (mItemClickListener != null) {
		            mItemClickListener.onItemClick(v, getLayoutPosition());
		        }
			}
		}
	
	private class LoadMoreView extends LoadMoreViewHolder implements View.OnClickListener{
		
		public LoadMoreView(View view) {
			super(view);
            view.setOnClickListener(this);
		}
	
		@Override
		public void onClick(View v) {
			DraweeController draweeController = loading_gif.getController();
			Animatable animatable = draweeController.getAnimatable();
			animatable.start();
			isLoadingMore = true;
	        if (mLoadMoreClickListener != null) {
	        	mLoadMoreClickListener.onItemClick(v, getLayoutPosition());
	        }
		}
	}
	
}

