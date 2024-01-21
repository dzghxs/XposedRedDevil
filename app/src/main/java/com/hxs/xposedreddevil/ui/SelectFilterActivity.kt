package com.hxs.xposedreddevil.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.adapter.FilterAdapter;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.databinding.ActivityNotRootBinding;
import com.hxs.xposedreddevil.databinding.ActivitySelectFilterBinding;
import com.hxs.xposedreddevil.model.FilterBean;
import com.hxs.xposedreddevil.model.FilterSaveBean;
import com.hxs.xposedreddevil.service.GroupChatService;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.weight.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectFilterActivity extends BaseActivity
        implements FilterAdapter.onItemClickListener {

    private ActivitySelectFilterBinding binding;

    LoadingDialog loadingDialog;

    FilterBean filterBean;
    FilterSaveBean bean;
    List<FilterBean> beanList = new ArrayList<>();
    FilterAdapter adapter;

    Gson gson = new Gson();
    JsonParser parser = new JsonParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectFilterBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        DataInit();
    }

    @SuppressLint("RestrictedApi")
    private void DataInit() {
        EventBus.getDefault().register(this);
        loadingDialog = new LoadingDialog(this);
        binding.tvClassName.setText("选择过滤的群聊");
        loadingDialog.show();
        binding.rlSelect.setLayoutManager(new LinearLayoutManager(this));
        binding.rlSelect.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    binding.fabRefresh.setVisibility(View.VISIBLE);
                } else if (dy == 0) {
                    binding.fabRefresh.setVisibility(View.VISIBLE);
                } else {
                    binding.fabRefresh.setVisibility(View.GONE);
                }
            }
        });
        adapter = new FilterAdapter(beanList, this);
        binding.rlSelect.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        if (!sharedPreferences.getString("filter", "").equals("")) {
            filterBean = new FilterBean();
            JsonArray jsonArray = parser.parse(sharedPreferences.getString("filter", "")).getAsJsonArray();
            for (JsonElement user : jsonArray) {
                filterBean = new FilterBean();
                filterBean = gson.fromJson(user, FilterBean.class);
                beanList.add(filterBean);
            }
            if (!sharedPreferences.getString("selectfilter", "").equals("")) {
                List<FilterSaveBean> list = new ArrayList<>();
                jsonArray = parser.parse(sharedPreferences.getString("selectfilter", "")).getAsJsonArray();
                for (JsonElement user : jsonArray) {
                    bean = new FilterSaveBean();
                    //使用GSON，直接转成Bean对象
                    bean = gson.fromJson(user, FilterSaveBean.class);
                    list.add(bean);
                }
                for (int i = 0; i < beanList.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (beanList.get(i).getName().equals(list.get(j).getName())) {
                            beanList.get(i).setCheck(true);
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
            loadingDialog.dismiss();
        } else {
            startService(new Intent(this, GroupChatService.class));
        }
        binding.ivClassBack.setOnClickListener(v -> finish());
        binding.tvClassAdd.setOnClickListener(v -> {
            List<FilterSaveBean> list = new ArrayList<>();
            for (int i = 0; i < beanList.size(); i++) {
                if (beanList.get(i).isCheck()) {
                    bean = new FilterSaveBean();
                    bean.setName(beanList.get(i).getName());
                    bean.setDisplayname(beanList.get(i).getDisplayname());
                    list.add(bean);
                }
            }
            sharedPreferences.edit().putString("selectfilter", gson.toJson(list)).commit();
            Toast.makeText(this, "添加完成", Toast.LENGTH_SHORT).show();
            finish();
        });
        binding.ivClassBack.setOnClickListener(v -> {
            beanList.clear();
//                deleteSingleFile(currApkPath + COPY_WX_DATA_DB);
            startService(new Intent(this, GroupChatService.class));
            loadingDialog.show();
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(Map<String, String> chatRoommap) {
        loadingDialog.dismiss();
        for (Map.Entry<String, String> entry : chatRoommap.entrySet()) {
            filterBean = new FilterBean();
            filterBean.setName(entry.getKey());
            filterBean.setDisplayname(entry.getValue());
            filterBean.setCheck(false);
            beanList.add(filterBean);
        }
        sharedPreferences.edit().putString("filter", gson.toJson(beanList)).commit();
        if (!sharedPreferences.getString("selectfilter", "").equals("")) {
            List<FilterSaveBean> list = new ArrayList<>();
            JsonArray jsonArray = parser.parse(sharedPreferences.getString("selectfilter", "")).getAsJsonArray();
            for (JsonElement user : jsonArray) {
                bean = new FilterSaveBean();
                //使用GSON，直接转成Bean对象
                bean = gson.fromJson(user, FilterSaveBean.class);
                list.add(bean);
            }
            for (int i = 0; i < beanList.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (beanList.get(i).getName().equals(list.get(j).getName())) {
                        beanList.get(i).setCheck(true);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
        stopService(new Intent(this, GroupChatService.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getError(MessageEvent msg) {
        if (msg.getMessage().equals("error")) {
            loadingDialog.dismiss();
            stopService(new Intent(this, GroupChatService.class));
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void itemClickListener(View v, int i) {
        if (beanList.get(i).isCheck()) {
            beanList.get(i).setCheck(false);
        } else {
            beanList.get(i).setCheck(true);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "删除单个文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getApplicationContext(), "删除单个文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
