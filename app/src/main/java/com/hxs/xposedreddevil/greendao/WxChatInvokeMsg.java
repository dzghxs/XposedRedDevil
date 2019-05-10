package com.hxs.xposedreddevil.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class WxChatInvokeMsg {
    @Id
    private String msgId;
    private String msgSeq;
    private String createTime;
    @Generated(hash = 712944388)
    public WxChatInvokeMsg(String msgId, String msgSeq, String createTime) {
        this.msgId = msgId;
        this.msgSeq = msgSeq;
        this.createTime = createTime;
    }
    @Generated(hash = 449343089)
    public WxChatInvokeMsg() {
    }
    public String getMsgId() {
        return this.msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public String getMsgSeq() {
        return this.msgSeq;
    }
    public void setMsgSeq(String msgSeq) {
        this.msgSeq = msgSeq;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
