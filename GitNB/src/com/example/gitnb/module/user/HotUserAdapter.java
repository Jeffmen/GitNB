package com.example.gitnb.module.user;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gitnb.R;
import com.example.gitnb.model.User;
import com.facebook.drawee.view.SimpleDraweeView;

public class HotUserAdapter extends RecyclerView.Adapter<HotUserAdapter.HotUserViewHolder>{

	private Context mContext;
    protected final LayoutInflater mInflater;
    private ArrayList<User> mUsers;
    private OnItemClickListener mItemClickListener;

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
    
	public Object getItem(int position) {
		return mUsers == null ? null : mUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
    
    public void update(ArrayList<User> data){
    	mUsers= data;
        notifyDataSetChanged();
    }
    
    public void insertAtBack(ArrayList<User> data){
    	mUsers.addAll(data);
        notifyDataSetChanged();
    }

	@Override
	public int getItemCount() {
		return mUsers == null ? 0 : mUsers.size();
	}

	@Override
	public void onBindViewHolder(HotUserViewHolder viewHolder, int position) {
	    viewHolder.ivAvatar.setImageURI(Uri.parse(mUsers.get(position).getAvatar_url()));
		viewHolder.tvLogin.setText(mUsers.get(position).getLogin());
		viewHolder.tvRank.setText(String.valueOf(position + 1)+".");
	}

	@Override
	public HotUserViewHolder onCreateViewHolder(ViewGroup viewgroup, int position) {
		View v = mInflater.inflate(R.layout.hot_user_list_item,viewgroup,false);
		return new HotUserViewHolder(v);
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
}

