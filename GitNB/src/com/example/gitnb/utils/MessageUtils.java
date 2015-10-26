package com.example.gitnb.utils;

import com.example.gitnb.app.Application;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


/**
 * Created by yw on 2015/5/5.
 */
public class MessageUtils {

    public static void showErrorMessage(Context cxt, String errorString) {
        Activity activity = (Activity) cxt;
        if (activity == null)
            Toast.makeText(Application.getContext(), errorString, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(cxt, errorString, Toast.LENGTH_LONG).show();
    }

    public static void showMiddleToast(Context cxt, String msg) {
        if(cxt == null)
            cxt = Application.getContext();
        Toast toast = Toast.makeText(cxt, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
