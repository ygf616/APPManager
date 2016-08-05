/*
*MoreFragment.java
*Created on 2014-8-12 下午3:02 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.ygf.weixin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.cniao5.jizha.R;
import com.cniao5.jizha.StartActivity;
import com.cniao5.jizha.model.Member;
import com.cniao5.jizha.util.ImageUtil;
import com.cniao5.jizha.util.MemberUtil;
import com.cniao5.jizha.widget.CustomDialog;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by Ivan on 14-8-12.
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */
public class MoreFragment extends RoboFragment implements View.OnClickListener {




    @InjectView(R.id.imageviewHead)
    private ImageView mImgHead;

    @InjectView(R.id.txtName)
    private TextView mTxtName;

    @InjectView(R.id.txtEmail)
    private TextView mTxtEmail;

    @InjectView(R.id.viewSetting)
    private View mViewSetting;

    @InjectView(R.id.viewAboutus)
    private View mViewAboutus;

    @InjectView(R.id.btnLogout)
    private Button mBtnLogout;


    private CustomDialog mDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayProfile();
        mBtnLogout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnLogout){
            showConfirmDialog();
        }
    }



    private void showConfirmDialog(){
        buildConfirmDialog().show();
    }

    private  CustomDialog buildConfirmDialog()
    {
        if (mDialog==null){

            View layoutView =LayoutInflater.from(getActivity()).inflate(R.layout.dialog_info, null);

            mDialog = new CustomDialog(getActivity(),layoutView);

            TextView txtMsg = (TextView) layoutView.findViewById(R.id.txtMsg);
            txtMsg.setText(R.string.logout_confirm);

            Button mBtnCancel = (Button) layoutView.findViewById(R.id.btnCancel);
            Button mBtnConfirm = (Button) layoutView.findViewById(R.id.btnConfirm);

            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

        }
        return mDialog;
    }


    private  void logout(){

        MemberUtil.clearMember(getActivity());

        startActivity(new Intent(getActivity(), StartActivity.class));

        getActivity().finish();
    }

    private  void displayProfile(){


        Member member = MemberUtil.getMember(getActivity());
        if(member!=null){

            mTxtName.setText(member.getName());
            mTxtEmail.setText(member.getEmail());
            ImageUtil.displayImageUseDefOptions(member.getHeadbig(), mImgHead);
        }


    }

}
