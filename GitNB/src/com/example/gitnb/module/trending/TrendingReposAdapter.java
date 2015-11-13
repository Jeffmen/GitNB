package com.example.gitnb.module.trending;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;

import com.example.gitnb.R;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.viewholder.LoadMoreViewHolder;
import com.example.gitnb.module.viewholder.ReposViewHolder;
import com.example.gitnb.module.viewholder.SearchViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

public class TrendingReposAdapter extends RecyclerView.Adapter<ViewHolder>{

	private Context mContext;
    private static final int TYPE_HEADER_VIEW = 2;
    private static final int TYPE_FOOTER_VIEW = 1;
    private static final int TYPE_NOMAL_VIEW = 0;
    private static final int PAGE_COUNT = 30;
    protected final LayoutInflater mInflater;
    private ArrayList<Repository> mRepos;
    private OnItemClickListener mItemClickListener;
    private OnItemClickListener mLoadMoreClickListener;
    private OnItemClickListener mSearchClickListener;
    private boolean isShowLoadMore = true;
    private boolean isLoadingMore = false;
    private String searchText = "";

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public TrendingReposAdapter(Context context) {
    	mContext = context;
    	mInflater = LayoutInflater.from(mContext);
	}
    
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public void SetOnLoadMoreClickListener(final OnItemClickListener mLastItemClickListener) {
        this.mLoadMoreClickListener = mLastItemClickListener;
    }
    
    public void SetOnSearchClickListener(final OnItemClickListener mSearchClickListener) {
        this.mSearchClickListener = mSearchClickListener;
    }
    
    public String getSearchText(){
    	return searchText;
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
    	else if (isShowLoadMore && getItemCount() - 1 == position) { // footer
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
			View v = mInflater.inflate(R.layout.search,viewgroup,false);
			return new SearchView(v);
		}
		else{
			View v = mInflater.inflate(R.layout.repos_list_item,viewgroup,false);
			return new ReposView(v);
		}
	}
	  
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {		
		switch(getItemViewType(position)){
		case TYPE_FOOTER_VIEW:
			LoadMoreView loadMoreViewHolder = (LoadMoreView) vh;
			Uri uri = (new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.loading)).build();
			DraweeController  draweeController= Fresco.newDraweeControllerBuilder()
					.setAutoPlayAnimations(isLoadingMore)
	                .setUri(uri)
	                .build();
			loadMoreViewHolder.loading_gif.setController(draweeController);
			loadMoreViewHolder.loading_txt.setText("load more...");
			break;
		case TYPE_NOMAL_VIEW:
			ReposView viewHolder = (ReposView) vh;
			Repository item = getItem(position);
			if(item != null){
				viewHolder.repos_name.setText(item.getName());
				viewHolder.repos_star.setText("Star:"+item.getStargazers_count());
				viewHolder.repos_fork.setText(item.isFork()?"fork":"owner");
				viewHolder.repos_language.setText(item.getLanguage());
				viewHolder.repos_homepage.setText(item.getHomepage());
				viewHolder.repos_discription.setText(item.getDescription());
			}
			viewHolder.user_avatar.setVisibility(View.VISIBLE);
			if(item.getOwner() != null){
			    viewHolder.user_avatar.setImageURI(Uri.parse(item.getOwner().getAvatar_url()));
			}
			viewHolder.repos_rank.setText(String.valueOf(position)+".");
			break;
		case TYPE_HEADER_VIEW:
			SearchView searchHolder = (SearchView) vh;
			if(searchText != null && !searchText.isEmpty()){
				searchHolder.search_text.setText(searchText.toCharArray(), 0, searchText.length());
				searchHolder.clear_button.setVisibility(View.VISIBLE);
			}
			else{
				searchHolder.clear_button.setVisibility(View.INVISIBLE);
			}
			//searchHolder.search_text.setEnabled(!isSearching);
			break;
		}
	}
	
	
	private class ReposView extends ReposViewHolder implements View.OnClickListener{
		
		public ReposView(View view) {
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
	
	private class SearchView extends SearchViewHolder{
		
		public SearchView(View view) {
			super(view);
			search_icon.setOnClickListener( new View.OnClickListener(){
	            public void onClick(View v) {
	    	        if (mSearchClickListener != null) {
	    	        	mSearchClickListener.onItemClick(v, getLayoutPosition());
	    	        }
	            }           
	        });      
			clear_button.setOnClickListener( new View.OnClickListener(){
	            public void onClick(View v) {
	            	search_text.setText("");
	            	searchText = "";
	            	clear_button.setVisibility(View.GONE);
	    	        if (mSearchClickListener != null) {
	    	        	mSearchClickListener.onItemClick(v, getLayoutPosition());
	    	        }
	            }           
	        });
			search_text.setOnKeyListener(new OnKeyListener(){

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_ENTER){
		    	        if (mSearchClickListener != null) {
		    	        	mSearchClickListener.onItemClick(v, getLayoutPosition());
		    	        }
		    	        return true;
					}
					return false;
				}
				
			});
			search_text.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable arg0) {
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count){		
					if(s.length() > 0){
						clear_button.setVisibility(View.VISIBLE);
						searchText = s.toString();
					}
				}
				
			});
		}
	}
}
