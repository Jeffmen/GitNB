package com.example.gitnb.module.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gitnb.R;
import com.facebook.drawee.view.SimpleDraweeView;

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
