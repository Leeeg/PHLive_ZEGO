package com.i5i58.live.reactNative;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.react.BuildConfig;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import org.xutils.common.util.LogUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Administrator on 2016/11/8.
 */

public class MyReactActivity extends ReactActivity {

    public static String mainName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private static View getRootView(Activity context)
    {
        return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
    }

    //构建一个阻塞的单一数据的队列
    public static ArrayBlockingQueue<String> mQueue = new ArrayBlockingQueue<>(1);
    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        Log.e("Code","=========="+mainName);
        return mainName;
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }
    /**
     * A list of packages used by the app. If the app uses additional views
     * or modules besides the default ones, add more packages here.
     */
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new IntentReactPackage()
        );
    }

    /**
     * 打开 带返回的Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("MyReactActivity : onActivityResult ");
//        if (resultCode == RESULT_OK && requestCode == 200) {
//            String result = data.getStringExtra("data");
//            if (result != null && !result.equals("")) {
//                mQueue.add(result);
//            } else {
//                mQueue.add("无数据啦");
//            }
//        } else {
//            mQueue.add("没有回调...");
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}