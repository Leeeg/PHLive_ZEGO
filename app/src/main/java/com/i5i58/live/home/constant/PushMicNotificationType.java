package com.i5i58.live.home.constant;

/**
 * Created by hzxuwen on 2016/7/22.
 */
public enum PushMicNotificationType {
    /**
     * 加入连麦队列通知
     */
    JOIN_QUEUE(1),
    /**
     * 退出连麦队列通知
     */
    EXIT_QUEUE(2),
    /**
     * 主播选择某人连麦
     */
    CONNECTING_MIC(3),
    /**
     * 主播断开某人连麦
     */
    DISCONNECT_MIC(4),
    /**
     * 观众拒绝主播的连麦选择
     */
    REJECT_CONNECTING(5);

    private int value;

    PushMicNotificationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PushMicNotificationType typeOfValue(int value) {
        for (PushMicNotificationType e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return JOIN_QUEUE;
    }
}
