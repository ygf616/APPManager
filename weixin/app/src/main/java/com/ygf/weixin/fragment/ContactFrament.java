/*
*ContactFrament.java
*Created on 2014-9-23 下午4:28 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.ygf.weixin.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.trinea.android.common.util.PreferencesUtils;
import com.cniao5.jizha.ProfileActivity;
import com.cniao5.jizha.R;
import com.cniao5.jizha.RequestMsgActivity;
import com.cniao5.jizha.adapter.ContactAdapter;
import com.cniao5.jizha.db.service.ContactService;
import com.cniao5.jizha.db.service.RequestMsgService;
import com.cniao5.jizha.model.Contact;
import com.cniao5.jizha.sys.Constant;
import com.cniao5.jizha.widget.SideBar;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import java.util.List;

/**
 * Created by Ivan on 14-9-23.
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */


public class ContactFrament extends RoboFragment implements AdapterView.OnItemClickListener {


    public static final String TAG="ContactFrament";

    private RequestMsgService mRequestService = RequestMsgService.getInstance();

    @InjectView(R.id.listviewContact)
    private StickyListHeadersListView mListView;

    @InjectView(R.id.progressBar)
    private ProgressBar mProgressBar;

    @InjectView(R.id.sideBar)
    private SideBar mSideBar;

    private ContactAdapter mAdapter;
    private ContactService contactService;
    private List<Contact> mContact;
    private TextView mTxtBadge;


    private ContactBroadcastReceiver receiver;

    private LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactService = ContactService.getInstance();
        mInflater = LayoutInflater.from(getActivity());

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_contact,container,false);
        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView.setOnItemClickListener(this);

        handler.post(new LoadContactRunnable());
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {

              int position=  mAdapter.getPositionForSection(s.charAt(0));

                mListView.setSelection(position);
            }
        });

        initHeadView();
        initBadge();

    }


    private void initHeadView(){

        View headView = mInflater.inflate(R.layout.template_contact_head,null);
        View newFriendView = headView.findViewById(R.id.layoutNewFriend);

        newFriendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestMsgActivity.class);
                startActivityForResult(intent,Constant.REQUEST_CODE);
            }
        });

        mTxtBadge = (TextView) headView.findViewById(R.id.txtBadge);

       mListView.addHeaderView(headView,null,false);
    }


    private void initBadge(){

        int addFriendRequestCount = mRequestService.countUnHandleMsg(getActivity());
        if(addFriendRequestCount>0){
            mTxtBadge.setVisibility(View.VISIBLE);
            mTxtBadge.setText(addFriendRequestCount+"");
        }

        else
            mTxtBadge.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        reloadContacts();
        initBadge();


    }


    private void reloadContacts(){

        mContact= contactService.findMemberContacts(getActivity());
        mAdapter.clear();
        mAdapter.addData(mContact);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact friend =(Contact)parent.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(Constant.FRIEND,friend);
        intent.putExtra("from",1);

        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==Constant.SUCCESS){
                mProgressBar.setVisibility(View.GONE);

                mAdapter = new ContactAdapter(getActivity(),mContact);
                mListView.setAdapter(mAdapter);
            }
        }
    };

    class LoadContactRunnable implements Runnable
    {

        @Override
        public void run() {

            boolean isInitFinish = PreferencesUtils.getBoolean(getActivity(), Constant.IS_DATA_INIT+ PreferencesUtils.getLong(getActivity(),Constant.MEMBER_ID), false);
            if (isInitFinish){
                mContact = contactService.findMemberContacts(getActivity());
                handler.sendEmptyMessage(Constant.SUCCESS);
            }
            else
                handler.postDelayed(new LoadContactRunnable(),2000);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        receiver= new ContactBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_CONTACT_UPDATE);
        filter.addAction(Constant.ACTION_MAKE_FRIEND_REQUEST);
        this.getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.getActivity().unregisterReceiver(receiver);
    }



    class ContactBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constant.ACTION_CONTACT_UPDATE)){
                reloadContacts();
            }
            else if(intent.getAction().equals(Constant.ACTION_MAKE_FRIEND_REQUEST)){
                initBadge();
            }

        }
    }

}
