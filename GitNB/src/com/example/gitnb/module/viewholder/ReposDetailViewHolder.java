package com.example.gitnb.module.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gitnb.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class ReposDetailViewHolder extends RecyclerView.ViewHolder{
	TextView repos_name;
	TextView repos_owner;
	TextView repos_created;
	TextView repos_homepage;
	TextView repos_discription;
	SimpleDraweeView user_avatar;
	
	public ReposDetailViewHolder(View view) {
		super(view);
		repos_name = (TextView) view.findViewById(R.id.repos_name);
		repos_owner = (TextView) view.findViewById(R.id.repos_owner);
		repos_created = (TextView) view.findViewById(R.id.repos_created);
		repos_homepage = (TextView) view.findViewById(R.id.repos_homepage);
		repos_discription = (TextView) view.findViewById(R.id.repos_description);
		user_avatar = (SimpleDraweeView) view.findViewById(R.id.user_avatar);
	}
}
