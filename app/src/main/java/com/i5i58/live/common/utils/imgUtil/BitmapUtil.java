package com.i5i58.live.common.utils.imgUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.i5i58.live.R;
import com.i5i58.live.TigerApplication;
import com.i5i58.live.model.entity.sys.SystemCache;

import org.xutils.common.util.LogUtil;

/**
 * Created by Lee on 2017/4/20.
 */

public class BitmapUtil {

    /**
     * 字符串生成二维码
     */
    public static Bitmap Create2DCode(String str) throws WriterException {
        LogUtil.d(str);
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        final int WHITE = 0xFFFFFFFF;
        // 整体为黑色
        final int BLACK = 0xFF000000;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                    // pixels[y * width + x-2] = BLACK ;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixel(0, 0, WHITE);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 通过view获取显示的图像
     * @param view
     * @return
     */
    public static Bitmap copyView(View view) {
        // 获取windows中最顶层的view
        view.buildDrawingCache();

        // 获取状态栏高度
//        Rect rect = new Rect();
//        view.getWindowVisibleDisplayFrame(rect);
//        int statusBarHeights = rect.top;
//        Display display = this.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
//        int widths = display.getWidth();
//        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }


    /**
     * 在图片上面写字
     * @param photo
     * @param str
     */
    public static Bitmap drawNewBitmap(Bitmap photo, String str) {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
        textPaint.setTextSize(18.0f);// 字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
        textPaint.setColor(SystemCache.getContext().getResources().getColor(R.color.red_fansclub));// 采用的颜色
        textPaint.setShadowLayer(3, 1, 1, 000000);// 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)

        int width = photo.getWidth();
        int hight = photo.getHeight();
        float textW = textPaint.measureText(str);
        float textX = hight+(width-hight-textW)/2;
        float textY = 23f;

        Log.e("bitmap", "=============width:" + width );
        Log.e("bitmap", "=============hight:" + hight );
        Log.e("bitmap", "=============textW:" + textW );
        Log.e("bitmap", "=============textX:" + textX );

        Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
        Canvas canvas = new Canvas(icon);// 初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); // 建立画笔
        photoPaint.setDither(true); // 获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);// 过滤一些

        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// 创建一个指定的新矩形的坐标
        RectF dst = new RectF(0, 0, width, hight);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到
        // dst使用的填充区photoPaint
        canvas.drawText(str, textX, textY, textPaint);// 绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        Log.e("bitmap", "=============iconX:" + icon.getWidth());
        Log.e("bitmap", "=============iconX:" + icon.getHeight());
        return icon;
    }

    public static Bitmap cutBitmap(Bitmap bm, float fl){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        float newWidth = width * fl;
        float newHeight = height * fl;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    /**
     * 字符串转换成图片
     * @param str
     * @return
     */
    public static Bitmap createBitmap(String str) {
        Bitmap bp = Bitmap.createBitmap(160, 50, Bitmap.Config.ARGB_8888); //画布大小
        Canvas c = new Canvas(bp);
        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        c.drawColor(Color.GRAY);//画布颜色

        Paint paint2 = new Paint();//画姓名前边的间隔
        paint2.setColor(Color.WHITE);
        paint2.setStrokeWidth(1f);
        c.drawLine(0, 0, 0, 30, paint2);

        Paint paint = new Paint();
        paint.setTextSize(40);//字体大小
        paint.setColor(Color.BLACK);//字体颜色
        paint.setFakeBoldText(false); //粗体
        paint.setTextSkewX(0);//斜度
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(str, 80, 35, paint);//文字位置
        c.save( Canvas.ALL_SAVE_FLAG );//保存
        c.restore();//
        return bp;
    }

    /**
     * 根据图片的名称获取对应的资源id
     * @param resourceName
     * @return
     */
    public int getDrawResourceID(String resourceName) {
        Context context = TigerApplication.getInstance().getBaseContext();
        Resources res = context.getResources();
        int picid = res.getIdentifier(resourceName,"drawable",context.getPackageName());
        return picid;
    }

}
