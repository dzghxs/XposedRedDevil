package com.hxs.xposedreddevil.weight;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hxs.xposedreddevil.R;

public class LoadingDialog extends ProgressDialog{

    private Context mContext;
    // 显示的图片
    private ImageView mImageView;

    /**
     * loading对话框构造方法
     *
     * @param context
     *            上下文
     *            资源
     */
    public LoadingDialog(Context context) {
        super(context, R.style.myProgressDialog);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        initView();
        initData();
    }

    /**
     * 页面初始化
     */
    private void initView() {
        mImageView = (ImageView) findViewById(R.id.load_gifv);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(mContext).load(R.raw.loading)
                    .apply(options).into(mImageView);
    }

}
