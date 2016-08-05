package com.ygf616.listviewi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2015-12-21.
 */
public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        // 初始化控件
        String index = getIntent().getStringExtra("index");
        String title = getIntent().getStringExtra("title");

        TextView info = (TextView) findViewById(R.id.info);
        info.setText("编号:"+index + " "+ title );
    }
}
