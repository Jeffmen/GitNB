package com.example.gitnb.module.user;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gitnb.R;
import com.example.gitnb.model.Repository;
import com.example.gitnb.model.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

public class UserReposAdapter extends RecyclerView.Adapter<ViewHolder>{

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
    private ArrayList<Repository> mRepos;
    private User userInfo;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public UserReposAdapter(Context context, User userInfo) {
    	mContext = context;
    	this.userInfo = userInfo;
    	mInflater = LayoutInflater.from(mContext);
	}
    
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public void SetOnLoadMoreClickListener(final OnItemClickListener mLastItemClickListener) {
        this.mLoadMoreClickListener = mLastItemClickListener;
    }
    
    public void UpdateUserInfo(User value) {
        this.userInfo = value;
        notifyDataSetChanged();
    }
    
	public Repository getItem(int position) {
		if(position == 0){
			return null;
		}
		if(isShowLoadMore && position == getItemCount()-1){
			return null;
		}
		return mRepos == null ? null : mRepos.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
    
    public void update(ArrayList<Repository> data){
    	if(data == null || data.size()<PAGE_COUNT){
    		isShowLoadMore = false;
    	}   
    	else{
    		isShowLoadMore = true;
    	}
//    	if (data != null && data.size() > 0){
    	mRepos= data;
//    	}
    	reset();
    }
    
    public void insertAtBack(ArrayList<Repository> data){
    	if(data == null || data.size()<PAGE_COUNT){
    		isShowLoadMore = false;
    	}
    	else{
    		isShowLoadMore = true;
    	}
        if (data != null && data.size() > 0){
        	mRepos.addAll(data);
        }
    	reset();
    }

    public void reset(){
    	this.isLoadingMore = false;
        notifyDataSetChanged();
    }
    
	@Override
	public int getItemCount() {
		return (mRepos == null ? 0 : mRepos.size())+(isShowLoadMore ? 2 : 1);
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
			return new LoadMoreViewHolder(v);
		}
		else if(viewType == TYPE_HEADER_VIEW){
			View v = mInflater.inflate(R.layout.user_detail_item,viewgroup,false);
			return new UserDetailViewHolder(v);
		}
		else{
			View v = mInflater.inflate(R.layout.repos_list_item,viewgroup,false);
			return new UserReposViewHolder(v);
		}
	}
	  
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {		
		switch(getItemViewType(position)){
		case TYPE_HEADER_VIEW:
			if(userInfo!=null){
				UserDetailViewHolder userInfoVeiwHolder = (UserDetailViewHolder) vh;
				userInfoVeiwHolder.user_name.setText(userInfo.getName());
				userInfoVeiwHolder.user_company.setText(userInfo.getCompany());
				userInfoVeiwHolder.user_location.setText(userInfo.getLocation());
				userInfoVeiwHolder.user_created_date.setText(userInfo.getCreated_at());
				userInfoVeiwHolder.user_blog.setText(userInfo.getBlog());
				userInfoVeiwHolder.user_avatar.setImageURI(Uri.parse(userInfo.getAvatar_url()));
			}
			break;
		case TYPE_FOOTER_VIEW:
			LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) vh;
			Uri uri = (new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.loading)).build();
			DraweeController  draweeController= Fresco.newDraweeControllerBuilder()
					.setAutoPlayAnimations(isLoadingMore)
	                .setUri(uri)
	                .build();
			loadMoreViewHolder.loading_gif.setController(draweeController);
			loadMoreViewHolder.loading_txt.setText("load more...");
			break;
		case TYPE_NOMAL_VIEW:
			UserReposViewHolder viewHolder = (UserReposViewHolder) vh;
			Repository item = getItem(position);
			if(item != null){
				viewHolder.repos_name.setText(item.getName());
				viewHolder.repos_star.setText("Star:"+item.getStargazers_count());
				viewHolder.repos_fork.setText(item.isFork()?"fork":"owner");
				viewHolder.repos_language.setText(item.getLanguage());
				viewHolder.repos_discription.setText(item.getDescription());
			}
			viewHolder.user_avatar.setVisibility(View.GONE);
			viewHolder.repos_rank.setText(String.valueOf(position)+".");
			break;
		}
	}
	
	public class UserDetailViewHolder extends RecyclerView.ViewHolder{
		TextView user_name;
		TextView user_company;
		TextView user_location;
		TextView user_created_date;
		TextView user_blog;
        SimpleDraweeView user_avatar;
		
		public UserDetailViewHolder(View view) {
			super(view);
			user_name = (TextView) view.findViewById(R.id.user_name);
			user_company = (TextView) view.findViewById(R.id.user_company);
			user_location = (TextView) view.findViewById(R.id.user_location);
			user_created_date = (TextView) view.findViewById(R.id.user_created_date);
			user_blog = (TextView) view.findViewById(R.id.user_blog);
			user_avatar = (SimpleDraweeView)view.findViewById(R.id.user_avatar);
		}
	}
	
	public class UserReposViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		TextView repos_name;
		TextView repos_star;
		TextView repos_fork;
		TextView repos_language;
		TextView repos_homepage;
		TextView repos_discription;
		TextView repos_rank;
		SimpleDraweeView user_avatar;
		
		public UserReposViewHolder(View view) {
			super(view);
			repos_rank = (TextView) view.findViewById(R.id.repos_rank);
			repos_name = (TextView) view.findViewById(R.id.repos_name);
			repos_star = (TextView) view.findViewById(R.id.repos_star);
			repos_fork = (TextView) view.findViewById(R.id.repos_fork);
			repos_language = (TextView) view.findViewById(R.id.repos_language);
			repos_homepage = (TextView) view.findViewById(R.id.repos_homepage);
			repos_discription = (TextView) view.findViewById(R.id.repos_description);
			user_avatar = (SimpleDraweeView) view.findViewById(R.id.user_avatar);
            view.setOnClickListener(this);
		}
	
		@Override
		public void onClick(View v) {
	        if (mItemClickListener != null) {
	            mItemClickListener.onItemClick(v, getLayoutPosition());
	        }
		}
	}
	
	public class LoadMoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		TextView loading_txt;
		SimpleDraweeView loading_gif;
		
		public LoadMoreViewHolder(View view) {
			super(view);
			loading_gif = (SimpleDraweeView) view.findViewById(R.id.loading_gif);
			loading_txt = (TextView) view.findViewById(R.id.loading_txt);
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

