package com.ygf616.listviewi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. 获取ListView对象
        ListView listView=(ListView)findViewById(R.id.lv_main);

        //2. 准备数据源
        String[] data = {
                "初识Android",
                "开发环境搭建",
                "基础控件I",
                "基础控件II",
                "线性布局",
                "相对布局"};

        //3. 准备适配器Adapter
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
            this, //context 上下文
            android.R.layout.simple_list_item_1, //行布局:系统自带的布局
            data// 数据源
        );

        //4. 将适配器关联到ListView
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
