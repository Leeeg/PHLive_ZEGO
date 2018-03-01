package com.i5i58.live.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidadvance.topsnackbar.TSnackbar;
import com.i5i58.live.R;
import com.i5i58.live.common.manager.APPManager;
import com.i5i58.live.common.utils.tSnackBar.TSnackbarUtil;

import org.xutils.x;

/**
 * Created by Lee on 2017/4/27.
 */

public class BaseDialogFragment extends DialogFragment {

    private boolean injected = false;
    private Activity activity;
    private APPManager appManager;

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
}
