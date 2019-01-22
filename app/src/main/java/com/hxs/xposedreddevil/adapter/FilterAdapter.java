package com.hxs.xposedreddevil.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.model.FilterBean;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    List<FilterBean> list;
    Context context;

    private onItemClickListener itemClickListener;

    public interface onItemClickListener{
        void itemClickListener(View v,int i);
    }

    public void setOnItemClickListener(onItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public FilterAdapter(List<FilterBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.filter_recycle_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.tv_filter_name.setText(list.get(i).getDisplayname());
        if (list.get(i).isCheck()) {
            holder.cb_filter_select.setChecked(true);
        } else {
            holder.cb_filter_select.setChecked(false);
        }
        holder.v_filter_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClickListener(v,i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_filter_name;
        CheckBox cb_filter_select;
        View v_filter_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_filter_name = itemView.findViewById(R.id.tv_filter_name);
            cb_filter_select = itemView.findViewById(R.id.cb_filter_select);
            v_filter_select = itemView.findViewById(R.id.v_filter_select);
        }
    }
}
