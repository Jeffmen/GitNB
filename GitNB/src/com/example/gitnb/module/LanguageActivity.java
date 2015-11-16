package com.example.gitnb.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import com.example.gitnb.R;

public class LanguageActivity  extends Activity {
    public static String LANGUAGE_KEY = "language_key";
    private RecyclerView recyclerView;
    private LanguageAdapter adapter;
    private static int COLUM_NUM = 4;
    private static int ITEM_SPACE = 35;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        adapter = new LanguageAdapter(this);
        adapter.SetOnItemClickListener(new LanguageAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				Intent intent=new Intent();
			    intent.putExtra(LANGUAGE_KEY, adapter.getItem(position));
			    setResult(RESULT_OK, intent);
			    finish();
				//Toast.makeText(LanguageActivity.this, "item:"+position, Toast.LENGTH_SHORT).show();
			}
		});
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(COLUM_NUM, ITEM_SPACE, true));
        recyclerView.setLayoutManager(new MyGridLayoutManager(this,COLUM_NUM));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.updateData(getResources().getTextArray(R.array.all_language_key));
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
	    setResult(RESULT_CANCELED, null);
        finish();
        return true;
    }
    
    public class MyGridLayoutManager extends GridLayoutManager {
        
        public MyGridLayoutManager(Context context, int columNum) {
            super(context, columNum);
        }
        

        @Override
        public void onMeasure(Recycler recycler, State state, int widthSpec,int heightSpec) {
            View view = recycler.getViewForPosition(0);
            if(view != null){
                measureChild(view, widthSpec, heightSpec);
                int measuredWidth = MeasureSpec.getSize(widthSpec);
                int measuredHeight = view.getMeasuredHeight() + ITEM_SPACE;
                setMeasuredDimension(measuredWidth, measuredHeight * getSpanCount() - ITEM_SPACE);
            }
            else{
            	super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        }
    }
    
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//                if (position < spanCount) { // top edge
//                    outRect.top = spacing;
//                }
//                outRect.bottom = spacing; // item bottom
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}