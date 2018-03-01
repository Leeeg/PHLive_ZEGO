package org.dync.giftlibrary.widget;

/**
 * Created by KathLine on 2017/1/8.
 */
public class GiftModel {

    private String giftId;
    private String giftName;
    private int addCount;
    private String giftPic;
    private String giftPrice;
    private String sendUserId;
    private String sendUserName;
    private String sendUserPic;
    private String hitCombo;
    private Long sendGiftTime;
    private int countType;
    private int giftCount;
    private int giftCtis;

    public static GiftModel create(String giftId, String giftName, int giftCont, String giftPic, String sendUserId,
                                   String sendUserName, String sendUserPic) {
        GiftModel giftModel = new GiftModel();
        giftModel.setGiftId(giftId);
        giftModel.setGiftName(giftName);
        giftModel.setAddCount(giftCont);
        giftModel.setGiftPic(giftPic);
        giftModel.setSendUserId(sendUserId);
        giftModel.setSendUserName(sendUserName);
        giftModel.setSendUserPic(sendUserPic);
        return giftModel;
    }

    public GiftModel() {
    }

    /**
     * 礼物面板
     * @param giftName
     * @param giftPic
     */
    public GiftModel(String giftName, String giftPic, String giftPrice){
        setGiftName(giftName);
        setGiftPic(giftPic);
        setGiftPrice(giftPrice);
    }

    public GiftModel(String giftId, String giftName, int addCont, String giftPic, String sendUserId,
                     String sendUserName, String sendUserPic, Long sendGiftTime, int giftType, int giftCount, int giftCtis) {
        setGiftId(giftId);
        setGiftName(giftName);
        setAddCount(addCont);
        setGiftPic(giftPic);
        setSendUserId(sendUserId);
        setSendUserName(sendUserName);
        setSendUserPic(sendUserPic);
        setSendGiftTime(sendGiftTime);
        setCountType(giftType);
        setGiftCount(giftCount);
        setGiftCtis(giftCtis);
    }

    public GiftModel(String giftId, String sendUserName, String sendUserPic, int addCont) {
        setGiftId(giftId);
        setSendUserName(sendUserName);
        setSendUserPic(sendUserPic);
        setAddCount(addCont);
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getAddCount() {
        return addCount;
    }

    public void setAddCount(int giftCount) {
        this.addCount = giftCount;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getSendUserPic() {
        return sendUserPic;
    }

    public void setSendUserPic(String sendUserPic) {
        this.sendUserPic = sendUserPic;
    }

    public String getGiftPic() {
        return giftPic;
    }

    public void setGiftPic(String giftPic) {
        this.giftPic = giftPic;
    }

    public String getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
    }

    public String getHitCombo() {
        return hitCombo;
    }

    public void setHitCombo(String hitCombo) {
        this.hitCombo = hitCombo;
    }

    public Long getSendGiftTime() {
        return sendGiftTime;
    }

    public void setSendGiftTime(Long sendGiftTime) {
        this.sendGiftTime = sendGiftTime;
    }

    public int getCountType() {
        return countType;
    }

    public void setCountType(int countType) {
        this.countType = countType;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public int getGiftCtis() {
        return giftCtis;
    }

    public void setGiftCtis(int giftCtis) {
        this.giftCtis = giftCtis;
    }
}
