package com.i5i58.live.common.utils.imgUtil;

/**
 * Created by Lee on 2017/4/27.
 */

public class MyImageLoader {
//
//    public static void load(final ImageView imageView, final int giftId, ImageLoaderEnum loaderEnum){
//        if (loaderEnum == ImageLoaderEnum.GIFT){
//            Bitmap giftBitmap = LiveRoomCache.giftMap.get(giftId);
//            if (null != giftBitmap){
//                imageView.setImageBitmap(giftBitmap);
//                return;
//            }else {
//                String fileName = LiveRoomCache.getGiftConfigMap().get(giftId).getPath();
//                File giftFile = new File(SystemCache.BASE_PATH + SystemCache.GIFT, fileName);
//                if (giftFile.exists()){
//                    try {
//                        FileInputStream fis = new FileInputStream(giftFile.getPath());
//                        giftBitmap = BitmapFactory.decodeStream(fis);
//                        imageView.setImageBitmap(giftBitmap);
//                        LiveRoomCache.giftMap.put(giftId,giftBitmap);
//                        return;
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    final Bitmap finalGiftBitmap = giftBitmap;
//                    Glide.with(SystemCache.getContext())
//                            .load(API.OSS_URL_GIFT + fileName + ".png")
//                            .asBitmap()
//                            .into(new SimpleTarget<Bitmap>() {
//                                @Override
//                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                    imageView.setImageBitmap(finalGiftBitmap);
//                                    LiveRoomCache.giftMap.put(giftId, finalGiftBitmap);
//                                }
//                            });
//                }
//            }
//        }else if(loaderEnum == ImageLoaderEnum.MOUNT){
//
//        }
//    }

}
