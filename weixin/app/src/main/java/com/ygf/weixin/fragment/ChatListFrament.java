/*
*ChartFrament.java
*Created on 2014-8-12 下午3:00 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.ygf.weixin.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.cniao5.jizha.ChatActivity;
import com.cniao5.jizha.R;
import com.cniao5.jizha.adapter.ChatListAdapter;
import com.cniao5.jizha.db.service.ChatMsgService;
import com.cniao5.jizha.model.ChatMsg;
import com.cniao5.jizha.model.ChatMsgEx;
import com.cniao5.jizha.sys.Constant;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ivan on 14-8-12.
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */
public class ChatListFrament extends RoboFragment implements AdapterView.OnItemClickListener {



    public static final String TAG="ChatListFrament";

    @InjectView(R.id.listView)
    private ListView mListView;
    private ChatListAdapter mAdapter;

    private  ChatMsgService mChatMsgService;
    private List<ChatMsgEx> mChatMsgs;

    private ChatBroadcastReceiver receiver;


    private  ChatListener chatListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatMsgService = ChatMsgService.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list,container,false);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChatMsgs = mChatMsgService.findHistoryChatMsg(getActivity());

        mAdapter = new ChatListAdapter(getActivity(),mChatMsgs);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        showTotalUnreadMsgCount(mChatMsgs);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof ChatListener)
            chatListener = (ChatListener) activity;
        else
            throw new ClassCastException(activity.toString()
                    + " must implement ChatListener");


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ChatMsgEx chatMsgEx = mAdapter.getItem(position);



        Intent intent = new Intent(getActivity(),ChatActivity.class);

        intent.putExtra(Constant.FRIEND, chatMsgEx.getContact());
        startActivityForResult(intent, 10000);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadMsg();

    }

    @Override
    public void onStart() {
        super.onStart();

        receiver= new ChatBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_MSG);

        getActivity().registerReceiver(receiver, filter);
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
    }



    class ChatBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {



            if (intent.getAction().equals(Constant.ACTION_MSG)) {
                Serializable obj = intent.getSerializableExtra(Constant.EXTRA_MSG);
                if (obj instanceof ChatMsg) {

//                    ChatMsg chatMsg = (ChatMsg) obj;

                    loadMsg();


                }
            }

        }
    }


    private  void loadMsg(){


        mChatMsgs = mChatMsgService.findHistoryChatMsg(getActivity());

        mAdapter.clear();
        mAdapter.addData(mChatMsgs);

        showTotalUnreadMsgCount(mChatMsgs);
    }

    private  void showTotalUnreadMsgCount(List<ChatMsgEx>  msgs){

        int count =0;
        if(msgs!=null && msgs.size()>0)
        {
            for (ChatMsgEx msg : msgs)
                count+=msg.getUnreadCount();

            chatListener.unReadMsgCount(count);
        }
    }


    public interface ChatListener{
        public void unReadMsgCount(int count);
    }




}
