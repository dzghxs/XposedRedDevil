package com.hxs.xposedreddevil;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.hxs.xposedreddevil.model.MsgsBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class MainActivity extends AppCompatActivity {

    Gson gson = new Gson();
    MsgsBean bean = new MsgsBean();

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
