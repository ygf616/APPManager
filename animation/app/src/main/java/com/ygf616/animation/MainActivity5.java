package com.ygf616.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;


public class MainActivity5 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_activity5);

        ImageView imageView = (ImageView) findViewById(R.id.img);
        imageView.bringToFront();// 置顶:不会被其他组件遮挡


        /*使用XML文件实现
        imageView.setBackgroundResource(R.drawable.frame);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();*/

         /*使用Java代码实现*/
        setFrameAnimation(imageView,1,10,"girl_",this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity5, menu);
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

    public void setFrameAnimation(ImageView iv, int start, int end, String preffix,
                                  Context context){
        AnimationDrawable animationDrawable = new AnimationDrawable();//帧动画对象
        iv.setBackgroundDrawable(animationDrawable);//设置组件背景
        String packageName = context.getApplicationContext().getPackageName();//获取当前包名
        for (int i = start; i <= end; i++) {
            //从图片名称反射资源ID
            int id = context.getResources().getIdentifier(preffix+i, "drawable",  packageName);
            Drawable frame = context.getResources().getDrawable(id);//得到动画帧的对象
            animationDrawable.addFrame(frame, 80);//添加帧,设定时间间隔
        }
        animationDrawable.setOneShot(false);// 只运行一次,false-无限循环
        animationDrawable.start();// 开始播放
    }
}
