package com.ygf.appmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ygf.appmanager.MainActivity;
import com.ygf.appmanager.R;
import com.ygf.appmanager.entity.AppInfo;
import com.ygf.appmanager.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2015-12-22.
 */
public class MyAdapter extends BaseAdapter {

    List<AppInfo> list;
    LayoutInflater inflater;

    /** 卸载接口 */
    IUninstall uninstall;
    /** Setter */
    public void setUninstall(IUninstall uninstall) {
        this.uninstall = uninstall;
    }

    public MyAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<AppInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.logo = (ImageView) convertView.findViewById(R.id.logo);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.version = (TextView) convertView.findViewById(R.id.version);
            holder.size = (TextView) convertView.findViewById(R.id.size);
            holder.btn = (Button) convertView.findViewById(R.id.btn);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AppInfo app = list.get(position);
        holder.logo.setImageDrawable(app.icon);
        // 进行高亮的显示
        if (MainActivity.KEYWORD == null){// 普通状态
            holder.title.setText(app.AppName);
        }else {// 搜索状态
            holder.title.setText(Utils.highLightText(app.AppName, MainActivity.KEYWORD));
        }
        holder.version.setText("版本："+app.versionName);
        holder.size.setText("大小："+app.size+"M");

        final int pos = position;
        final String packageName = app.packageName;
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //卸载
                uninstall.btnOnClick(pos,packageName);

            }
        });

        return convertView;
    }

    public class ViewHolder{
        ImageView logo;
        TextView title;
        TextView version;
        TextView size;
        Button btn;
    }
}
