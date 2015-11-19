package com.example.gitnb.api.retrofit;

import android.content.Context;

public class UsersClient extends RetrofitNetworkAbs{

	public UsersClient() {
		super();
	}
	
	@Override
	public UsersClient setNetworkListener(NetworkListener networkListener) {
        return setNetworkListener(networkListener, this);
	}

}
