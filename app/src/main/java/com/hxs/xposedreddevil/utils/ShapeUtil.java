package com.hxs.xposedreddevil.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by zed on 2018/5/10.
 */
public class ShapeUtil {
    /**
     * 你可以自己把参数设置为动态 这里我懒得弄了 哈哈
     * @return
     */
    public static GradientDrawable getCornerDrawable(){
        int strokeWidth = 0; // 3dp 边框宽度
        int roundRadius = 4; // 8dp 圆角半径
        int strokeColor = Color.parseColor("#e1e1e1");//边框颜色
        int fillColor = Color.parseColor("#e1e1e1");//内部填充颜色
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }
}
