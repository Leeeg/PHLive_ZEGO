package com.i5i58.live.home.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

/**
 * Created by Lee on 2017/5/4.
 */

public class ChatRoomMessageHelper implements Parcelable {


    private MessageEnum msgType;
    private String messageContext;
    private ChatRoomMessage chatRoomMessage;

    public MessageEnum getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageEnum msgType) {
        this.msgType = msgType;
    }

    public String getMessageContext() {
        return messageContext;
    }

    public void setMessageContext(String messageContext) {
        this.messageContext = messageContext;
    }

    public ChatRoomMessage getChatRoomMessage() {
        return chatRoomMessage;
    }

    public void setChatRoomMessage(ChatRoomMessage chatRoomMessage) {
        this.chatRoomMessage = chatRoomMessage;
    }

    public ChatRoomMessageHelper() {
    }

    public ChatRoomMessageHelper(MessageEnum msgType, String messageContext, ChatRoomMessage chatRoomMessage) {
        this.msgType = msgType;
        this.messageContext = messageContext;
        this.chatRoomMessage = chatRoomMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.msgType == null ? -1 : this.msgType.ordinal());
        dest.writeString(this.messageContext);
        dest.writeSerializable(this.chatRoomMessage);
    }

    protected ChatRoomMessageHelper(Parcel in) {
        int tmpMsgType = in.readInt();
        this.msgType = tmpMsgType == -1 ? null : MessageEnum.values()[tmpMsgType];
        this.messageContext = in.readString();
        this.chatRoomMessage = (ChatRoomMessage) in.readSerializable();
    }

    public static final Parcelable.Creator<ChatRoomMessageHelper> CREATOR = new Parcelable.Creator<ChatRoomMessageHelper>() {
        @Override
        public ChatRoomMessageHelper createFromParcel(Parcel source) {
            return new ChatRoomMessageHelper(source);
        }

        @Override
        public ChatRoomMessageHelper[] newArray(int size) {
            return new ChatRoomMessageHelper[size];
        }
    };
}
