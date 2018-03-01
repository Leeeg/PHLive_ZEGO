package com.i5i58.live.home.fragments;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.i5i58.live.R;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.enums.ResultCode;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.httpUtil.HttpUtils;
import com.i5i58.live.common.utils.httpUtil.Net;
import com.i5i58.live.common.view.fragment.LazyFragment;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 麦序Fragments
 */
public class MicFragments extends LazyFragment implements XListView.IXListViewListener {

    private int tabIndex;
    private boolean broadCast1 = false;
    private boolean broadCast2 = false;
    public static final String INTENT_INT_INDEX = "index";
    private LinearLayout ll_title;
    private CommonAdapter commonAdapter;
    private XListView mac_listview;
    private TextView tv_top_3, tv_top_1;
    private int pageNum = 0;
    private int pageSize = 20;
    List<Map<String, Object>> list = new ArrayList<>();
    private Boolean isAtt;
    Map<String, Object> map = new HashMap<>();
    private int itemIndex;
    private int count;
    private Handler mHandler;
    private boolean isManager = false;

    public static MicFragments newInstance(int tabIndex, boolean isLazyLoad) {
        Bundle args = new Bundle();
        args.putInt(INTENT_INT_INDEX, tabIndex);
        args.putBoolean(LazyFragment.INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        MicFragments fragment = new MicFragments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.act_mac_list);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX) + 1;
        mHandler = new Handler();
        getData();
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fragmentHandler.sendEmptyMessageDelayed(1, 100);
            }
        }).start();
    }

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        fragmentHandler.removeMessages(1);
    }

    private Handler fragmentHandler = new Handler() {
        public void handleMessage(Message msg) {
            IntentFilter filter;
            switch (tabIndex) {
                case 1:
                    initViewMic();
                    if (list.size() == 0) {
                        httpMic();
                    }
                    broadCast1 = true;
                    filter = new IntentFilter(PersonalDialogFragment.ACTION1);
                    getActivity().registerReceiver(broadcastReceiver1, filter);
                    break;
                case 2:
                    initViewTiger();
                    if (list.size() == 0) {
                        httpTiger();
                    }
                    broadCast2 = true;
                    filter = new IntentFilter(PersonalDialogFragment.ACTION2);
                    getActivity().registerReceiver(broadcastReceiver2, filter);
                    break;
                case 3:
                    initViewPlayer();
                    if (list.size() == 0) {
                        httpPlayer();
                    }
                    break;
                default:
                    break;
            }
        }
    };


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
        View popView = View.inflate(getActivity(), R.layout.pop_report, null);

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

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
        getActivity().getWindow().setAttributes(lp);

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
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
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


    /**
     * 麦序=====================================================================================================
     */
    private void initViewMic() {
        tv_top_1 = find(R.id.tv_top_1);
        tv_top_1.setText("麦序");
        tv_top_3 = find(R.id.tv_top_3);
        tv_top_3.setText(String.valueOf(list.size()));
        ll_title = find(R.id.ll_title);
        ll_title.setVisibility(View.GONE);
        mac_listview = find(R.id.mac_listview);
        commonAdapter = new CommonAdapter(getActivity(), list, 1);
        mac_listview.setAdapter(commonAdapter);
        mac_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mac_listview.setXListViewListener(this);
        mac_listview.setPullLoadEnable(false);// 可以加载更多
        mac_listview.setPullRefreshEnable(true);// 可以刷新
        mac_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                final ImageView iv_attention = (ImageView) view.findViewById(R.id.iv_attention);
                itemIndex = position;
                PersonalDialogFragment fragment = new PersonalDialogFragment(list.get(position - 1).get("accid").toString(), new PersonalDialogFragment.PersonalCallback() {
                    @Override
                    public void report(String taAccId) {
                        reportPopWindow(taAccId);
                    }

                    @Override
                    public void attention(boolean isAttention) {
                        if (isAttention){
                            iv_attention.setImageResource(R.drawable.bt_attention_cancel);
                        }else {
                            iv_attention.setImageResource(R.drawable.bt_attention);
                        }
                    }

                    @Override
                    public void singleChat(String name, String iconUrl, String accId) {

                        SingleChatInfoDialogFragment fragment = new SingleChatInfoDialogFragment(name, iconUrl, accId);
                        fragment.show(getChildFragmentManager(),SingleChatInfoDialogFragment.class.getSimpleName());
                    }

                    @Override
                    public void at(String accId, String atTarget) {

                    }
                });
                fragment.show(getFragmentManager(),PersonalDialogFragment.class.getSimpleName());

                iv_attention.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        String acc = list.get(position - 1).get("accid").toString();
                        if (acc.equals(UserPreferences.getAccId())) {
                            TSBError("无法关注自己！");
                            return;
                        }
                        if (acc.equals(LiveRoomCache.getChannel().getOwnerId())){
                            isManager = true;
                        }
                        if ((boolean)list.get(position - 1).get("isAtt")) {
                            iv_attention.setImageResource(R.drawable.bt_attention);
                            list.get(position - 1).put("isAtt", false);
                            attentionClick(true, acc, isManager);
                        } else {
                            iv_attention.setImageResource(R.drawable.bt_attention_cancel);
                            list.get(position - 1).put("isAtt", true);
                            attentionClick(false, acc, isManager);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取麦序
     */
    private void httpMic() {
        mac_listview.setEnabled(false);
        mac_listview.setPullLoadEnable(false);// 可以加载更多
        new Thread() {
            public void run() {
                super.run();
                Net net = new Net();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cId", LiveRoomCache.getChannelId());
                map.put("pageNum", pageNum);
                map.put("pageSize", pageSize);
                net.setMap(map);
                net.setMethod("GET");
                net.setUrl(API.REST_URL + API.MAC);

                try {
                    JSONObject js = new JSONObject(HttpUtils.doGet(net).toString());
                    Log.d("mic", js.toString());
                    JSONObject data = js.getJSONObject("data");
                    JSONArray content = data.getJSONArray("content");
                    count = Integer.valueOf(data.getString("count"));
                    if (content.length() >= 1) {
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject player = content.getJSONObject(i);
                            String cId = getString(player.get("cId"));
                            String accId = getString(player.get("accId"));
                            String sitTime = getString(player.get("sitTime"));
                            map = new HashMap<>();
                            if (player.has("faceSmallUrl")) {
                                String faceSmallUrl = getString(player.get("faceSmallUrl"));
                                map.put("faceSmallUrl", faceSmallUrl);
                            }
                            if (player.has("name")) {
                                String name = getString(player.get("name"));
                                map.put("name", name);
                            }
                            if (player.has("vip")) {
                                String vip = getString(player.get("vip"));
                                map.put("vip", vip);
                            }
                            if (player.has("richScore")) {
                                String richScore = getString(player.get("richScore"));
                                map.put("richScore", richScore);
                            }
                            if (player.has("guardLevel")) {
                                String guardLevel = getString(player.get("guardLevel"));
                                map.put("guardLevel", guardLevel);
                            }
                            map.put("cId", cId);
                            map.put("attention", 1);
                            map.put("accid", accId);
                            map.put("sitTime", sitTime);
                            isAtt = getAttention(accId);
                            map.put("isAtt", isAtt);
                            list.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 判断是否已关注
     * @param accId
     * @return
     */
    private boolean getAttention(String accId) {
        Net net = new Net();
        Map<String, Object> map = new HashMap<>();
        map.put("target", accId);
        net.setMap(map);
        net.setAccid(UserPreferences.getAccId());
        net.setToken(UserPreferences.getToken());
        net.setUrl(API.REST_URL + API.GETFOLLOWSTATUS);
        try {
            JSONObject js = new JSONObject(HttpUtils.doPost(net).toString());
            Log.e("FOLLOW", js.toString());
            if (js.getString("code").equals("success")) {
                if (js.getString("data").equals("1") || js.getString("data").equals("3")) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void onLoad() {
        mac_listview.stopRefresh();
        if (count > 20 && count > list.size()) {
            Log.i("dataSize",list.size()+"");
            Log.i("dataSize",count+"");
            mac_listview.stopLoadMore(false);
            mac_listview.setPullLoadEnable(true);
        } else {
            mac_listview.stopLoadMore(true);
            mac_listview.setPullLoadEnable(false);
        }
        mac_listview.setEnabled(true);
    }

    private Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_top_3.setText(count + "");
                    commonAdapter.notifyDataSetChanged();
                    onLoad();
                    break;
                case 4:
                    tv_top_3.setText(count + "");
                    commonAdapter.notifyDataSetChanged();
                    onLoad();
                    break;
                default:
                    break;
            }
        }
    };

    private String getString(Object obj) {
        return obj.toString();
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                pageNum = 0;
                switch (tabIndex) {
                    case 1:
                        httpMic();
                        break;
                    case 2:
                        httpTiger();
                        break;
                    case 3:
                        httpPlayer();
                        break;
                    default:
                        break;
                }
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNum++;
                switch (tabIndex) {
                    case 1:
                        httpMic();
                        break;
                    case 2:
                        httpTiger();
                        break;
                    case 3:
                        httpPlayer();
                        break;
                    default:
                        break;
                }
            }
        }, 1000);
    }

    public void updateItem(boolean Att) {
        View item = mac_listview.getChildAt(itemIndex);
        ImageView iv_attention = (ImageView) item.findViewById(R.id.iv_attention);
        if (Att) {
            iv_attention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_attention_cancel));
        } else {
            iv_attention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bt_attention));
        }
        isAtt = !isAtt;
    }

    BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAttention = intent.getExtras().getBoolean("data");
            updateItem(isAttention);
        }
    };

    BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAttention = intent.getExtras().getBoolean("data");
            updateItem(isAttention);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadCast1) {
            getActivity().unregisterReceiver(broadcastReceiver1);
        }
        if (broadCast2) {
            getActivity().unregisterReceiver(broadcastReceiver2);
        }
    }

    /**
     * 老虎席==================================================================================================
     */
    private void initViewTiger() {
        tv_top_1 = find(R.id.tv_top_1);
        tv_top_1.setText("老虎席");
        tv_top_3 = find(R.id.tv_top_3);
        tv_top_3.setText(String.valueOf(list.size()));
        ll_title = find(R.id.ll_title);
        ll_title.setVisibility(View.GONE);
        mac_listview = find(R.id.mac_listview);
        commonAdapter = new CommonAdapter(getActivity(), list, 1);
        mac_listview.setAdapter(commonAdapter);
        mac_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mac_listview.setXListViewListener(this);
        mac_listview.setPullLoadEnable(false);// 可以加载更多
        mac_listview.setPullRefreshEnable(true);// 可以刷新
        mac_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final ImageView iv_attention = (ImageView) view.findViewById(R.id.iv_attention);
                itemIndex = position;
                PersonalDialogFragment fragment = new PersonalDialogFragment(list.get(position - 1).get("accid").toString(), new PersonalDialogFragment.PersonalCallback() {
                    @Override
                    public void report(String taAccId) {
                        reportPopWindow(taAccId);
                    }

                    @Override
                    public void attention(boolean isAttention) {
                        if (isAttention){
                            iv_attention.setImageResource(R.drawable.bt_attention_cancel);
                        }else {
                            iv_attention.setImageResource(R.drawable.bt_attention);
                        }
                    }

                    @Override
                    public void singleChat(String name, String iconUrl, String accId) {

                        SingleChatInfoDialogFragment fragment = new SingleChatInfoDialogFragment(name, iconUrl, accId);
                        fragment.show(getChildFragmentManager(),SingleChatInfoDialogFragment.class.getSimpleName());
                    }

                    @Override
                    public void at(String accId, String atTarget) {

                    }
                });
                fragment.show(getFragmentManager(),PersonalDialogFragment.class.getSimpleName());

                iv_attention.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        String acc = list.get(position - 1).get("accid").toString();
                        if (acc.equals(UserPreferences.getAccId())) {
                            TSBError("无法关注自己！");
                            return;
                        }
                        if (acc.equals(LiveRoomCache.getChannel().getOwnerId())){
                            isManager = true;
                        }
                        if ((boolean)list.get(position - 1).get("isAtt")) {
                            iv_attention.setImageResource(R.drawable.bt_attention);
                            list.get(position - 1).put("isAtt", false);
                            attentionClick(true, acc, isManager);
                        } else {
                            iv_attention.setImageResource(R.drawable.bt_attention_cancel);
                            list.get(position - 1).put("isAtt", true);
                            attentionClick(false, acc, isManager);
                        }
                    }
                });
            }
        });
    }

    private void httpTiger() {
        mac_listview.setEnabled(false);
        mac_listview.setPullLoadEnable(false);// 可以加载更多
        new Thread() {
            public void run() {
                super.run();
                Net net = new Net();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cId", LiveRoomCache.getChannelId());
                map.put("pageNum", pageNum);
                map.put("pageSize", pageSize);
                net.setMap(map);
                net.setAccid(UserPreferences.getAccId());
                net.setToken(UserPreferences.getToken());
                net.setUrl(API.REST_URL + API.TIGERLIST);

                try {
                    JSONObject js = new JSONObject(HttpUtils.doPost(net).toString());
                    Log.d("tigerSofa", js.toString());
                    if (js.getString("code").equals("success")) {
                        JSONObject data = js.getJSONObject("data");
                        JSONArray content = data.getJSONArray("content");
                        count = Integer.valueOf(data.getString("count"));
                        if (content.length() >= 1) {
                            for (int i = 0; i < content.length(); i++) {
                                JSONObject player = content.getJSONObject(i);
                                map = new HashMap<>();
                                map.put("attention", "1");
                                String cId = getString(player.get("cId"));
                                map.put("cId", cId);
                                String accId = getString(player.get("accId"));
                                isAtt = getAttention(accId);
                                map.put("isAtt", isAtt);
                                map.put("accid", accId);
                                if (player.has("majia")) {
                                    String majia = getString(player.get("majia"));
                                    map.put("majia", majia);
                                }
                                if (player.has("guardLevel")) {
                                    String guardLevel = getString(player.get("guardLevel"));
                                    map.put("guardLevel", guardLevel);
                                }
                                Net netE = new Net();
                                Map<String, Object> mapE = new HashMap<String, Object>();
                                mapE.put("accIds", accId);
                                netE.setMap(mapE);
                                netE.setUrl(API.REST_URL + API.GETMAJIAACCOUNTS);
                                JSONObject jsE = new JSONObject(HttpUtils.doGet(netE).toString());
                                if (jsE.getString("code").equals("success")) {
                                    JSONArray dataE = jsE.getJSONArray("data");
                                    for (int j = 0; j < dataE.length(); j++) {
                                        JSONObject account = dataE.getJSONObject(j);
                                        if (account.has("faceSmallUrl")) {
                                            String faceSmallUrl = getString(account.get("faceSmallUrl"));
                                            map.put("faceSmallUrl", faceSmallUrl);
                                        }
                                        if (account.has("vip")) {
                                            String vip = getString(account.get("vip"));
                                            if (Integer.valueOf(vip) > new Date().getTime()) {
                                                map.put("vip", vip);
                                            }
                                        }
                                        if (account.has("richScore")) {
                                            String richScore = getString(account.get("richScore"));
                                            map.put("richScore", richScore);
                                        }
                                        if (account.has("nickName")) {
                                            String nickName = getString(account.get("nickName"));
                                            map.put("name", nickName);
                                        }
                                    }
                                }
                                list.add(map);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = 4;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 玩家列表
     */
    private void initViewPlayer() {
        tv_top_1 = find(R.id.tv_top_1);
        tv_top_1.setText("观众");
        tv_top_3 = find(R.id.tv_top_3);
        tv_top_3.setText(String.valueOf(list.size()));
        ll_title = find(R.id.ll_title);
        ll_title.setVisibility(View.GONE);
        mac_listview = find(R.id.mac_listview);
        commonAdapter = new CommonAdapter(getActivity(), list, 1);
        mac_listview.setAdapter(commonAdapter);
        mac_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mac_listview.setXListViewListener(this);
        mac_listview.setPullLoadEnable(false);// 可以加载更多
        mac_listview.setPullRefreshEnable(true);// 可以刷新
        mac_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemIndex = position;
                PersonalDialogFragment fragment = new PersonalDialogFragment(list.get(position - 1).get("accid").toString(), new PersonalDialogFragment.PersonalCallback() {
                    @Override
                    public void report(String taAccId) {
                        reportPopWindow(taAccId);
                    }

                    @Override
                    public void attention(boolean isAttention) {
                        LogUtil.d("isAttention  >>>  " + isAttention);
                    }

                    @Override
                    public void singleChat(String name, String iconUrl, String accId) {

                        SingleChatInfoDialogFragment fragment = new SingleChatInfoDialogFragment(name, iconUrl, accId);
                        fragment.show(getChildFragmentManager(),SingleChatInfoDialogFragment.class.getSimpleName());
                    }

                    @Override
                    public void at(String accId, String atTarget) {

                    }
                });
                fragment.show(getFragmentManager(),PersonalDialogFragment.class.getSimpleName());

            }
        });
    }

    private void httpPlayer() {
        mac_listview.setEnabled(false);
        mac_listview.setPullLoadEnable(false);// 可以加载更多
        new Http(HttpEnum.GET).addRootUrl(API.REST_URL)
                .addRouteUrl(API.PLAYER)
                .addParam("cId", LiveRoomCache.getChannelId())
                .addParam("pageNum", pageNum+"")
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            Log.d("player", js.toString());
                            JSONObject data = js.getJSONObject("data");
                            count = Integer.valueOf(data.getString("count"));
                            JSONArray array = data.getJSONArray("viewer");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject user = array.getJSONObject(i);
                                String accId = getString(user.get("accId"));
                                String name = getString(user.get("name"));
                                map = new HashMap<>();
                                map.put("time", 0);
                                map.put("attention", 0);
                                map.put("accid", accId);
                                map.put("name", name);
                                if (user.has("richScore")) {
                                    String richScore = getString(user.get("richScore"));
                                    map.put("richScore", richScore);
                                }
                                if (user.has("faceSmallUrl")) {
                                    String faceSmallUrl = getString(user.get("faceSmallUrl"));
                                    map.put("faceSmallUrl", faceSmallUrl);
                                }
                                if (user.has("vip")) {
                                    String vip = getString(user.get("vip"));
                                    map.put("vip", vip);
                                }
                                if (user.has("guardLevel")) {
                                    String guardLevel = getString(user.get("guardLevel"));
                                    map.put("guard", guardLevel);
                                }
                                list.add(map);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        tv_top_3.setText(count + "");
                        commonAdapter.notifyDataSetChanged();
                        onLoad();
                    }
                });
    }

    private void attentionClick(boolean isAttention, String accId, final boolean isManager) {
        if (accId.equals(UserPreferences.getAccId())) {
            TSBError("无法关注自己！");
            return;
        }
        if (isAttention) {//取消关注
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.CANNELFOLLOW)
                    .addParam("target", accId)
                    .getResult(new HttpCallback() {
                        @Override
                        @HttpResultCut
                        public void success(JSONObject js, boolean success) {
                            try {
                                if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                    if (js.getString("code").equals("success")) {
                                        TSBSuccess("已取消");
                                        if (isManager) {
                                            Intent broad = new Intent(AudienceOperateFragment.action);
                                            broad.putExtra("isAttention", false);
                                            getActivity().sendBroadcast(broad);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {//未关注
            if (isManager){
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.FOLLOWMANAGER)
                        .addParam("attAccId", accId)
                        .addParam("cId", LiveRoomCache.getChannelId())
                        .getResult(new HttpCallback() {
                            @Override
                            @HttpResultCut
                            public void success(JSONObject js, boolean success) {
                                try {
                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                        if (js.getString("code").equals("success")) {
                                            LogUtil.d("关注主播");
                                            TSBSuccess("已关注！");
                                            Intent broad = new Intent(AudienceOperateFragment.action);
                                            broad.putExtra("isAttention", true);
                                            getActivity().sendBroadcast(broad);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }else {
                new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                        .addRouteUrl(API.FOLLOW)
                        .addParam("target", accId)
                        .getResult(new HttpCallback() {
                            @Override
                            @HttpResultCut
                            public void success(JSONObject js, boolean success) {
                                try {
                                    if (js.getString("code").equals(ResultCode.SUCCESS.getCode())) {
                                        if (js.getString("code").equals("success")) {
                                            LogUtil.d("关注普通用户");
                                            TSBSuccess("已关注！");
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        }
    }

    protected <T extends View> T find(int id) {
        return (T) findViewById(id);
    }
}