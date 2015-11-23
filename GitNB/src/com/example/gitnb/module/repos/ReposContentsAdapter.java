package com.example.gitnb.module.repos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gitnb.R;
import com.example.gitnb.model.Content;
import com.example.gitnb.model.Repository;
import com.example.gitnb.module.user.HotUserFragment;
import com.example.gitnb.module.user.UserDetailActivity;
import com.example.gitnb.module.viewholder.RepoContentViewHolder;
import com.example.gitnb.module.viewholder.ReposDetailViewHolder;

public class ReposContentsAdapter extends RecyclerView.Adapter<ViewHolder>{

	private Context mContext;
    private static final int TYPE_HEADER_VIEW = 2;
    private static final int TYPE_NOMAL_VIEW = 0;
    private OnItemClickListener mItemClickListener;
    protected final LayoutInflater mInflater;
    private ArrayList<Content> mContents;
    private Repository reposInfo;
    
    
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public ReposContentsAdapter(Context context, Repository info) {
    	mContext = context;
    	reposInfo = info;
    	mInflater = LayoutInflater.from(mContext);
	}
    
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
	public Content getItem(int position) {
		if(position == 0){
			return null;
		}
		return mContents == null ? null : mContents.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
    
    public void update(ArrayList<Content> data){
    	mContents= data;
    	Collections.sort(mContents);
    	reset();
    }
    
    public void insertAtBack(ArrayList<Content> data){
        if (data != null && data.size() > 0){
        	mContents.addAll(data);
        }
    	reset();
    }

    public void reset(){
        notifyDataSetChanged();
    }
    
	@Override
	public int getItemCount() {
		return mContents == null ? 1 : mContents.size()+1;
	}
	
    @Override
    public int getItemViewType(int position) {
    	if(position == 0){
    		return TYPE_HEADER_VIEW;
    	}
    	return TYPE_NOMAL_VIEW;
    }

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewgroup, int viewType) {
		if(viewType == TYPE_HEADER_VIEW){
			View v = mInflater.inflate(R.layout.repos_detail_item,viewgroup,false);
			return new ReposDetailView(v);
		}
		else{
			View v = mInflater.inflate(R.layout.repo_content_list_item,viewgroup,false);
			return new ReposContentView(v);
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
		case TYPE_NOMAL_VIEW:
			ReposContentView viewHolder = (ReposContentView) vh;
			Content content = getItem(position);
			if(content != null){
			    viewHolder.content_name.setText(content.name);
			    if(content.isDir())
			    	viewHolder.content_type.setImageResource(R.drawable.ic_folder_open_white_48dp);
			    if(content.isFile())
			    	viewHolder.content_type.setImageResource(R.drawable.ic_attachment_white_48dp);
			}
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
	
	private class ReposContentView extends RepoContentViewHolder{
		
		public ReposContentView(View view) {
			super(view);		
			content_name.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
			        if (mItemClickListener != null) {
			            mItemClickListener.onItemClick(v, getLayoutPosition());
			        }
				}});
		}
	}
	
}

