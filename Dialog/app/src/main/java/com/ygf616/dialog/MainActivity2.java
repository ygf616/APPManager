package com.ygf616.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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


    Dialog dialog;// 声明对话框
    PopupWindow popup;//声明PopupWindow

    public void btnClick(View v){

        LayoutInflater inflater = LayoutInflater.from(this);
        final View mMyView = inflater.inflate(R.layout.layout_myself_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(mMyView);
        mMyView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }


    public void btn4dateClick(View v){
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker dp, int year,
                                          int month, int dayOfMonth) {
                        String text = "您选择了：" + year + "年" + (month + 1)
                                + "月" + dayOfMonth + "日";
                        Toast.makeText(MainActivity2.this, text,
                                Toast.LENGTH_LONG).show();
                    }
                }
                //设置初始日期
                , c.get(Calendar.YEAR)
                , c.get(Calendar.MONTH)
                , c.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void btn4timeClick(View v){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String text = "您选择了：" + hourOfDay + "时" + minute
                                + "分";
                        Toast.makeText(MainActivity2.this, text,
                                Toast.LENGTH_LONG).show();
                    }
                }
                ,c.get(Calendar.HOUR_OF_DAY)
                ,c.get(Calendar.MINUTE)
                ,true).show();
    }

    public void btn4popupClick(View v){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View mMyView = inflater.inflate(R.layout.layout_myself_dialog, null);
        final PopupWindow popup;
        popup = new PopupWindow(mMyView,600,450);
        popup.showAtLocation(findViewById(R.id.btn4popup), Gravity.CENTER,0,0);
        mMyView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    public void btn4activityClick(View v){
        Intent intent = new Intent(this,DialogActivity.class);
        startActivity(intent);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                pd.dismiss();
            }else if (msg.what == 1){
                pd.setProgress(msg.arg1);
            }
        }
    };
    ProgressDialog pd;
    public void btn4processClick(View v){
        showProgress();
        new Thread(){
            @Override
            public void run(){
                for(int i=1;i<=10;i++){
                    try {
                        Message msg = Message.obtain();
                        msg.arg1 = i;
                        msg.what = 1;
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    public void showProgress(){
        pd = new ProgressDialog(this);
        pd.setTitle("任务执行中");
        pd.setMessage("任务执行中,请稍候...");
        pd.setCancelable(true);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMax(10);
        pd.setIndeterminate(false);
        pd.show();
    }

    private long mExitTime;
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出",
                    Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
