package com.example.gitnb.module.repos;

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
import com.example.gitnb.model.Event;
import com.example.gitnb.model.EventType;
import com.example.gitnb.model.Repository;
import com.example.gitnb.model.User;
import com.example.gitnb.module.viewholder.EventViewHolder;
import com.example.gitnb.module.viewholder.LoadMoreViewHolder;
import com.example.gitnb.module.viewholder.ReposViewHolder;
import com.example.gitnb.module.viewholder.SearchViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

public class ReposEventsAdapter extends RecyclerView.Adapter<ViewHolder>{

	private Context mContext;
    private static final int TYPE_HEADER_VIEW = 2;
    private static final int TYPE_FOOTER_VIEW = 1;
    private static final int TYPE_NOMAL_VIEW = 0;
    private static final int PAGE_COUNT = 30;
    protected final LayoutInflater mInflater;
    private ArrayList<Event> events;
    private OnItemClickListener mItemClickListener;
    private OnItemClickListener mLoadMoreClickListener;
    private OnItemClickListener mSearchClickListener;
    private boolean isShowLoadMore = false;
    private boolean isShowSearch = false;
    private boolean isLoadingMore = false;
    private String searchText = "";

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public ReposEventsAdapter(Context context) {
    	mContext = context;
    	mInflater = LayoutInflater.from(mContext);
	}
    
    public void setShowLoadMore(boolean value){
    	this.isShowLoadMore = value;
    }
    
    public void setShowSearch(boolean value){
    	this.isShowSearch = value;
    }
    
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public void setOnLoadMoreClickListener(final OnItemClickListener mLastItemClickListener) {
        this.mLoadMoreClickListener = mLastItemClickListener;
    }
    
    public void setOnSearchClickListener(final OnItemClickListener mSearchClickListener) {
        this.mSearchClickListener = mSearchClickListener;
    }
    
    public String getSearchText(){
    	return searchText;
    }
    
	public Event getItem(int position) {
		if(isShowSearch && position == 0){
			return null;
		}
		if(isShowLoadMore && position == getItemCount()-1){
			return null;
		}
		return events == null ? null : events.get(position-(isShowSearch?1:0));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
    
    public void update(ArrayList<Event> data){
		isShowLoadMore = true;
    	if(data == null || data.size()<PAGE_COUNT){
    		isShowLoadMore = false;
    	}
    	events= data;
    	reset();
    }
    
    public void insertAtBack(ArrayList<Event> data){
    	if(data == null || data.size()<PAGE_COUNT){
    		isShowLoadMore = false;
    	}
    	else{
    		isShowLoadMore = true;
    	}
        if (data != null && data.size() > 0){
        	events.addAll(data);
        }
    	reset();
    }

    public void reset(){
    	this.isLoadingMore = false;
        notifyDataSetChanged();
    }
    
	@Override
	public int getItemCount() {
		int orther = 0;
		if(isShowLoadMore) orther++;
		if(isShowSearch) orther++;
		if(events == null){
			return 0 + orther;
		}
		else {
			return events.size() + orther;
		}
	}
	
    @Override
    public int getItemViewType(int position) {
    	if(isShowSearch && position == 0){
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
			View v = mInflater.inflate(R.layout.event_list_item,viewgroup,false);
			return new EventView(v);
		}
	}
	//https://api.github.com/users/jeffmen/received_events
	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {		
		switch(getItemViewType(position)){
		case TYPE_FOOTER_VIEW:
			LoadMoreView loadMoreViewHolder = (LoadMoreView) vh;
			Uri uri = (new Uri.Builder()).scheme("res").path(String.valueOf(R.drawable.github_loading)).build();
			DraweeController  draweeController= Fresco.newDraweeControllerBuilder()
					.setAutoPlayAnimations(isLoadingMore)
	                .setUri(uri)
	                .build();
			loadMoreViewHolder.loading_gif.setController(draweeController);
			loadMoreViewHolder.loading_txt.setText("load more...");
			break;
		case TYPE_NOMAL_VIEW:
			EventView viewHolder = (EventView) vh;
			Event item = getItem(position);
			if(item != null){
				viewHolder.type_img.setBackgroundResource(R.drawable.ic_chevron_right_white_18dp);
				viewHolder.created_date.setText(item.created_at);
				viewHolder.event_user.setText(item.actor.getLogin());
				viewHolder.event_type.setText(getTypeString(item.getType()));
				viewHolder.repos_name.setText(item.repo.getName());
				viewHolder.description.setText(item.payload.issue.title);
			}
			viewHolder.user_avatar.setVisibility(View.VISIBLE);
			if(item.actor != null){
			    viewHolder.user_avatar.setImageURI(Uri.parse(item.actor.getAvatar_url()));
			}
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
	
	private String getTypeString(EventType type){
		switch (type) {
		case WatchEvent:
				return "starred";
		case CreateEvent:
				return "starred";
		case CommitCommentEvent:
				return "starred";
		case DownloadEvent:
				return "starred";
		case FollowEvent:
				return "starred";
		case ForkEvent:
				return "starred";
		case GistEvent:
				return "starred";
		case GollumEvent:
				return "starred";
		case IssueCommentEvent:
				return "starred";
		case IssuesEvent:
				return "starred";
		case MemberEvent:
				return "starred";
		case PublicEvent:
		case PullRequestEvent:
		case PullRequestReviewCommentEvent:
		case PushEvent:
		case StatusEvent:
		case TeamAddEvent:
		case DeleteEvent:
		case ReleaseEvent:
				return "starred";
			default:
				return "null";
		}
	}
	
	private class EventView extends EventViewHolder implements View.OnClickListener{
		
		public EventView(View view) {
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
