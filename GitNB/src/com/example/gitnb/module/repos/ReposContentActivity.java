package com.example.gitnb.module.repos;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.OKHttpClient;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.app.BaseActivity;
import com.example.gitnb.model.Content;
import com.example.gitnb.utils.MessageUtils;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ReposContentActivity extends BaseActivity {
	private static int UPDATE_CONTENT = 100;
    private MaterialAnimatedSwitch swithBt;
	private ScrollView text_scrollview;
	private TextView text_content;
	private WebView web_content;
    private Content content;
    private boolean isShowInWeb = false;
    
    private Handler mHandler = new Handler(){ 
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == UPDATE_CONTENT)
            {
            	updateContent();
            }
        }
    };
    
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
        text_content = (TextView) findViewById(R.id.text_content);
        web_content = (WebView) findViewById(R.id.web_content);
        text_scrollview = (ScrollView) findViewById(R.id.text_scrollview);
        swithBt = (MaterialAnimatedSwitch) findViewById(R.id.switch_bt);  
        swithBt.setVisibility(View.VISIBLE);
        swithBt.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
        
           @Override 
           public void onCheckedChanged(boolean isChecked) {
        	   isShowInWeb = isChecked;
        	   updateContent();
           }
        
        });
        requestContents(content.url);        
    }
    
    private void updateContent(){
    	if(isShowInWeb){
    		text_content.setVisibility(View.GONE);
    		web_content.setVisibility(View.VISIBLE);
            //web_content.getSettings().setJavaScriptEnabled(true);
    		text_content.setText(null);
            web_content.loadUrl(content.html_url);
    	}
    	else{
    		text_content.setVisibility(View.VISIBLE);
    		web_content.setVisibility(View.GONE);
    		web_content.clearView();
    		text_content.setText(new String(Base64.decode(content.content, Base64.DEFAULT)));
    	}
    }
    
    private void requestContents(final String url){
    	OKHttpClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {

			@Override
			public void onOK(Object ts) {
				content = (Content) ts;
				mHandler.sendEmptyMessage(UPDATE_CONTENT);
			}

			@Override
			public void onError(String Message) {
				MessageUtils.showErrorMessage(ReposContentActivity.this, Message);
			}
			
    	}).request(url);
    }
}
