package com.hxs.xposedreddevil.model;

public class MsgsBean {

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
         * fromusername : wxid_bovwmfisc26g41
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
             * des : 我给你发了一个红包，赶紧去拆! 祝：恭喜发财，大吉大利！
             * wcpayinfo : {"receivertitle":"恭喜发财，大吉大利","imageid":"","paymsgid":"1000039401201808237011262284001","sendertitle":"恭喜发财，大吉大利","innertype":"0","templateid":"7a2a165d31da7fce6dd77e05c300028a","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201808237011262284001","receiverdes":"领取红包","imageaeskey":"","senderdes":"查看红包","iconurl":"http://wx.gtimg.com/hongbao/1701/hb.png","sceneid":"1002","invalidtime":"0","scenetext":"微信红包","locallogoicon":"c2c_hongbao_icon_cn","imagelength":"0","nativeurl":"wxpay://c2cbizmessagehandler/hongbao/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201808237011262284001&sendusername=wxid_bovwmfisc26g41&transid=b065a826ed299d158b13c5575ac7cac6a99470a7022aa0f3f79963d992f76fc5c428d5341f322ebca5b0f4192225c3ca"}
             * appid :
             * sdkver :
             * type : 2001
             * title : 微信红包
             * thumburl : http://wx.gtimg.com/hongbao/1701/hb.png
             * url : https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201808237011262284001
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
                 * imageid :
                 * paymsgid : 1000039401201808237011262284001
                 * sendertitle : 恭喜发财，大吉大利
                 * innertype : 0
                 * templateid : 7a2a165d31da7fce6dd77e05c300028a
                 * url : https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201808237011262284001
                 * receiverdes : 领取红包
                 * imageaeskey :
                 * senderdes : 查看红包
                 * iconurl : http://wx.gtimg.com/hongbao/1701/hb.png
                 * sceneid : 1002
                 * invalidtime : 0
                 * scenetext : 微信红包
                 * locallogoicon : c2c_hongbao_icon_cn
                 * imagelength : 0
                 * nativeurl : wxpay://c2cbizmessagehandler/hongbao/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201808237011262284001&sendusername=wxid_bovwmfisc26g41&transid=b065a826ed299d158b13c5575ac7cac6a99470a7022aa0f3f79963d992f76fc5c428d5341f322ebca5b0f4192225c3ca
                 */

                private String receivertitle;
                private String imageid;
                private String paymsgid;
                private String sendertitle;
                private String innertype;
                private String templateid;
                private String url;
                private String receiverdes;
                private String imageaeskey;
                private String senderdes;
                private String iconurl;
                private String sceneid;
                private String invalidtime;
                private String scenetext;
                private String locallogoicon;
                private String imagelength;
                private String nativeurl;

                public String getReceivertitle() {
                    return receivertitle;
                }

                public void setReceivertitle(String receivertitle) {
                    this.receivertitle = receivertitle;
                }

                public String getImageid() {
                    return imageid;
                }

                public void setImageid(String imageid) {
                    this.imageid = imageid;
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

                public String getImageaeskey() {
                    return imageaeskey;
                }

                public void setImageaeskey(String imageaeskey) {
                    this.imageaeskey = imageaeskey;
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

                public String getScenetext() {
                    return scenetext;
                }

                public void setScenetext(String scenetext) {
                    this.scenetext = scenetext;
                }

                public String getLocallogoicon() {
                    return locallogoicon;
                }

                public void setLocallogoicon(String locallogoicon) {
                    this.locallogoicon = locallogoicon;
                }

                public String getImagelength() {
                    return imagelength;
                }

                public void setImagelength(String imagelength) {
                    this.imagelength = imagelength;
                }

                public String getNativeurl() {
                    return nativeurl;
                }

                public void setNativeurl(String nativeurl) {
                    this.nativeurl = nativeurl;
                }
            }
        }
    }
}
