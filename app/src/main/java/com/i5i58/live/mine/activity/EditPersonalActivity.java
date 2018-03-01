package com.i5i58.live.mine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.aop.http.HttpResultCut;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.db.User;
import com.i5i58.live.common.enums.HttpEnum;
import com.i5i58.live.common.net.Http;
import com.i5i58.live.common.net.HttpCallback;
import com.i5i58.live.common.preferences.SysPreferences;
import com.i5i58.live.common.utils.file.FileUtils;
import com.i5i58.live.common.utils.imageLoader.ImagePickerGLideImageLoader;
import com.i5i58.live.common.utils.mathUtil.MathUtil;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.common.view.glide.GlideCircleTransform;
import com.i5i58.live.luban.Luban;
import com.i5i58.live.luban.OnCompressListener;
import com.i5i58.live.model.api.API;
import com.i5i58.live.model.entity.sys.SystemCache;
import com.i5i58.live.widget.DateTimeSelectorDialogBuilder;
import com.i5i58.live.widget.LocationSelectorDialogBuilder;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by Lee on 2017/4/20.
 */

@ContentView(R.layout.act_mine_editpersonal)
public class EditPersonalActivity extends BaseActivity implements DateTimeSelectorDialogBuilder.OnSaveListener, LocationSelectorDialogBuilder.OnSaveLocationLister {

    private int PICTURE_REQUEST_CODE    = 1000;
    private final int NICKNAME_REQUEST_CODE   = 101;
    private final int STAGENAME_REQUEST_CODE  = 102;
    private final int SINGURE_REQUEST_CODE    = 103;
    private final int SMALLIMAGE              = 1;
    private final int ORGIMAGE                = 2;
    private DateTimeSelectorDialogBuilder dialogBuilder;
    private LocationSelectorDialogBuilder locationBuilder;

    @ViewInject(R.id.table_top_tv_title)
    private TextView titleTxt;

    @ViewInject(R.id.tv_nickname)
    private TextView nickNameTxt;

    @ViewInject(R.id.tv_stageName)
    private TextView stageNameTxt;

    @ViewInject(R.id.tv_sex)
    private TextView sexTxt;

    @ViewInject(R.id.tv_birth)
    private TextView birthTxt;

    @ViewInject(R.id.tv_address)
    private TextView locationTxt;

    @ViewInject(R.id.tv_signature)
    private TextView signatureTxt;

    @ViewInject(R.id.person_msg_tv_openid)
    private TextView openIdTxt;

    @ViewInject(R.id.person_msg_tv_scre)
    private TextView scoreTxt;

    @ViewInject(R.id.edit_person_tv_star)
    private TextView person_tv_star;

    @ViewInject(R.id.iv_rank)
    private ImageView iv_rank;

    @ViewInject(R.id.edit_person_tv_scor)
    private TextView person_tv_score;

    @ViewInject(R.id.ri_image)
    private ImageView iconImg;

    @ViewInject(R.id.person_msg_iv_rich_lv)
    private ImageView richScoreImg;

    @Event(R.id.table_top_fl_back)
    private void backClick(View v){
        closeAct(this);
    }

    @Event(R.id.ll_qrcode)
    private void intnetToMyQrCode(View v){
        Intent intent = new Intent(EditPersonalActivity.this, MyQrCodeActivity.class);
        intentAct(this,intent);
    }

    @Event(R.id.ll_image)
    private void choseIconPopWindow(View v) {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.pop_myqrcode_share, null);

        TextView camera    = (TextView) popView.findViewById(R.id.pop_qrcode_share);
        TextView chose     = (TextView) popView.findViewById(R.id.pop_qrcode_save);
        TextView cancel   = (TextView) popView.findViewById(R.id.pop_qrcode_cancel);
        View     close    = popView.findViewById(R.id.pop_close);

        camera.setText("拍照");
        chose.setText("从相册选取");
        cancel.setText("取消");

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    //拍照
                    case R.id.pop_qrcode_share:
                        intent = new Intent(EditPersonalActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                        startActivityForResult(intent, PICTURE_REQUEST_CODE);
                        break;
                    //相册
                    case R.id.pop_qrcode_save:
                        intent = new Intent(EditPersonalActivity.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,false); // 是否是直接打开相机
                        startActivityForResult(intent, PICTURE_REQUEST_CODE);
                        break;

                    case R.id.pop_qrcode_cancel:
                    case R.id.pop_close:

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

        camera.setOnClickListener(listener);
        chose.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        close.setOnClickListener(listener);

        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Event(R.id.ll_sex)
    private void choseSexPopWindow(View v) {
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.pop_myqrcode_share, null);

        TextView camera    = (TextView) popView.findViewById(R.id.pop_qrcode_share);
        TextView chose     = (TextView) popView.findViewById(R.id.pop_qrcode_save);
        TextView cancel   = (TextView) popView.findViewById(R.id.pop_qrcode_cancel);
        View     close    = popView.findViewById(R.id.pop_close);

        camera.setText("男");
        chose.setText("女");
        cancel.setText("取消");

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.PopBottom);
        popWindow.setFocusable(true);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    //男
                    case R.id.pop_qrcode_share:
                        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                                .addRouteUrl(API.SETGENDER)
                                .addParam("gender", "0")
                                .getResult(new HttpCallback() {
                                    @Override
                                    @HttpResultCut
                                    public void success(JSONObject js, boolean success) {
                                        try {
                                            if (js.getString("code").equals("success")) {
                                                TSBSuccess("性别已更新");
                                                SystemCache.getPersonalMsg().getAccount().setGender(0);
                                                sexTxt.setText("男");
                                            } else {
                                                LogUtil.e("修改性别出错");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            LogUtil.e(e.toString());
                                        } catch (NullPointerException e){
                                            LogUtil.e(e.toString());
                                        }
                                    }
                                });
                        break;
                    //女
                    case R.id.pop_qrcode_save:
                        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                                .addRouteUrl(API.SETGENDER)
                                .addParam("gender", "1")
                                .getResult(new HttpCallback() {
                                    @Override
                                    @HttpResultCut
                                    public void success(JSONObject js, boolean success) {
                                        try {
                                            if (js.getString("code").equals("success")) {
                                                TSBSuccess("性别已更新");
                                                SystemCache.getPersonalMsg().getAccount().setGender(1);
                                                sexTxt.setText("女");
                                            } else {
                                                LogUtil.e("修改性别出错");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            LogUtil.e(e.toString());
                                        } catch (NullPointerException e){
                                            LogUtil.e(e.toString());
                                        }
                                    }
                                });
                        break;

                    case R.id.pop_qrcode_cancel:
                    case R.id.pop_close:

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

        camera.setOnClickListener(listener);
        chose.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        close.setOnClickListener(listener);

        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Event(R.id.ll_birth)
    private void choseBirthPopWindow(View v) {
        if (dialogBuilder == null) {
            dialogBuilder = DateTimeSelectorDialogBuilder.getInstance(this);
            dialogBuilder.setOnSaveListener(this);
        }
        dialogBuilder.show();
    }

    @Event(R.id.ll_addr)
    private void choseLocationPopWindow(View v) {
        if (locationBuilder == null) {
            locationBuilder = LocationSelectorDialogBuilder.getInstance(this);
            locationBuilder.setOnSaveLocationLister(this);
        }
        locationBuilder.show();
    }

    @Event({R.id.ll_nickname,R.id.ll_stagename,R.id.ll_signature})
    private void intentToModifiction(View v){
        Intent intent = new Intent(this,Modification.class);
        switch (v.getId()){
            case R.id.ll_nickname:
                intent.putExtra("title","昵称");
                intent.putExtra("hint",nickNameTxt.getText());
                startActivityForResult(intent,NICKNAME_REQUEST_CODE);
                break;
            case R.id.ll_stagename:
                intent.putExtra("title","艺名");
                intent.putExtra("isAnchor", SystemCache.getPersonalMsg().getAccount().isAnchor());
                intent.putExtra("hint",stageNameTxt.getText());
                startActivityForResult(intent,STAGENAME_REQUEST_CODE);
                break;
            case R.id.ll_signature:
                intent.putExtra("title","个性签名");
                intent.putExtra("hint",signatureTxt.getText());
                startActivityForResult(intent,SINGURE_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveLocation(String location, String provinceId, String cityId) {
        String s[] = location.split(" ");
        String state = s[0];
        String city = s[1];
        final String addre = s[0] + " " + s[1];
        final String addMsg = "{\"state\":\""+ state +"\",\"city\":\""+ city +"\"}";
        new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                .addRouteUrl(API.SETADDRESS)
                .addParam("address", addMsg)
                .getResult(new HttpCallback() {
                    @Override
                    @HttpResultCut
                    public void success(JSONObject js, boolean success) {
                        try {
                            if (js.getString("code").equals("success")) {
                                TSBSuccess("地址已更新");
                                SystemCache.getPersonalMsg().getAccount().setLocation(addMsg);
                                locationTxt.setText(addre);
                            } else {
                                LogUtil.e("修改地址出错");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e(e.toString());
                        } catch (NullPointerException e){
                            LogUtil.e(e.toString());
                        }
                    }
                });
    }

    @Override
    public void onSaveSelectedDate(final String selectedDate) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long dateNow = Long.valueOf((new Date().getTime()));
            Date date = simpleDateFormat.parse(selectedDate);
            final long timeTemp = date.getTime();
            if (timeTemp > dateNow) {
                TSBError("日期设置有误");
                return;
            }
            new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                    .addRouteUrl(API.BIRTH)
                    .addParam("brith", String.valueOf(timeTemp))
                    .getResult(new HttpCallback() {
                        @Override
                        @HttpResultCut
                        public void success(JSONObject js, boolean success) {
                            try {
                                if (js.getString("code").equals("success")) {
                                    TSBSuccess("生日已更新");
                                    SystemCache.getPersonalMsg().getAccount().setBirthDate(timeTemp);
                                    birthTxt.setText(selectedDate);
                                } else {
                                    LogUtil.e("修改生日出错");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                LogUtil.e(e.toString());
                            } catch (NullPointerException e){
                                LogUtil.e(e.toString());
                            }
                        }
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ImagePicker.RESULT_CODE_ITEMS:
                if (data != null && requestCode == PICTURE_REQUEST_CODE) {
                    final ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    final String smallUuid = UUID.randomUUID().toString();
                    upLoadPicture(images.get(1).path,smallUuid,SMALLIMAGE);

                    Glide.with(this).load(images.get(1).path)
                            .transform(new GlideCircleTransform(this))
                            .placeholder(R.drawable.icon_mine_personal_default)
                            .error(R.drawable.icon_myqrcode_default)
                            .into(iconImg);

                    Luban.get(this)
                            .load(new File(images.get(0).path))                     //传入要压缩的图片
                            .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                            .setCompressListener(new OnCompressListener() { //设置回调

                                @Override
                                public void onStart() {

                                }
                                @Override
                                public void onSuccess(final File file) {//上传高清图
                                    final String orgUuid = UUID.randomUUID().toString();
                                    upLoadPicture(file.getPath(),orgUuid,ORGIMAGE);
                                }
                                @Override
                                public void onError(Throwable e) {
                                    LogUtil.e("图片压缩失败");
                                }
                            }).launch();    //启动压缩
                }
                break;
            case NICKNAME_REQUEST_CODE:
                if (null != data){
                    nickNameTxt.setText(data.getStringExtra("text"));
                    SystemCache.getPersonalMsg().getAccount().setName(data.getStringExtra("text"));
                    TSBSuccess("昵称已更新");
                }
                break;
            case STAGENAME_REQUEST_CODE:
                if (null != data){
                    stageNameTxt.setText(data.getStringExtra("text"));
                    SystemCache.getPersonalMsg().getAccount().setStageName(data.getStringExtra("text"));
                    TSBSuccess("艺名已更新");
                }
                break;
            case SINGURE_REQUEST_CODE:
                if (null != data){
                    signatureTxt.setText(data.getStringExtra("text"));
                    SystemCache.getPersonalMsg().getAccount().setPersonalBrief(data.getStringExtra("text"));
                    TSBSuccess("个性签名已更新");
                }
                break;
            default:
                break;
        }
    }

    private void init() {
        try {
            titleTxt.setText(R.string.personal_msg);
            Glide.with(this).load(StringUtil.checkIcon(SystemCache.getPersonalMsg().getAccount().getFaceSmallUrl()))
                    .transform(new GlideCircleTransform(this))
                    .placeholder(R.drawable.icon_mine_personal_default)
                    .error(R.drawable.icon_myqrcode_default)
                    .into(iconImg);
            nickNameTxt.setText(SystemCache.getPersonalMsg().getAccount().getName());
            if (0 != SystemCache.getPersonalMsg().getAccount().getGender()){
                sexTxt.setText("女");
            }
            if (null != SystemCache.getPersonalMsg().getAccount().getStageName()){
                stageNameTxt.setText(SystemCache.getPersonalMsg().getAccount().getStageName());
            }
            if (0 != SystemCache.getPersonalMsg().getAccount().getBirthDate()){
                birthTxt.setText(StringUtil.stampToDate(String.valueOf(SystemCache.getPersonalMsg().getAccount().getBirthDate())));
            }
            if (null != SystemCache.getPersonalMsg().getAccount().getLocation()){
                locationTxt.setText(StringUtil.getLocation(SystemCache.getPersonalMsg().getAccount().getLocation()));
            }
            if (null != SystemCache.getPersonalMsg().getAccount().getPersonalBrief()){
                signatureTxt.setText(SystemCache.getPersonalMsg().getAccount().getPersonalBrief());
            }
            openIdTxt.setText(SystemCache.getPersonalMsg().getAccount().getOpenId());
            long richScore = SystemCache.getPersonalMsg().getAccount().getRichScore();
            InputStream is = getResources().openRawResource(FileUtils.getResouceID("drawable", "lvl_" + MathUtil.getRichScoreLevel(richScore)));
            Bitmap mBitmap = BitmapFactory.decodeStream(is);
            richScoreImg.setImageBitmap(mBitmap);
            String score = String.valueOf(SystemCache.getPersonalMsg().getAccount().getScore());
            scoreTxt.setText(score);
            long[] scoreArray = MathUtil.checkScore(Long.valueOf(score));
            person_tv_star.setText(String.valueOf(scoreArray[0]));
            person_tv_score.setText(String.valueOf(scoreArray[1]));
            setStarColor(String.valueOf(scoreArray[0]));
        } catch (NullPointerException e){
            LogUtil.e(e.toString());
        }
    }

    /**
     * 通过手机号查询历史登陆用户
     */
    private void selectUserByPhone(String phone, String icon){
        try {
            List<User> users = TigerApplication.getInstance().getDbManager().selector(User.class)
                    .where("phone","=",phone).limit(1).findAll();
            User user = users.get(0);
            if (null != user){
                user.setIcon(icon);
                TigerApplication.getInstance().getDbManager().update(user);
            }
        } catch (DbException e) {
            e.printStackTrace();
            LogUtil.e("通过手机号查询历史登陆用户出错");
        }
    }

    private void upLoadPicture(String filePath, final String uuid, final int type) {
        PutObjectRequest put = new PutObjectRequest("gg78live", "Icon/" + uuid + ".png", FileUtils.File2byte(filePath));
        TigerApplication.getInstance().oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (type == SMALLIMAGE){
                    new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                            .addRouteUrl(API.SETICONSMALL)
                            .addParam("iconUrl", uuid + ".png")
                            .getResult(new HttpCallback() {
                                @Override
                                @HttpResultCut
                                public void success(JSONObject js, boolean success) {
                                    try {
                                        if (js.getString("code").equals("success")) {
                                            LogUtil.e("上传小图像成功");
                                            SysPreferences.saveIcon(uuid  + ".png");
                                            if (SysPreferences.isLoginByPhone()){
                                                selectUserByPhone(SysPreferences.getPhone(), uuid + ".png");
                                            }
                                        } else {
                                            LogUtil.e("上传小头像出错");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                    }
                                }
                            });
                }else {
                    new Http(HttpEnum.POST).addRootUrl(API.REST_URL)
                            .addRouteUrl(API.SETICONORG)
                            .addParam("iconUrl", uuid + ".png")
                            .getResult(new HttpCallback() {
                                @Override
                                @HttpResultCut
                                public void success(JSONObject js, boolean success) {
                                    try {
                                        if (js.getString("code").equals("success")) {
                                            LogUtil.e("上传大头像成功");
                                        } else {
                                            LogUtil.e("上传大头像出错");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e(e.toString());
                                    }
                                }
                            });
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    LogUtil.e("OSS : 本地异常如网络异常等");
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtil.e("ErrorCode" + serviceException.getErrorCode());
                    LogUtil.e("RequestId" + serviceException.getRequestId());
                    LogUtil.e("HostId" + serviceException.getHostId());
                    LogUtil.e("RawMessage" + serviceException.getRawMessage());
                }
            }
        });
    }

    private void setStarColor(String score) {
        switch (score){
            case "1":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_01));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_1));
                break;
            case "2":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_02));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_2));
                break;
            case "3":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_03));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_3));
                break;
            case "4":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_04));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_4));
                break;
            case "5":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_05));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_5));
                break;
            case "6":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_06));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_6));
                break;
            case "7":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_07));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_7));
                break;
            case "8":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_08));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_8));
                break;
            case "9":
                iv_rank.setBackgroundDrawable(getResources().getDrawable(R.drawable.normal_level_09));
                person_tv_star.setTextColor(getResources().getColor(R.color.score_lv_9));
                break;
            default:
                break;
        }
    }

    private void configImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImagePickerGLideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);  //显示拍照按钮
        imagePicker.setMultiMode(false);  //多选
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        appManager.addActivity(this);
        init();
        //初始化ImagePicker
        configImagePicker();
    }
}
