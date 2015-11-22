package com.example.gitnb.module.repos;

import com.example.gitnb.R;
import com.example.gitnb.model.Content;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class ReposContentActivity extends Activity {
    private Content content;
	
    protected void setTitle(TextView view){
        if(content != null && content.name.isEmpty()){
        	view.setText(content.name);
        }else{
        	view.setText("NULL");
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_content);
        Intent intent = getIntent();
        content = intent.getParcelableExtra(RepositoryDetailActivity.CONTENT);
        WebView contentView = (WebView) findViewById(R.id.content);
        //contentView.getSettings().setJavaScriptEnabled(true);
        contentView.loadUrl(content.html_url);
    }

}
