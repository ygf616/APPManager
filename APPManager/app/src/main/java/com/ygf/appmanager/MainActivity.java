package com.ygf.appmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ygf.appmanager.adapter.IUninstall;
import com.ygf.appmanager.adapter.MyAdapter;
import com.ygf.appmanager.entity.AppInfo;
import com.ygf.appmanager.util.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener,IUninstall,SearchView.OnQueryTextListener{

    ListView lv;
    List<AppInfo> list;
    MyAdapter adapter;

    public static final int SORT_NAME = 0;
    public static final int SORT_DATE = 1;
    public static final int SORT_SIZE = 2;
    public static final String[] arr_sort = {"按名称","按日期","按大小"};

    int currSort = SORT_NAME;
    Comparator<AppInfo> currComparator = null;

    TextView tv_sort;
    TextView tv_size;

    //日期比较器 (倒序)
    Comparator<AppInfo> dateComparator = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo lhs, AppInfo rhs) {

            if(lhs.lastUpdateTime > rhs.lastUpdateTime){
                return -1;
            }else if(lhs.lastUpdateTime == rhs.lastUpdateTime){
                return 0;
            }else{
                return 1;
            }

        }
    };
    //大小比较器 (倒序)
    Comparator<AppInfo> sizeComparator = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo lhs, AppInfo rhs) {

            if(lhs.byteSize > rhs.byteSize){
                return -1;
            }else if(lhs.byteSize == rhs.byteSize){
                return 0;
            }else{
                return 1;
            }

        }
    };
    //名称比较器 (正序)
    Comparator<AppInfo> nameComparator = new Comparator<AppInfo>() {
        @Override
        public int compare(AppInfo lhs, AppInfo rhs) {

            return lhs.AppName.toLowerCase().compareTo(rhs.AppName.toLowerCase());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tv_sort = (TextView) findViewById(R.id.tv_sort);
        tv_size = (TextView) findViewById(R.id.tv_size);
        //拿到ListView
        lv = (ListView) findViewById(R.id.lv_main);
        //数据源
        list = Utils.getAppInfos(this);
        //适配器
        adapter = new MyAdapter(this);
        adapter.setList(list);
        //关联
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        adapter.setUninstall(this);
        updateData();

    }

    private void update_top(){
        tv_sort.setText("排序:"+arr_sort[currSort]);
        tv_size.setText("应用数:"+list.size());
    }

    SearchView sv;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                updateData();
                return true;
            }
        });
        sv = (SearchView) search.getActionView();
        sv.setSubmitButtonEnabled(true);
        sv.setQueryHint("查询应用名");
        sv.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        // "刷新的点击事件"
        if (id == R.id.btn_refresh) {
            updateData();
            return true;
        }
        if (id == R.id.sort_name) {
            currSort = SORT_NAME;
        }
        if (id == R.id.sort_date) {
            currSort = SORT_DATE;
        }
        if (id == R.id.sort_size) {
            currSort = SORT_SIZE;
        }
        //更新应用列表
        updateDate_sort(currSort);
        return true;
    }

    private void updateDate_sort(int sort){
        if (sort == SORT_NAME) {
            currComparator = nameComparator;
        }
        if (sort == SORT_DATE) {
            currComparator = dateComparator;
        }
        if (sort == SORT_SIZE) {
            currComparator = sizeComparator;
        }
        Collections.sort(list,currComparator);//排序
        adapter.setList(list);
        adapter.notifyDataSetChanged();
        update_top();
    }

    ProgressDialog pd;
    public void showProgressDialog(){
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("刷新列表");
        pd.setMessage("请耐心等待");
        pd.show();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                updateDate_sort(currSort);
                pd.dismiss();// 关闭进度框
            }
        }
    };

    private void updateData(){
        new Thread(){
            @Override
            public void run() {
                list = Utils.getAppInfos(MainActivity.this);
                KEYWORD = null;// 将KEYWORD清空
                handler.sendEmptyMessage(1);
            }
        }.start();// 子线程
        showProgressDialog();// UI线程
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 获取本行的应用信息对象
        AppInfo app = (AppInfo) parent.getItemAtPosition(position);
        // 运行应用
        Utils.openPackage(this, app.packageName);
    }

    public static final int CODE_UNINSTALL = 0;

    @Override
    public void btnOnClick(int pos, String packageName) {
        Utils.uninstallApk(this,packageName,CODE_UNINSTALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //接收卸载应用界面的返回结果,并刷新列表
        if (resultCode == CODE_UNINSTALL){
            updateData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String KEYWORD; // 全局变量:搜索关键字
    @Override
    public boolean onQueryTextSubmit(String query) {
        // 提交搜索关键字
        list = Utils.getSearchResult(list,query);
        KEYWORD = query;
        updateDate_sort(currSort);//显示搜索结果

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("确定退出吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }
                )
                .setNegativeButton("取消",null)
                .show();

    }
}
