package com.ygf616.listviewi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015-12-21.
 */
public class MyAdapter extends BaseAdapter {

    /** 数据集合 */
    List<Map<String,Object>> list;
    /** 反射器 */
    LayoutInflater inflater;
    /**
     * 构造器
     * @param context 上下文
     */
    public MyAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }
    /**
     * 传入数据集合
     * @param list
     */
    public void setList(List<Map<String, Object>> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item,null);
            holder = new ViewHolder();
            holder.logo=(ImageView)convertView.findViewById(R.id.logo);
            holder.title=(TextView)convertView.findViewById(R.id.title);
            holder.version=(TextView)convertView.findViewById(R.id.version);
            holder.size=(TextView)convertView.findViewById(R.id.size);
            Button btn=(Button)convertView.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("spl", "点击");
                }
            });
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.logo.setImageResource((Integer) list.get(position).get("logo"));
        holder.title.setText((String) list.get(position).get("title"));
        holder.version.setText((String) list.get(position).get("version"));
        holder.size.setText((String) list.get(position).get("size"));
        return convertView;
    }

    public class ViewHolder{
        ImageView logo;
        TextView title;
        TextView version;
        TextView size;
    }
}
