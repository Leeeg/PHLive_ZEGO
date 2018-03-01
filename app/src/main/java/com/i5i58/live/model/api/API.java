package com.i5i58.live.model.api;

/**
 * Created by Lee on 2017/4/7.
 */

public interface API {

    String REST_URL = "https://www.i5i58.com:7970";
//    String REST_URL = "https://www.i5i58.com:7971/v1";
//    String REST_URL = "http://192.168.1.99:5970/v1";  //Frank
    String WEBSOCKET = "ws://www.i5i58.com:6988/websocket";

    //登录
    String LOGINBYPHONE = "/account/loginByPhoneNo";
    String LOGINBYOPENID = "/account/loginByOpenId";
    String LOGINBYTOKEN = "/account/loginByToken";
    String THIRDLOGIN = "/account/loginByThird";

    //支付
    String CREATORDER = "/pay/createOrder";

    //设置
    String LIVENOTIFY = "/account/updateLivingNotify";
    String NODISTURB = "/account/updateNoDisturb";

    //ֱ直播间
    String GIFTCONFIG = "/channel/getGiftConfig";
    String MOUNTCONFIG = "/channel/getMountConfig";
    String ANIMATIONCONFIG = "/channel/getAnimationConfig";
    String WEEKOFFER = "/channel/getWeekOffer";
    String MAC = "/channel/getMicSequence";
    String PLAYER = "/channel/getViewerList";
    String TIGERLIST = "/channel/getMajiaList";
    String GETHEART = "/channel/getHeart";
    String TAKEHEART = "/channel/takeHeart";
    String GIVEHEART = "/channel/giveHeart";
    String SOFALIST = "/channel/getSofaList";
    String CHANNELCONFIG = "/channel/getChannelConfig";
    String OPENPUSH = "/channel/openPush";
    String CLOSEPUSH = "/channel/closePush";
    String RIGHTINFO = "/channel/getChannelRightInfo";
    String COVERIMAGE = "/channel/updateCoverImage";
    String CHANNELTITLE = "/channel/editChannelTitle";
    String FOLLOWMANAGER = "/channel/follow";
    String GETCHANNELINFO = "/channel/getChannelInfo";
    String SHARE = "/channel/socailShareSuccess";
    String GETNOTIFYCOUNT = "/channel/getNoticeCount";
    String GETNOTIFYABLE = "/channel/getNoticeLiveStatus";
    String NOTIFYLIVE = "/channel/noticeLive";

    String ENTERCHANNEL = "/channel/enterChannel";
    String EXITCHANNEL = "/channel/exitChannel";
    String GIVEGIFT = "/channel/giveGift";
    String DRIFTCOMMENT = "/channel/driftComment";


    //主播
    String GETMYPUSH = "/anchor/getMyPush";
    String GETMYPUSHMOBILE = "/anchor/getMyPushMobile";

    //RN_config
    String CHECKRN = "/config/checkRn";

    //首页
    String GETCAROUSEL = "/home/getCarousel";
    String GETHOTCHANNEL = "/home/getHotChannel";

    //Config
    String GETTYPECONFIG = "/config/getTypeConfig";
    String GETPLATFROMCONFIG = "/config/getPlatformConfig";

    //个人信息
    String SETICONSMALL = "/account/setIconSmall";
    String SETICONORG = "/account/setIconOrg";
    String GETMYPERSIONAL = "/account/getMyPersonal";
    String GETTAINFO = "/account/getTAInfo";
    String GETMYINFO = "/account/getMyInfo";
    String SENDCODE = "/account/sendCode";
    String CHECKCODE = "/account/checkCode";
    String REGISTER = "/account/register";
    String NICKNAME = "/account/setNickName";
    String STAGENAME = "/account/setStageName";
    String SIGNATURE = "/account/setPersonalBrief";
    String SETGENDER = "/account/setGender";
    String SETADDRESS = "/account/setAddress";
    String BIRTH = "/account/setBirth";
    String GETWALLET = "/account/getWallet";
    String GETMAJIAACCOUNTS = "/account/getMajiaAccounts";
    String GETFACEURL = "/account/getFaceUrlByAccId";
    String REPORT = "/account/reportUser";

    //关注
    String FOLLOW = "/social/follow";
    String ISFOLLOW = "/social/isFollow";
    String GETFOLLOWSTATUS = "/social/getFollowStatus";
    String CANNELFOLLOW = "/social/cancelFollow";
    String GETTAFANS = "/social/getTaFans";

    //搜索
    String QUERYNEWANCHOR = "/info/queryNewAnchor";
    String QUERYHOTWATCHANCHOR = "/info/queryLotWatchAnchor";
    String QUERYANCHOR = "/info/queryAnchor";
    String QUERYCHANNEL = "/info/queryChannel";

    //OSSs
//	String OSS_URL = "http://teststatic.i5i58.com";
    String OSS_URL = "https://gg78live.oss-cn-hangzhou.aliyuncs.com";
    String OSS_SYSTEM_ICON_URL = OSS_URL+"/SystemIcon/";
    String OSS_URL_GIFT = OSS_URL+"/Gifts/";
    String OSS_URL_MOUNTS = OSS_URL+"/Mounts/";
    String OSS_URL_HTML = OSS_URL+"/HTML/";
    String OSS_URL_ICON = OSS_URL+"/Icon/";
    String OSS_URL_CAROUSEL = OSS_URL+"/Carousel/";
    String OSS_URL_CHANNELCOVER = OSS_URL+"/ChannelCover/";
    String OSS_URL_RN = OSS_URL+"/Rn/Zip/Android/";
    String OSS_URL_SYSTEMICON = OSS_URL+"/Rn/Icon/";

}
