package com.i5i58.live.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.androidadvance.topsnackbar.TSnackbar;
import com.i5i58.live.R;
import com.i5i58.live.common.manager.APPManager;
import com.i5i58.live.common.utils.tSnackBar.TSnackbarUtil;

import org.xutils.x;

/**
 * Created by Lee on 2017/4/7.
 */

public class BaseFragment  extends Fragment {

    private boolean injected = false;
    private Activity activity;
    private APPManager appManager;
    private InputMethodManager imm;

    public void TSBSuccess(String msg){
        TSnackbarUtil.TSBSuccess(getActivity(),msg);
    }

    public void TSBError(String msg){
        TSnackbarUtil.TSBError(getActivity(),msg);
    }

    public TSnackbar TSBErrorLongtimeShow(String msg){
        return TSnackbarUtil.TSBErrorLongtimeShow(getActivity(),msg);
    }

    protected void intentAct(Activity activity, Intent intent){
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.act_in_from_right, R.anim.act_out_to_left);
    }

    protected void closeAct(Activity activity){
        appManager.removeActivity(activity);
        activity.overridePendingTransition(R.anim.act_in_from_left, R.anim.act_out_to_right);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        activity = getActivity();
        appManager = APPManager.getInstance();
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    // 关闭键盘
    public void HideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
    }

}
