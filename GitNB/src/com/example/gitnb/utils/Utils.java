package com.example.gitnb.utils;

import android.content.Context;

public class Utils {
	public static int dpToPx(Context context,int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}
