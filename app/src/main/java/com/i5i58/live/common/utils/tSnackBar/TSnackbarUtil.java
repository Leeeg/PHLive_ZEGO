package com.i5i58.live.common.utils.tSnackBar;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

/**
 * Created by Lee on 2017/4/11.
 */

public class TSnackbarUtil {

    public static void TSBSuccess(Activity activity, String msg){
        TSnackbar tSnackbar = TSnackbar.make(activity.getWindow().getDecorView(),msg,TSnackbar.LENGTH_SHORT);
        tSnackbar.setActionTextColor(Color.BLACK);
        View tSnackbarView = tSnackbar.getView();
        tSnackbarView.setBackgroundColor(Color.parseColor("#26BFA1"));
        TextView textView = (TextView) tSnackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        tSnackbar.show();
    }

    public static void TSBError(Activity activity, String msg){
        TSnackbar tSnackbar = TSnackbar.make(activity.getWindow().getDecorView(),msg,TSnackbar.LENGTH_SHORT);
        tSnackbar.setActionTextColor(Color.BLACK);
        View snackbarView = tSnackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        tSnackbar.show();
    }

    public static TSnackbar TSBErrorLongtimeShow(Activity activity, String msg){
        TSnackbar tSnackbar = TSnackbar.make(activity.getWindow().getDecorView(),msg,TSnackbar.LENGTH_INDEFINITE);
        tSnackbar.setActionTextColor(Color.BLACK);
        View snackbarView = tSnackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        tSnackbar.show();
        return tSnackbar;
    }
}
