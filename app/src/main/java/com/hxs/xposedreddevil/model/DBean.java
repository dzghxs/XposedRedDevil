package com.hxs.xposedreddevil.model;

import java.util.List;

public class DBean {

    /**
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * fromusername : wxid_mn13yx8bnr9q21
         */

        private AppmsgBean appmsg;
        private String fromusername;

        public AppmsgBean getAppmsg() {
            return appmsg;
        }

        public void setAppmsg(AppmsgBean appmsg) {
            this.appmsg = appmsg;
        }

        public String getFromusername() {
            return fromusername;
        }

        public void setFromusername(String fromusername) {
            this.fromusername = fromusername;
        }

        public static class AppmsgBean {
            /**
             * des : 我给你发了一个红包，赶紧去拆!
             * wcpayinfo : {"receivertitle":"恭喜发财，大吉大利","paymsgid":"1000039401201808237013301500303","sendertitle":"恭喜发财，大吉大利","innertype":"0","templateid":"7a2a165d31da7fce6dd77e05c300028a","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201808237013301500303&ver=6&sign=c436c473377bea9d6d3a9310e27e816293d3f0e0ecaf4e95615e1ec0b5a321d9f49a74ce4a89a6179171eb42e902bad1f78fd9b8e0286cc13a6518636c8f3b00069b0e5dea594b80bd106dfcff360846","receiverdes":"领取红包","senderdes":"查看红包","iconurl":"https://wx.gtimg.com/hongbao/1800/hb.png","sceneid":"1002","invalidtime":"1535097362","scenetext":[{"content":"微信红包"},{"content":"微信红包"}],"locallogoicon":"c2c_hongbao_icon_cn","nativeurl":"wxpay://c2cbizmessagehandler/hongbao/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201808237013301500303&sendusername=wxid_mn13yx8bnr9q21&ver=6&sign=c436c473377bea9d6d3a9310e27e816293d3f0e0ecaf4e95615e1ec0b5a321d9f49a74ce4a89a6179171eb42e902bad1f78fd9b8e0286cc13a6518636c8f3b00069b0e5dea594b80bd106dfcff360846"}
             * appid :
             * sdkver :
             * type : 2001
             * title : 微信红包
             * thumburl : https://wx.gtimg.com/hongbao/1800/hb.png
             * url : https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201808237013301500303&ver=6&sign=c436c473377bea9d6d3a9310e27e816293d3f0e0ecaf4e95615e1ec0b5a321d9f49a74ce4a89a6179171eb42e902bad1f78fd9b8e0286cc13a6518636c8f3b00069b0e5dea594b80bd106dfcff360846
             */

            private String des;
            private WcpayinfoBean wcpayinfo;
            private String appid;
            private String sdkver;
            private String type;
            private String title;
            private String thumburl;
            private String url;

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public WcpayinfoBean getWcpayinfo() {
                return wcpayinfo;
            }

            public void setWcpayinfo(WcpayinfoBean wcpayinfo) {
                this.wcpayinfo = wcpayinfo;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getSdkver() {
                return sdkver;
            }

            public void setSdkver(String sdkver) {
                this.sdkver = sdkver;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getThumburl() {
                return thumburl;
            }

            public void setThumburl(String thumburl) {
                this.thumburl = thumburl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public static class WcpayinfoBean {
                /**
                 * receivertitle : 恭喜发财，大吉大利
                 * paymsgid : 1000039401201808237013301500303
                 * sendertitle : 恭喜发财，大吉大利
                 * innertype : 0
                 * templateid : 7a2a165d31da7fce6dd77e05c300028a
                 * url : https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201808237013301500303&ver=6&sign=c436c473377bea9d6d3a9310e27e816293d3f0e0ecaf4e95615e1ec0b5a321d9f49a74ce4a89a6179171eb42e902bad1f78fd9b8e0286cc13a6518636c8f3b00069b0e5dea594b80bd106dfcff360846
                 * receiverdes : 领取红包
                 * senderdes : 查看红包
                 * iconurl : https://wx.gtimg.com/hongbao/1800/hb.png
                 * sceneid : 1002
                 * invalidtime : 1535097362
                 * scenetext : [{"content":"微信红包"},{"content":"微信红包"}]
                 * locallogoicon : c2c_hongbao_icon_cn
                 * nativeurl : wxpay://c2cbizmessagehandler/hongbao/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201808237013301500303&sendusername=wxid_mn13yx8bnr9q21&ver=6&sign=c436c473377bea9d6d3a9310e27e816293d3f0e0ecaf4e95615e1ec0b5a321d9f49a74ce4a89a6179171eb42e902bad1f78fd9b8e0286cc13a6518636c8f3b00069b0e5dea594b80bd106dfcff360846
                 */

                private String receivertitle;
                private String paymsgid;
                private String sendertitle;
                private String innertype;
                private String templateid;
                private String url;
                private String receiverdes;
                private String senderdes;
                private String iconurl;
                private String sceneid;
                private String invalidtime;
                private String locallogoicon;
                private String nativeurl;
                private List<ScenetextBean> scenetext;

                public String getReceivertitle() {
                    return receivertitle;
                }

                public void setReceivertitle(String receivertitle) {
                    this.receivertitle = receivertitle;
                }

                public String getPaymsgid() {
                    return paymsgid;
                }

                public void setPaymsgid(String paymsgid) {
                    this.paymsgid = paymsgid;
                }

                public String getSendertitle() {
                    return sendertitle;
                }

                public void setSendertitle(String sendertitle) {
                    this.sendertitle = sendertitle;
                }

                public String getInnertype() {
                    return innertype;
                }

                public void setInnertype(String innertype) {
                    this.innertype = innertype;
                }

                public String getTemplateid() {
                    return templateid;
                }

                public void setTemplateid(String templateid) {
                    this.templateid = templateid;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getReceiverdes() {
                    return receiverdes;
                }

                public void setReceiverdes(String receiverdes) {
                    this.receiverdes = receiverdes;
                }

                public String getSenderdes() {
                    return senderdes;
                }

                public void setSenderdes(String senderdes) {
                    this.senderdes = senderdes;
                }

                public String getIconurl() {
                    return iconurl;
                }

                public void setIconurl(String iconurl) {
                    this.iconurl = iconurl;
                }

                public String getSceneid() {
                    return sceneid;
                }

                public void setSceneid(String sceneid) {
                    this.sceneid = sceneid;
                }

                public String getInvalidtime() {
                    return invalidtime;
                }

                public void setInvalidtime(String invalidtime) {
                    this.invalidtime = invalidtime;
                }

                public String getLocallogoicon() {
                    return locallogoicon;
                }

                public void setLocallogoicon(String locallogoicon) {
                    this.locallogoicon = locallogoicon;
                }

                public String getNativeurl() {
                    return nativeurl;
                }

                public void setNativeurl(String nativeurl) {
                    this.nativeurl = nativeurl;
                }

                public List<ScenetextBean> getScenetext() {
                    return scenetext;
                }

                public void setScenetext(List<ScenetextBean> scenetext) {
                    this.scenetext = scenetext;
                }

                public static class ScenetextBean {
                    /**
                     * content : 微信红包
                     */

                    private String content;

                    public String getContent() {
                        return content;
                    }

                    public void setContent(String content) {
                        this.content = content;
                    }
                }
            }
        }
    }
}
