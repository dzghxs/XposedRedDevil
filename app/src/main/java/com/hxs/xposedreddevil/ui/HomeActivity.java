package com.hxs.xposedreddevil.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.control.InitConfig;
import com.hxs.xposedreddevil.control.MySyntherizer;
import com.hxs.xposedreddevil.control.NonBlockSyntherizer;
import com.hxs.xposedreddevil.greendao.DbCarryList;
import com.hxs.xposedreddevil.listener.UiMessageListener;
import com.hxs.xposedreddevil.utils.AssetsCopyTOSDcard;
import com.hxs.xposedreddevil.utils.AutoCheck;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.OfflineResource;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;
import com.hxs.xposedreddevil.utils.SQLiteUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.carrypagenum;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.carrypagetime;
import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;
import static com.hxs.xposedreddevil.utils.Constant.currApkPath;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.iv_home_setting)
    ImageView ivHomeSetting;
    @BindView(R.id.sp_center_version)
    Spinner spCenterVersion;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.home_card_unroot)
    CardView homeCardUnroot;
    @BindView(R.id.home_card_root)
    CardView homeCardRoot;
    @BindView(R.id.tv_home_unroot)
    TextView tvHomeUnroot;
    @BindView(R.id.tv_home_root)
    TextView tvHomeRoot;

    Gson gson = new Gson();

    protected String appId = "16875226";

    protected String appKey = "dCOyb2SHnn5vMId2S02c6nDQ";

    protected String secretKey = "KUnkU4dSvvIT32AT3GUaOdV7KRPAKFpR";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.MIX;

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
    protected String offlineVoice = OfflineResource.VOICE_MALE;

    // 主控制类，所有合成控制方法从这个类开始
    public MySyntherizer synthesizer;

    DbCarryList carryList = new DbCarryList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        PropertiesUtils.putValue(RED_FILE, "widthPixels", widthPixels + "");
        CheckPermissionInit();
        try {
            SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);
            Map<String, String> params = getParams();
            InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
            AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 100) {
                        AutoCheck autoCheck = (AutoCheck) msg.obj;
                        synchronized (autoCheck) {
                            String message = autoCheck.obtainDebugMessage();
                            toPrint(message); // 可以用下面一行替代，在logcat中查看代码
                            // Log.w("AutoCheckMessage", message);
                        }
                    }
                }

            });
            synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataInit();
    }

    @Override
    protected void onDestroy() {
        synthesizer.release();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void DataInit() {
        File file = new File(currApkPath + "ListTime");
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
        tvHomeUnroot.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        tvHomeRoot.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        AssetsCopyTOSDcard.Assets2Sd(this, "lucky_sound.mp3", Environment.getExternalStorageDirectory().toString() + "/xposedreddevil/lucky_sound.mp3");
        if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.14")) {
            spCenterVersion.setSelection(1);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.13")) {
            spCenterVersion.setSelection(0);
        }
        if (spCenterVersion.getSelectedItem().equals("7.0.14")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "7.0.14");
        } else if (spCenterVersion.getSelectedItem().equals("7.0.13")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "7.0.13");
        }
        if (!PackageManagerUtil.getItems(this).equals("")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", PackageManagerUtil.getItems(this));
        } else {
            WriteVersion();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MessageEvent msg) {
        if (msg.getMessage().contains("@")) {
            Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
            speak(msg.getMessage());
        }
    }

    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    private void speak(String str) {
        // 需要合成的文本text的长度不能超过1024个GBK字节。
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // synthesizer.setParams(params);
        int result = synthesizer.speak(str);
        checkResult(result, "speak");
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            toPrint("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    protected void toPrint(String str) {
        Message msg = Message.obtain();
        msg.obj = str;
        mainHandler.sendMessage(msg);
    }

    /**
     * 验证权限
     */
    public void CheckPermissionInit() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .permission(Permission.READ_PHONE_STATE)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        Toast.makeText(HomeActivity.this, "必要权限未提供，app即将关闭", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
    }

    private void WriteVersion() {
        new AlertDialog.Builder(this).setTitle("获取微信版本失败，请手动选择微信版本")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    @OnClick({R.id.home_card_unroot, R.id.home_card_root, R.id.iv_home_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.home_card_unroot:
                startActivity(new Intent(this, NotRootActivity.class));
                break;
            case R.id.home_card_root:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
