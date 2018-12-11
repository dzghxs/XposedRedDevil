package com.hxs.xposedreddevil.model;

public class VersionBean {

    /**
     * code : 200
     * msg : 成功
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * versionCode : 8
         * apkurl : https://raw.githubusercontent.com/dzghxs/XposedRedDevil/master/app/ä¸å°±æ¯å¾®ä¿¡çº¢åä¹.apk
         * updateContent : 1.优化试挂操作逻辑，判断是否为判断试挂，如果判断为试挂，延迟1秒拆红包（此功能仅为简单判断，还是手动操作靠谱，微笑脸）
         */

        private int versionCode;
        private String apkurl;
        private String updateContent;

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getApkurl() {
            return apkurl;
        }

        public void setApkurl(String apkurl) {
            this.apkurl = apkurl;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }
    }
}
