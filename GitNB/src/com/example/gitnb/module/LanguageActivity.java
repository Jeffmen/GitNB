package com.example.gitnb.module;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.gitnb.R;

public class LanguageActivity  extends Activity {

    private RecyclerView recyclerView;
    private LanguageAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_shower);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        adapter = new LanguageAdapter(this);
        adapter.SetOnItemClickListener(new LanguageAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position) {
				Toast.makeText(LanguageActivity.this, "item:"+position, Toast.LENGTH_SHORT).show();
			}
		});
        recyclerView.setAdapter(adapter);
    }

}