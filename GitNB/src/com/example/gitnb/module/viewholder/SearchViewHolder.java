package com.example.gitnb.module.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gitnb.R;

public class SearchViewHolder extends RecyclerView.ViewHolder{
	EditText search_text;
	ImageView search_icon;
	ImageView clear_button;
	
	public SearchViewHolder(View view) {
		super(view);
		clear_button = (ImageView) view.findViewById(R.id.clear_button);
		search_icon = (ImageView) view.findViewById(R.id.search_icon);
		search_text = (EditText) view.findViewById(R.id.search_text);
	}
}
