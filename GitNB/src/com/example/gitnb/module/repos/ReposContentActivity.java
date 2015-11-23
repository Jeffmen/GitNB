package com.example.gitnb.module.repos;

import java.util.ArrayList;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.ApiRetrofit;
import com.example.gitnb.api.retrofit.RepoClient;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.Content;
import com.example.gitnb.utils.MessageUtils;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class ReposContentActivity extends BaseActivity {
	private TextView contentView;
    private Content content;
	
    protected void setTitle(TextView view){
        if(content != null || content.name.isEmpty()){
        	view.setText(content.name);
        }else{
        	view.setText("NULL");
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        content = (Content)intent.getParcelableExtra(RepositoryDetailActivity.CONTENT);
        setContentView(R.layout.activity_repo_content);
        contentView = (TextView) findViewById(R.id.content);
        //contentView.getSettings().setJavaScriptEnabled(true);
        //contentView.loadUrl(content.html_url);
        requestContents(content.url.replace(ApiRetrofit.API_URL, ""));
    }
    
    private void requestContents(final String url){
    	RepoClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
		        contentView.setText(((Content) ts).content);
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(ReposContentActivity.this, Message);
			}
			
    	}).get(url);
    }
}
