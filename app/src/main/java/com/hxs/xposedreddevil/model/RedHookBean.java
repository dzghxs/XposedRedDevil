package com.hxs.xposedreddevil.model;

public class RedHookBean {
    /**
     * msg : {"appmsg":{"des":"我给你发了一个红包，赶紧去拆! 祝：恭喜发财，大吉大利！","wcpayinfo":{"imageid":"","paymsgid":"1000039401201901027015344247045","sendertitle":"恭喜发财，大吉大利","innertype":"0","receiverc2cshowsourcemd5":"","subtype":"0","senderdes":"查看红包","scenetext":"微信红包","locallogoicon":"c2c_hongbao_icon_cn","expressionurl":"","nativeurl":"wxpay://c2cbizmessagehandler/hongbao/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201901027015344247045&sendusername=wxid_bovwmfisc26g41&transid=970c9972d26da84b3a657cfbc194475b6bc4cdba0abb7ebbf6f99d972f85c8da32c87e644e88095b3624618850aeca1e","senderc2cshowsourcemd5":"","broaden":{"typeid":"","iosversion":"","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045","nativeurl":"","androidversion":""},"senderc2cshowsourceurl":"","receivertitle":"恭喜发财，大吉大利","receiverc2cshowsourceurl":"","recshowsourceurl":"","detailshowsourceurl":"","expressiontype":"0","templateid":"7a2a165d31da7fce6dd77e05c300028a","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045","receiverdes":"领取红包","imageaeskey":"","recshowsourcemd5":"","detailshowsourcemd5":"","iconurl":"http://wx.gtimg.com/hongbao/1701/hb.png","sceneid":"1002","invalidtime":"0","corpname":"","imagelength":"0"},"appid":"","sdkver":"","type":"2001","title":"微信红包","thumburl":"http://wx.gtimg.com/hongbao/1701/hb.png","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045"},"fromusername":"wxid_bovwmfisc26g41"}
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
         * appmsg : {"des":"我给你发了一个红包，赶紧去拆! 祝：恭喜发财，大吉大利！","wcpayinfo":{"imageid":"","paymsgid":"1000039401201901027015344247045","sendertitle":"恭喜发财，大吉大利","innertype":"0","receiverc2cshowsourcemd5":"","subtype":"0","senderdes":"查看红包","scenetext":"微信红包","locallogoicon":"c2c_hongbao_icon_cn","expressionurl":"","nativeurl":"wxpay://c2cbizmessagehandler/hongbao/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201901027015344247045&sendusername=wxid_bovwmfisc26g41&transid=970c9972d26da84b3a657cfbc194475b6bc4cdba0abb7ebbf6f99d972f85c8da32c87e644e88095b3624618850aeca1e","senderc2cshowsourcemd5":"","broaden":{"typeid":"","iosversion":"","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045","nativeurl":"","androidversion":""},"senderc2cshowsourceurl":"","receivertitle":"恭喜发财，大吉大利","receiverc2cshowsourceurl":"","recshowsourceurl":"","detailshowsourceurl":"","expressiontype":"0","templateid":"7a2a165d31da7fce6dd77e05c300028a","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045","receiverdes":"领取红包","imageaeskey":"","recshowsourcemd5":"","detailshowsourcemd5":"","iconurl":"http://wx.gtimg.com/hongbao/1701/hb.png","sceneid":"1002","invalidtime":"0","corpname":"","imagelength":"0"},"appid":"","sdkver":"","type":"2001","title":"微信红包","thumburl":"http://wx.gtimg.com/hongbao/1701/hb.png","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045"}
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
             * wcpayinfo : {"imageid":"","paymsgid":"1000039401201901027015344247045","sendertitle":"恭喜发财，大吉大利","innertype":"0","receiverc2cshowsourcemd5":"","subtype":"0","senderdes":"查看红包","scenetext":"微信红包","locallogoicon":"c2c_hongbao_icon_cn","expressionurl":"","nativeurl":"wxpay://c2cbizmessagehandler/hongbao/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201901027015344247045&sendusername=wxid_bovwmfisc26g41&transid=970c9972d26da84b3a657cfbc194475b6bc4cdba0abb7ebbf6f99d972f85c8da32c87e644e88095b3624618850aeca1e","senderc2cshowsourcemd5":"","broaden":{"typeid":"","iosversion":"","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045","nativeurl":"","androidversion":""},"senderc2cshowsourceurl":"","receivertitle":"恭喜发财，大吉大利","receiverc2cshowsourceurl":"","recshowsourceurl":"","detailshowsourceurl":"","expressiontype":"0","templateid":"7a2a165d31da7fce6dd77e05c300028a","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045","receiverdes":"领取红包","imageaeskey":"","recshowsourcemd5":"","detailshowsourcemd5":"","iconurl":"http://wx.gtimg.com/hongbao/1701/hb.png","sceneid":"1002","invalidtime":"0","corpname":"","imagelength":"0"}
             * appid :
             * sdkver :
             * type : 2001
             * title : 微信红包
             * thumburl : http://wx.gtimg.com/hongbao/1701/hb.png
             * url : https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045
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
                 * imageid :
                 * paymsgid : 1000039401201901027015344247045
                 * sendertitle : 恭喜发财，大吉大利
                 * innertype : 0
                 * receiverc2cshowsourcemd5 :
                 * subtype : 0
                 * senderdes : 查看红包
                 * scenetext : 微信红包
                 * locallogoicon : c2c_hongbao_icon_cn
                 * expressionurl :
                 * nativeurl : wxpay://c2cbizmessagehandler/hongbao/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201901027015344247045&sendusername=wxid_bovwmfisc26g41&transid=970c9972d26da84b3a657cfbc194475b6bc4cdba0abb7ebbf6f99d972f85c8da32c87e644e88095b3624618850aeca1e
                 * senderc2cshowsourcemd5 :
                 * broaden : {"typeid":"","iosversion":"","url":"https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045","nativeurl":"","androidversion":""}
                 * senderc2cshowsourceurl :
                 * receivertitle : 恭喜发财，大吉大利
                 * receiverc2cshowsourceurl :
                 * recshowsourceurl :
                 * detailshowsourceurl :
                 * expressiontype : 0
                 * templateid : 7a2a165d31da7fce6dd77e05c300028a
                 * url : https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045
                 * receiverdes : 领取红包
                 * imageaeskey :
                 * recshowsourcemd5 :
                 * detailshowsourcemd5 :
                 * iconurl : http://wx.gtimg.com/hongbao/1701/hb.png
                 * sceneid : 1002
                 * invalidtime : 0
                 * corpname :
                 * imagelength : 0
                 */

                private String imageid;
                private String paymsgid;
                private String sendertitle;
                private String innertype;
                private String receiverc2cshowsourcemd5;
                private String subtype;
                private String senderdes;
                private String scenetext;
                private String locallogoicon;
                private String expressionurl;
                private String nativeurl;
                private String senderc2cshowsourcemd5;
                private BroadenBean broaden;
                private String senderc2cshowsourceurl;
                private String receivertitle;
                private String receiverc2cshowsourceurl;
                private String recshowsourceurl;
                private String detailshowsourceurl;
                private String expressiontype;
                private String templateid;
                private String url;
                private String receiverdes;
                private String imageaeskey;
                private String recshowsourcemd5;
                private String detailshowsourcemd5;
                private String iconurl;
                private String sceneid;
                private String invalidtime;
                private String corpname;
                private String imagelength;

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

                public String getReceiverc2cshowsourcemd5() {
                    return receiverc2cshowsourcemd5;
                }

                public void setReceiverc2cshowsourcemd5(String receiverc2cshowsourcemd5) {
                    this.receiverc2cshowsourcemd5 = receiverc2cshowsourcemd5;
                }

                public String getSubtype() {
                    return subtype;
                }

                public void setSubtype(String subtype) {
                    this.subtype = subtype;
                }

                public String getSenderdes() {
                    return senderdes;
                }

                public void setSenderdes(String senderdes) {
                    this.senderdes = senderdes;
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

                public String getExpressionurl() {
                    return expressionurl;
                }

                public void setExpressionurl(String expressionurl) {
                    this.expressionurl = expressionurl;
                }

                public String getNativeurl() {
                    return nativeurl;
                }

                public void setNativeurl(String nativeurl) {
                    this.nativeurl = nativeurl;
                }

                public String getSenderc2cshowsourcemd5() {
                    return senderc2cshowsourcemd5;
                }

                public void setSenderc2cshowsourcemd5(String senderc2cshowsourcemd5) {
                    this.senderc2cshowsourcemd5 = senderc2cshowsourcemd5;
                }

                public BroadenBean getBroaden() {
                    return broaden;
                }

                public void setBroaden(BroadenBean broaden) {
                    this.broaden = broaden;
                }

                public String getSenderc2cshowsourceurl() {
                    return senderc2cshowsourceurl;
                }

                public void setSenderc2cshowsourceurl(String senderc2cshowsourceurl) {
                    this.senderc2cshowsourceurl = senderc2cshowsourceurl;
                }

                public String getReceivertitle() {
                    return receivertitle;
                }

                public void setReceivertitle(String receivertitle) {
                    this.receivertitle = receivertitle;
                }

                public String getReceiverc2cshowsourceurl() {
                    return receiverc2cshowsourceurl;
                }

                public void setReceiverc2cshowsourceurl(String receiverc2cshowsourceurl) {
                    this.receiverc2cshowsourceurl = receiverc2cshowsourceurl;
                }

                public String getRecshowsourceurl() {
                    return recshowsourceurl;
                }

                public void setRecshowsourceurl(String recshowsourceurl) {
                    this.recshowsourceurl = recshowsourceurl;
                }

                public String getDetailshowsourceurl() {
                    return detailshowsourceurl;
                }

                public void setDetailshowsourceurl(String detailshowsourceurl) {
                    this.detailshowsourceurl = detailshowsourceurl;
                }

                public String getExpressiontype() {
                    return expressiontype;
                }

                public void setExpressiontype(String expressiontype) {
                    this.expressiontype = expressiontype;
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

                public String getRecshowsourcemd5() {
                    return recshowsourcemd5;
                }

                public void setRecshowsourcemd5(String recshowsourcemd5) {
                    this.recshowsourcemd5 = recshowsourcemd5;
                }

                public String getDetailshowsourcemd5() {
                    return detailshowsourcemd5;
                }

                public void setDetailshowsourcemd5(String detailshowsourcemd5) {
                    this.detailshowsourcemd5 = detailshowsourcemd5;
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

                public String getCorpname() {
                    return corpname;
                }

                public void setCorpname(String corpname) {
                    this.corpname = corpname;
                }

                public String getImagelength() {
                    return imagelength;
                }

                public void setImagelength(String imagelength) {
                    this.imagelength = imagelength;
                }

                public static class BroadenBean {
                    /**
                     * typeid :
                     * iosversion :
                     * url : https://wxapp.tenpay.com/mmpayhb/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201901027015344247045
                     * nativeurl :
                     * androidversion :
                     */

                    private String typeid;
                    private String iosversion;
                    private String url;
                    private String nativeurl;
                    private String androidversion;

                    public String getTypeid() {
                        return typeid;
                    }

                    public void setTypeid(String typeid) {
                        this.typeid = typeid;
                    }

                    public String getIosversion() {
                        return iosversion;
                    }

                    public void setIosversion(String iosversion) {
                        this.iosversion = iosversion;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public String getNativeurl() {
                        return nativeurl;
                    }

                    public void setNativeurl(String nativeurl) {
                        this.nativeurl = nativeurl;
                    }

                    public String getAndroidversion() {
                        return androidversion;
                    }

                    public void setAndroidversion(String androidversion) {
                        this.androidversion = androidversion;
                    }
                }
            }
        }
    }
}
