package com.example.gitnb.module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gitnb.R;
import com.example.gitnb.module.user.LetterTileDrawable;
import com.example.gitnb.utils.Utils;

public class LanguageAdapter extends RecyclerView.Adapter<ViewHolder>{
	private Context mContext;
	private int iconSize;
    private CharSequence[] languageKey;
    protected final LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public LanguageAdapter(Context context) {
    	mContext = context;
    	mInflater = LayoutInflater.from(mContext);
        languageKey = mContext.getResources().getTextArray(R.array.all_language_key);
		iconSize = Utils.dpToPx(context, 100);
	}
    
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

	@Override
	public int getItemCount() {
		return languageKey.length;
	}

	@Override
	public void onBindViewHolder(ViewHolder vh, int position) {	
		LanguageViewHolder viewHolder = (LanguageViewHolder) vh;
		LetterTileDrawable titleIcon = new LetterTileDrawable(mContext.getResources(), iconSize);
		titleIcon.setIsCircular(true);
		titleIcon.setContactDetails(languageKey[position].toString(), languageKey[position].toString());
	    viewHolder.language.setImageDrawable(titleIcon);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewgroup, int viewType) {
		View v = mInflater.inflate(R.layout.language_list_item,viewgroup,false);
		return new LanguageViewHolder(v);
	}

	public class LanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		public ImageView language;
		
		public LanguageViewHolder(View view) {
			super(view);
			language = (ImageView) view.findViewById(R.id.language);
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

