package com.example.gitnb.module;

import com.example.gitnb.R;
import com.example.gitnb.api.retrofit.GitHub;
import com.example.gitnb.api.retrofit.LoginClient;
import com.example.gitnb.api.retrofit.RetrofitNetworkAbs;
import com.example.gitnb.api.retrofit.UsersClient;
import com.example.gitnb.app.BaseSwipeActivity;
import com.example.gitnb.model.Token;
import com.example.gitnb.model.User;
import com.example.gitnb.utils.CurrentUser;
import com.example.gitnb.utils.MessageUtils;
import com.example.gitnb.widget.ProgressWebView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class GitHubAnthorizeActivity extends BaseSwipeActivity {
	private ProgressWebView web_content;
    
    protected void setTitle(TextView view){
        view.setText("Authorize");
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_authorize);
        web_content = (ProgressWebView) findViewById(R.id.web_content);
        ((Switch)findViewById(R.id.switch_bt)).setVisibility(View.GONE);
        String url = GitHub.API_AUTHORIZE_URL;
        url += "?client_id=" + GitHub.CLIENT_ID;
        url += "&state=" + GitHub.STATE;
        url += "&redirect_uri=" + GitHub.REDIRECT_URI;
        url += "&scope=" + GitHub.SCOPE;
        web_content.getSettings().setJavaScriptEnabled(true);
        web_content.loadUrl(url);
        web_content.setUrlLoadingListener(new ProgressWebView.UrlLoadingListener() {
			
			@Override
			public void loading(String url) {
				if(url.contains(GitHub.REDIRECT_URI)){
					Uri uri = Uri.parse(url);
					String code = uri.getQueryParameter(GitHub.CODE_KEY);
					GitHub.getInstance().setCode(code);
					LoginClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener<Token>() {

						@Override
						public void onOK(Token token) {
							GitHub.getInstance().setToken(token.access_token);

							UsersClient.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener<User>() {

								@Override
								public void onOK(User user) {
									CurrentUser.save(GitHubAnthorizeActivity.this, user);
									finish();
								}

								@Override
								public void onError(String Message) {
							        MessageUtils.showErrorMessage(GitHubAnthorizeActivity.this, Message);
									finish();
								}
								
					    	}).me();
						}

						@Override
						public void onError(String Message) {
					        MessageUtils.showErrorMessage(GitHubAnthorizeActivity.this, Message);
							finish();
						}
						
			    	}).requestTokenAsync();
				}
			}
		});
    }

    @Override
    protected void startRefresh(){
    }

    @Override
    protected void endRefresh(){
    }

    @Override
    protected void endError(){
    }
    
}
