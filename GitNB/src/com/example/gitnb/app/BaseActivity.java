package com.example.gitnb.app;

import com.example.gitnb.R;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public abstract class BaseActivity  extends AppCompatActivity {
	private Toolbar toolbar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatus();
    }
    
    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);        
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle((TextView) toolbar.findViewById(R.id.title));         
        setSupportActionBar(toolbar);
        //setNavigationOnClickListener must be at the back of setSupportActionBar and the function is valid
        toolbar.setNavigationIcon(getNavigationIcon());
        toolbar.setNavigationOnClickListener(getNavigationOnClickListener());
    }
    
    abstract protected void setTitle(TextView view);
    abstract protected int getNavigationIcon();
    abstract protected View.OnClickListener getNavigationOnClickListener();
    
    private void setStatus(){
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
