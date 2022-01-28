package com.hxs.xposedreddevil.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.adapter.CarryListAdapter;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.greendao.DbCarryList;
import com.hxs.xposedreddevil.utils.SQLiteUtils;
import com.lzp.floatingactionbuttonplus.FabTagLayout;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarryListActivity extends BaseActivity implements FloatingActionButtonPlus.OnItemClickListener, OnRefreshLoadMoreListener {

    @BindView(R.id.rl_carry)
    RecyclerView rlCarry;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.FabPlus)
    FloatingActionButtonPlus FabPlus;

    List<DbCarryList> list = new ArrayList<>();
    CarryListAdapter adapter;

    int page = 0, year, month, day;
    DatePickerDialog datePickerDialog;

    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carry_list);
        ButterKnife.bind(this);
        dialog = new AlertDialog.Builder(CarryListActivity.this);
        DataInit();
        FabPlus.setOnItemClickListener(this);
        refresh.setOnRefreshLoadMoreListener(this);
    }

    private void DataInit() {
        rlCarry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarryListAdapter(this, list);
        rlCarry.setAdapter(adapter);
        list.addAll(SQLiteUtils.getInstance().selectPageContacts(0));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(FabTagLayout tagView, int position) {
        switch (position) {
            case 0:
                DateInit();
                break;
            case 1:
                View v = LayoutInflater.from(CarryListActivity.this).inflate(R.layout.carry_name, null);
                final EditText et = v.findViewById(R.id.et_name);
                Button btn = v.findViewById(R.id.btn_name);
                dialog.setView(v);
                final AlertDialog al = dialog.create();
                al.show();
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
                android.view.WindowManager.LayoutParams p = al.getWindow().getAttributes();  //获取对话框当前的参数值
                p.height = (int) (d.getHeight() * 0.2);   //高度设置为屏幕的0.3
                p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
                al.getWindow().setAttributes(p);     //设置生效
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.clear();
                        list.addAll(SQLiteUtils.getInstance().queryNameEq(et.getText().toString()));
                        adapter.notifyDataSetChanged();
                        al.dismiss();
                    }
                });
                break;
            case 2:
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    sb.append("Id:" + list.get(i).getId() + "------->名字:" + list.get(i).getName() +
                            "------->金额:" + list.get(i).getMoney() + "------->时间:" + list.get(i).getTime() +
                            "------->方式:" + list.get(i).getStatus() + "\n");
                }
                PropertiesUtils.putListValue(sb.toString());
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        list.addAll(SQLiteUtils.getInstance().selectPageContacts(++page));
        adapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        }, 1500);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        list.clear();
        page = 0;
        list.addAll(SQLiteUtils.getInstance().selectPageContacts(page));
        adapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
        }, 1500);
    }

    /**
     * 时间选择控件
     */
    public void DateInit() {
        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(this,
                R.style.MyDatePickerDialogTheme,
//                DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int years, int months, int dayOfMonths) {
                        year = years;
                        month = months + 1;
                        day = dayOfMonths;
                        list.clear();
                        if (month > 9) {
                            list.addAll(SQLiteUtils.getInstance().queryTimeEq(year + "-" + month + "-" + day));
                        } else {
                            list.addAll(SQLiteUtils.getInstance().queryTimeEq(year + "-0" + month + "-" + day));
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                year, month - 1, day);
        //设置起始日期和结束日期
        datePickerDialog.show();
    }

}
