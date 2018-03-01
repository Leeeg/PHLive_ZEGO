package com.i5i58.live.main.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.i5i58.live.R;
import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.preferences.UserPreferences;
import com.i5i58.live.common.utils.string.StringUtil;
import com.i5i58.live.common.utils.system.MyStatusBarUtil;
import com.i5i58.live.common.view.imageVIew.RoundImageView;

public class Accredit extends BaseActivity {
	
	private ImageView im_close;
    private RoundImageView iv_persion_image;
	private Button bt_login;
    private TextView tv_name;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        MyStatusBarUtil.StatusBarLightMode(this);

        setContentView(R.layout.accredit);
        initView();
	}

    private void initView() {
        tv_name = find(R.id.tv_name);
        if(null != UserPreferences.getNickName()){
            tv_name.setText(UserPreferences.getNickName());
        }
        iv_persion_image = find(R.id.iv_persion_image);
        if(null != UserPreferences.getFace()){
            Glide.with(this).load(StringUtil.checkIcon(UserPreferences.getFace()))
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(iv_persion_image);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = getIntent();
        if(null != intent){
            String Tag = intent.getStringExtra("APPKey");
            if (null != Tag) {
                Toast.makeText(Accredit.this, Tag, Toast.LENGTH_SHORT).show();
            }
        }
        init();
    }

    @SuppressLint("ResourceAsColor")
	private void init() {
		im_close = find(R.id.im_close);
		im_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		bt_login = find(R.id.bt_login);
		bt_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = getPackageManager().getLaunchIntentForPackage("com.i5i58.gameplaza");
                if (null != intent) {
                    intent.putExtra("userName", UserPreferences.getAccId());
                    intent.putExtra("password", UserPreferences.getToken());
                    intent.putExtra("kindId", "0");
                    intent.putExtra("openId", UserPreferences.getOpenId());
                    startActivity(intent);
                    finish();
                    Log.e("Preferences","intent------------");
                    Log.e("Preferences",intent+"");
//                    Accredit.this.setResult(Activity.RESULT_OK, intent);
//                    Accredit.this.finish();
                }else{
                    openBrowser("www.baidu.com");
                }
			}
		});
	}

    private void openBrowser(String url) {
        Uri uri = Uri.parse("https://" + url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Preferences","onActivityResult-----"+resultCode);
    }
}
