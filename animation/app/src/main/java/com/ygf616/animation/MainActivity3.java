package com.ygf616.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


public class MainActivity3 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_activity3);
        /*
        ImageView imageView = (ImageView) findViewById(R.id.img);
        Animation animation;


       /*使用XML文件实现
       animation = AnimationUtils.loadAnimation(this, R.anim.trans);*/

        /*使用java代码实现实现
        animation = new TranslateAnimation(1080.0f, 0.0f, 0.0f, 0.0f);
        animation.setDuration(2000);
        animation.setRepeatCount(1);

        imageView.clearAnimation();
        imageView.startAnimation(animation);*/


        Intent intent =new Intent(this,MainActivity4.class);
        startActivity(intent);
        overridePendingTransition(R.anim.trans,R.anim.trans_out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
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
