package com.ygf616.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    /**信息提示对话框 **/
    private static final int DIALOG_0 = 1;
    /**确认对话框 **/
    private static final int DIALOG_1 = 2;
    /**列表对话框 **/
    private static final int DIALOG_2 = 3;
    /**单选对话框 **/
    private static final int DIALOG_3 = 4;
    /**多选对话框 **/
    private static final int DIALOG_4 = 5;
    /**自定义对话框 **/
    private static final int DIALOG_5 = 6;


    //列表对话框数据源
    final String[] mItems_list = {"青鸟云课堂","学士后","启蒙星","Accp","BNet"};

    //单选对话框数据源
    final String[] mItems_radio = {"1星","2星","3星","4星","5星"};
    int mSingleChoiceID = -1;

    //多选对话框数据源
    final String[] mItems_checkbox = {"游泳","读书","足球","逛街","其他"};
    ArrayList<Integer> MultiChoiceID = new ArrayList <Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Button bt_showmessage = (Button) findViewById(R.id.bt_showmessage);
        bt_showmessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDialog(DIALOG_0);
            }
        });

        Button bt_config = (Button) findViewById(R.id.bt_config);
        bt_config.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDialog(DIALOG_1);
            }
        });

        Button bt_list = (Button) findViewById(R.id.bt_list);
        bt_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDialog(DIALOG_2);
            }
        });

        Button bt_radio = (Button) findViewById(R.id.bt_radio);
        bt_radio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDialog(DIALOG_3);
            }
        });

        Button bt_checkbox = (Button) findViewById(R.id.bt_checkbox);
        bt_checkbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDialog(DIALOG_4);
            }
        });

        Button bt_myself = (Button) findViewById(R.id.bt_myself0);
        bt_myself.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createDialog(DIALOG_5);
            }
        });
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

    public void  createDialog(int id){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        switch(id) {
            case DIALOG_0:
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("消息提示");
                builder.setMessage("这是一个最简单的对话框，仅仅提示一些信息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //点击确定后的逻辑
                        showMsg("好的，知道了");
                    }
                });


                break;
            case DIALOG_1:
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("你确定退出吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //点击确定后的逻辑
                        showMsg("马上退出！");
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //点击取消后的逻辑
                        showMsg("稍稍等会！");
                    }
                });
                break;
            case DIALOG_2:
                builder.setTitle("青鸟产品列表");
                builder.setItems(mItems_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //点击后弹出窗口选择了第几项
                        showMsg("你选择的id为" + which + " , " + mItems_list[which]);
                    }
                });
                break;
            case DIALOG_3:
                mSingleChoiceID = -1;
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("大侠，给个5分评价吧");
                builder.setSingleChoiceItems(mItems_radio, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSingleChoiceID = whichButton;
                        showMsg("你选择的id为" + whichButton + " , " + mItems_radio[whichButton]);
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(mSingleChoiceID > 0) {
                            showMsg("你选择的是" + mSingleChoiceID);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                break;
            case DIALOG_4:
                MultiChoiceID.clear();
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("你有哪些爱好？");
                builder.setMultiChoiceItems(mItems_checkbox,
                        new boolean[]{false, false, false, false, false, false, false},
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton,
                                                boolean isChecked) {
                                if(isChecked) {
                                    MultiChoiceID.add(whichButton);
                                    showMsg("你选择的id为" + whichButton + " , " + mItems_checkbox[whichButton]);
                                }else {
                                    MultiChoiceID.remove(whichButton);
                                }

                            }
                        });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String str = "";
                        int size = MultiChoiceID.size();
                        for (int i = 0 ;i < size; i++) {
                            str+= mItems_checkbox[MultiChoiceID.get(i)] + ", ";
                        }
                        showMsg("你选择的是" + str);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                break;
            case DIALOG_5:
                LayoutInflater inflater = LayoutInflater.from(this);
                final View mMyView = inflater.inflate(R.layout.layout_myself_dialog, null);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setView(mMyView);
                mMyView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMsg("自定义的View哦");
                    }
                });
                break;
        }
        builder.create().show();
    }

    /**
     * 消息提示
     * @param message
     */
    private void showMsg(String message) {
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
