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
import com.example.gitnb.model.HotUser;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

public class HotUserAdapter extends RecyclerView.Adapter<ViewHolder>{

	private Context mContext;
    private static final int TYPE_FOOTER_VIEW = 1;
    private static final int TYPE_NOMAL_VIEW = 0;
    protected final LayoutInflater mInflater;
    private ArrayList<HotUser> mUsers;
    private OnItemClickListener mItemClickListener;
    private OnItemClickListener mLoadMoreClickListener;
    private boolean isLoadingMore = false;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public HotUserAdapter(Context context) {
    	mContext = context;
    	mInflater = LayoutInflater.from(mContext);
	}
    
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public void SetOnLoadMoreClickListener(final OnItemClickListener mLastItemClickListener) {
        this.mLoadMoreClickListener = mLastItemClickListener;
    }
    
	public Object getItem(int position) {
		if(position == getItemCount()-1){
			return null;
		}
		return mUsers == null ? null : mUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
    
    public void update(ArrayList<HotUser> data){
    	this.isLoadingMore = false;
    	mUsers= data;
        notifyDataSetChanged();
    }
    
    public void insertAtBack(ArrayList<HotUser> data){
    	this.isLoadingMore = false;
    	mUsers.addAll(data);
        notifyDataSetChanged();
    }

	@Override
	public int getItemCount() {
		return (mUsers == null ? 0 : mUsers.size())+1;
	}
	
    @Override
    public int getItemViewType(int position) {
    	if (getItemCount() - 1 == position) { // footer
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
		else{
			View v = mInflater.inflate(R.layout.hot_user_list_item,viewgroup,false);
			return new HotUserViewHolder(v);
		}
	}
	  
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {		
		switch(getItemViewType(position)){
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
			HotUserViewHolder viewHolder = (HotUserViewHolder) vh;
		    viewHolder.ivAvatar.setImageURI(Uri.parse(mUsers.get(position).getAvatar_url()));
			viewHolder.tvLogin.setText(mUsers.get(position).getLogin());
			viewHolder.tvRank.setText(String.valueOf(position + 1)+".");
			break;
		}
	}
	
	
	public class HotUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		TextView tvLogin;
		TextView tvRank;
		SimpleDraweeView ivAvatar;

		public HotUserViewHolder(View view) {
			super(view);
			ivAvatar = (SimpleDraweeView) view.findViewById(R.id.id_user_avatar);
	        tvLogin = (TextView) view.findViewById(R.id.user_login);
	        tvRank = (TextView) view.findViewById(R.id.user_rank);
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

