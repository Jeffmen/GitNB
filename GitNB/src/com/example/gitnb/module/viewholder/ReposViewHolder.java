package com.example.gitnb.module.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gitnb.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class ReposViewHolder extends RecyclerView.ViewHolder{
	TextView repos_name;
	TextView repos_star;
	TextView repos_fork;
	TextView repos_language;
	TextView repos_homepage;
	TextView repos_discription;
	TextView repos_rank;
	SimpleDraweeView user_avatar;
	
	public ReposViewHolder(View view) {
		super(view);
		repos_rank = (TextView) view.findViewById(R.id.repos_rank);
		repos_name = (TextView) view.findViewById(R.id.repos_name);
		repos_star = (TextView) view.findViewById(R.id.repos_star);
		repos_fork = (TextView) view.findViewById(R.id.repos_fork);
		repos_language = (TextView) view.findViewById(R.id.repos_language);
		repos_homepage = (TextView) view.findViewById(R.id.repos_homepage);
		repos_discription = (TextView) view.findViewById(R.id.repos_description);
		user_avatar = (SimpleDraweeView) view.findViewById(R.id.user_avatar);
	}
}
