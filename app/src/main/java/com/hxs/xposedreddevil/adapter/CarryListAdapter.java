package com.hxs.xposedreddevil.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.greendao.DbCarryList;

import java.util.List;

public class CarryListAdapter extends RecyclerView.Adapter<CarryListAdapter.ViewHolder> {

    Context context;
    List<DbCarryList> list;

    public CarryListAdapter(Context context, List<DbCarryList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.carry_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tv_id.setText(list.get(i).getId() + "");
        holder.tv_name.setText(list.get(i).getName());
        holder.tv_time.setText(list.get(i).getTime());
        holder.tv_money.setText(list.get(i).getMoney());
        holder.tv_status.setText(list.get(i).getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id, tv_name, tv_money, tv_time, tv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
