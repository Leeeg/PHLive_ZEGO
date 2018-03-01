package com.i5i58.live.model.entity.sys;

import com.i5i58.live.ZegoAppHelper;
import com.i5i58.live.common.enums.AppUpdateEnum;

/**
 * Created by Lee on 2017/4/14.
 */

public class AppUpdate {


    /**
     * yunXinKey : ba7a0d3dc3914a9e5cbac325d3c6aeab
     * ossKey : 78xKgz5yZPGMjdtv
     * weiBoSecret : f006e60c140f7721e8b2aa4e1dcd7283
     * wechatPayKey : wx880fa74e302e49c6
     * zegoRtmpSignKey : P1wTTEUQaq0kFMz/m+4cG6G/Bd4EW6ytSvTE33QgYEE=
     * release : mine
     * wechatKey : wx880fa74e302e49c6
     * umengKey : 58660e541061d22cf9000384
     * wechatSecret : ebd78ceb6555d6856379370780417123
     * payType : third_pay
     * qqKey : 1105831799
     * weiBoKey : 1374374554
     * qqSecret : KEYDN8AnxhE3FBXL3zZ
     * weiBoCallBackUrl : http://www.panglaohu.com
     * shareUrl : http://www.i5i58.com:8082/home/social.html?cId=
     * appUpdate : noUpdate
     * homeType : [{"name":"热门","value":1024},{"name":"潮音乐","value":1,"sortId":1},{"name":"燃舞蹈","value":2,"sortId":2},{"name":"脱口秀","value":3,"sortId":3}]
     * zegoKey : 1989373496
     * ossSecret : PzsRMoiU4XsLFVURtNmi3jaxVeFoLT
     */

    private String yunXinKey;
    private String ossKey;
    private String ossSecret;
    private String wechatKey;
    private String wechatSecret;
    private String wechatPayKey;
    private String zegoKey;
    private String zegoRtmpSignKey;
    private String umengKey;
    private String weiBoKey;
    private String weiBoSecret;
    private String weiBoCallBackUrl;
    private String qqKey;
    private String qqSecret;

    private String payType;
    private String shareUrl;
    private String appUpdate;
    private String release;
    private String updateUrl;

    public String getYunXinKey() {
        return yunXinKey;
    }

    public void setYunXinKey(String yunXinKey) {
        this.yunXinKey = yunXinKey;
    }

    public String getOssKey() {
        return ossKey;
    }

    public void setOssKey(String ossKey) {
        this.ossKey = ossKey;
    }

    public String getWeiBoSecret() {
        return weiBoSecret;
    }

    public void setWeiBoSecret(String weiBoSecret) {
        this.weiBoSecret = weiBoSecret;
    }

    public String getWechatPayKey() {
        return wechatPayKey;
    }

    public void setWechatPayKey(String wechatPayKey) {
        this.wechatPayKey = wechatPayKey;
    }

    public String getZegoRtmpSignKey() {
        return zegoRtmpSignKey;
    }

    public void setZegoRtmpSignKey(String zegoRtmpSignKey) {
        this.zegoRtmpSignKey = zegoRtmpSignKey;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getWechatKey() {
        return wechatKey;
    }

    public void setWechatKey(String wechatKey) {
        this.wechatKey = wechatKey;
    }

    public String getUmengKey() {
        return umengKey;
    }

    public void setUmengKey(String umengKey) {
        this.umengKey = umengKey;
    }

    public String getWechatSecret() {
        return wechatSecret;
    }

    public void setWechatSecret(String wechatSecret) {
        this.wechatSecret = wechatSecret;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getQqKey() {
        return qqKey;
    }

    public void setQqKey(String qqKey) {
        this.qqKey = qqKey;
    }

    public String getWeiBoKey() {
        return weiBoKey;
    }

    public void setWeiBoKey(String weiBoKey) {
        this.weiBoKey = weiBoKey;
    }

    public String getQqSecret() {
        return qqSecret;
    }

    public void setQqSecret(String qqSecret) {
        this.qqSecret = qqSecret;
    }

    public String getWeiBoCallBackUrl() {
        return weiBoCallBackUrl;
    }

    public void setWeiBoCallBackUrl(String weiBoCallBackUrl) {
        this.weiBoCallBackUrl = weiBoCallBackUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        SystemCache.setShareUrl(shareUrl);
        this.shareUrl = shareUrl;
    }

    public AppUpdateEnum getAppUpdate() {
        if(null == appUpdate || appUpdate.equals("noUpdate")){
            return AppUpdateEnum.NOUPDATE;
        }else if(appUpdate.equals("forceUpdate")){
            return AppUpdateEnum.FORCEUPDATE;
        }else{
            return AppUpdateEnum.ENABLEUPDATE;
        }
    }

    public void setAppUpdate(String appUpdate) {
        this.appUpdate = appUpdate;
    }

    public String getZegoKey() {
        return zegoKey;
    }

    public void setZegoKey(String zegoKey) {
        ZegoAppHelper.INTERNATIONAL_APP_ID = Long.getLong(zegoKey);
        this.zegoKey = zegoKey;
    }

    public String getOssSecret() {
        return ossSecret;
    }

    public void setOssSecret(String ossSecret) {
        this.ossSecret = ossSecret;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
