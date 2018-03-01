package com.i5i58.live.home.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.common.view.xListView.XListView;
import com.i5i58.live.home.adapters.CommonAdapter;
import com.i5i58.live.home.dialogFragment.PersonalDialogFragment;
import com.i5i58.live.home.dialogFragment.SingleChatInfoDialogFragment;
import com.i5i58.live.home.helper.LiveRoomCache;
import com.i5i58.live.model.api.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贡献榜Activity
 */
@ContentView(R.layout.act_mac_list)
public class ContributeActivity extends BaseActivity implements XListView.IXListViewListener {

    private CommonAdapter commonAdapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();
    private Handler mHandler = new Handler();

    private TextView tv_1, tv_2, tv_3, tv_4;

    @ViewInject(R.id.mac_listview)
    private XListView mac_listview;

    @ViewInject(R.id.table_top_tv_title)
    private TextView tv_title;

    @ViewInject(R.id.im_help)
    private ImageView im_help;

    @ViewInject(R.id.rl_help)
    private RelativeLayout rl_help;

    @Event(R.id.im_help)
    private void helpClick(View v){
        rl_help.setVisibility(View.VISIBLE);
        rl_help.getBackground().setAlpha(200);
    }

    @Event(R.id.tv_known)
    private void knowClick(View v){
        rl_help.setVisibility(View.GONE);
    }

    @Event(R.id.table_top_fl_back)
    private void backClick(View v){
        closeAct(this);
    }

    /**
     * 举报
     * @param reason
     */
    private void report(String taAccId, String reason){
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.REPORT)
                .addParam("reportedUser", taAccId)
                .addParam("reason", reason)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())){
                                if (js.get("code").toString().equals("success")) {
                                    TSBSuccess("举报成功！");
                                }else{
                                    LogUtil.e("举报出错");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void reportPopWindow(final String taAccId) {
        View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.pop_report, null);

        TextView report_tv_1 = (TextView) popView.findViewById(R.id.report_tv_1);
        TextView report_tv_2 = (TextView) popView.findViewById(R.id.report_tv_2);
        TextView report_tv_3 = (TextView) popView.findViewById(R.id.report_tv_3);
        TextView report_tv_4 = (TextView) popView.findViewById(R.id.report_tv_4);
        TextView report_tv_5 = (TextView) popView.findViewById(R.id.report_tv_5);
        TextView report_tv_6 = (TextView) popView.findViewById(R.id.report_tv_6);
        TextView report_tv_cancel = (TextView) popView.findViewById(R.id.report_tv_cancel);
        View close = popView.findViewById(R.id.pop_close);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);//允许点击空白
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.report_tv_1:
                        report(taAccId, "政治敏感");
                        break;
                    case R.id.report_tv_2:
                        report(taAccId, "色情低俗");
                        break;
                    case R.id.report_tv_3:
                        report(taAccId, "广告骚扰");
                        break;
                    case R.id.report_tv_4:
                        report(taAccId, "人身攻击");
                        break;
                    case R.id.report_tv_5:
                        report(taAccId, "声音违规");
                        break;
                    case R.id.report_tv_6:
                        report(taAccId, "其他");
                        break;
                    case R.id.report_tv_cancel:
                    case R.id.pop_close:
                        break;
                    default:
                        break;
                }
                popWindow.dismiss();
            }
        };
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        report_tv_1.setOnClickListener(listener);
        report_tv_2.setOnClickListener(listener);
        report_tv_3.setOnClickListener(listener);
        report_tv_4.setOnClickListener(listener);
        report_tv_5.setOnClickListener(listener);
        report_tv_6.setOnClickListener(listener);
        report_tv_cancel.setOnClickListener(listener);
        close.setOnClickListener(listener);

        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void initView() {
        tv_1 = find(R.id.tv_top_1);
        tv_2 = find(R.id.tv_top_2);
        tv_3 = find(R.id.tv_top_3);
        tv_4 = find(R.id.tv_top_4);

        tv_title.setText("贡献榜");
        tv_1.setText("一周贡献榜");
        rl_help.getBackground().setAlpha(80);

        im_help.setVisibility(View.VISIBLE);
        tv_2.setVisibility(View.GONE);
        tv_3.setVisibility(View.GONE);
        tv_4.setVisibility(View.GONE);

        commonAdapter = new CommonAdapter(ContributeActivity.this, list, 0);
        mac_listview.setAdapter(commonAdapter);
        mac_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mac_listview.setXListViewListener(this);
        mac_listview.setPullLoadEnable(false);// 可以加载更多
        mac_listview.doAutoRefresh();
        mac_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonalDialogFragment fragment = new PersonalDialogFragment(list.get(position - 1).get("accid").toString(), new PersonalDialogFragment.PersonalCallback() {
                    @Override
                    public void report(String taAccId) {
                        reportPopWindow(taAccId);
                    }

                    @Override
                    public void attention(boolean isAttention) {

                    }

                    @Override
                    public void singleChat(String name, String iconUrl, String accId) {
                        SingleChatInfoDialogFragment fragment = new SingleChatInfoDialogFragment(name, iconUrl, accId);
                        fragment.show(getSupportFragmentManager(),SingleChatInfoDialogFragment.class.getSimpleName());
                    }

                    @Override
                    public void at(String accId, String atTarget) {

                    }
                });
                fragment.show(getSupportFragmentManager(),PersonalDialogFragment.class.getSimpleName());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        initView();
    }

    private void httpContri() {
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.WEEKOFFER)
                .addParam("cId", LiveRoomCache.getChannelId())
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                list.clear();
                                JSONArray array = js.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject data = array.getJSONObject(i);
                                    map = new HashMap<>();
                                    map.put("num", i + 1);
                                    if (!data.isNull("accId")) {
                                        String accId = getString(data.get("accId"));
                                        map.put("accid", accId);
                                    }
                                    if (!data.isNull("offer")) {
                                        String offer = getString(data.get("offer"));
                                        map.put("offer", offer);
                                    }
                                    if (!data.isNull("name")) {
                                        String name = getString(data.get("name"));
                                        map.put("name", name);
                                    }
                                    if (!data.isNull("richScore")) {
                                        String richScore = getString(data.get("richScore"));
                                        map.put("richScore", richScore);
                                    }
                                    if (!data.isNull("vip")) {
                                        String vip = getString(data.get("vip"));
                                        map.put("vip", vip);
                                    }
                                    if (!data.isNull("guardLevel")) {
                                        String guardLevel = getString(data.get("guardLevel"));
                                        map.put("guardLevel", guardLevel);
                                    }
                                    if (!data.isNull("faceSmallUrl")) {
                                        String faceSmallUrl = getString(data.get("faceSmallUrl"));
                                        map.put("faceSmallUrl", faceSmallUrl);
                                    }
                                    list.add(map);
                                }
                                commonAdapter.notifyDataSetChanged();
                                mac_listview.stopRefresh();
                            } else {
                                TSBError(js.getString("msg"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String getString(Object obj) {
        return obj.toString();
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                httpContri();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {

    }

}
