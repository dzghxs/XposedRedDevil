# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#*****************************************基本指令区指令--含义*********************************************************#

#代码混淆的压缩比例，值在0-7之间
-optimizationpasses 5

#混淆后类名都为小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

#指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers

 #不优化输入的类文件
-dontoptimize

 #不做预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

#生成原类名和混淆后的类名的映射文件
-printmapping proguardMapping.txt

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护注解
-keepattributes *Annotation*,InnerClasses

#Object对象的层次保护,不混淆泛型
-keepattributes Signature

#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#忽略警告
-ignorewarnings

#****************************************************基础指令区********************************************************#

#-keep class XXXX //保留类名不变，也就是类名不混淆，而类中的成员名不保证。
#-keepclasseswithmembers class XXXX //保留类名和成员名。当然也可以是类中特定方法

#**************************************************默认保留区**********************************************************#

# 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

#如果应用了support的包
-keep class android.support.** {*;}

-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#保护枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保护R文件
-keep class **.R$* {
 *;
}

#自定义类的保护
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    public void get*(...);
}
#保护自定义类中的默认方法
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保护静态方法
-keepclassmembers class **.R$* {
    public static <fields>;
}
#保护本地方法（JNI）
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保护序列化Serializable
-keep public class * implements java.io.Serializable {
        public *;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保护序列化Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * {
    void *(**On*Event);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#---------------------------------webview------------------------------------


#**************************************************默认保留区**********************************************************#

#-keep class 你的实体类所在的包.** { *; }

#***********************************************自定义实体类区*********************************************************#

#保留自定义model类
-keep com.hxs.xposedreddevil.model.**{*;}

#***********************************************自定义实体类区*********************************************************#



#***********************************************第三方包区域***********************************************************#

#Gson
-keep class com.google.gson.** { *;}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

#---------------------------------第三方jar包-------------------------------

#以log4j为例子
#-libraryjars log4j-1.2.17.jar
#-dontwarn org.apache.log4j.**
#-keep class  org.apache.log4j.** { *;}

#-------------------------------------------------------------------------

#***********************************************第三方包区域***********************************************************#


