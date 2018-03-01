package com.i5i58.live.common.utils.imageLoader;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

/**
 * Created by Lee on 2017/4/21.
 */

public class ImagePickerGLideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(path).into(imageView);
    }

    @Override
    public void clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }
}
