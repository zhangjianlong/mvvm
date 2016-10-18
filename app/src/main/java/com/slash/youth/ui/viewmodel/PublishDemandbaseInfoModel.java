package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandBaseinfoBinding;
import com.slash.youth.ui.activity.PublishDemandAddInfoActivity;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.CommonUtils;

/**
 * 发布需求页面 第二版
 * Created by zhouyifeng on 2016/10/17.
 */
public class PublishDemandBaseInfoModel extends BaseObservable {
    public static final int PUBLISH_ANONYMITY_ANONYMOUS = 0;//匿名发布
    public static final int PUBLISH_ANONYMITY_REALNAME = 1;//实名发布

    ActivityPublishDemandBaseinfoBinding mActivityPublishDemandBaseinfoBinding;
    Activity mActivity;
    public SlashAddPicLayout mSaplAddPic;
    public SlashDateTimePicker mChooseDateTimePicker;

    int anonymity = PUBLISH_ANONYMITY_REALNAME;//是否匿名发布，默认为实名发布
    String demandTitle = "";
    String demandDesc = "";
    long startTime = 0;
    private int mCurrentChooseMonth;
    private int mCurrentChooseDay;
    private int mCurrentChooseHour;
    private int mCurrentChooseMinute;


    public PublishDemandBaseInfoModel(ActivityPublishDemandBaseinfoBinding activityPublishDemandBaseinfoBinding, Activity activity) {
        this.mActivityPublishDemandBaseinfoBinding = activityPublishDemandBaseinfoBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        mChooseDateTimePicker = mActivityPublishDemandBaseinfoBinding.sdtpPublishDemandChooseDatetime;
        mSaplAddPic = mActivityPublishDemandBaseinfoBinding.saplPublishDemandAddpic;
        mSaplAddPic.setActivity(mActivity);
        mSaplAddPic.initPic();
    }

    //选择实名发布
    public void checkRealName(View v) {
        mActivityPublishDemandBaseinfoBinding.ivPublicDemandRealnameIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishDemandBaseinfoBinding.ivPublishDemandAnonymousIcon.setImageResource(R.mipmap.default_btn);
        anonymity = PUBLISH_ANONYMITY_REALNAME;
    }

    //选择匿名发布
    public void checkAnonymous(View v) {
        mActivityPublishDemandBaseinfoBinding.ivPublicDemandRealnameIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishDemandBaseinfoBinding.ivPublishDemandAnonymousIcon.setImageResource(R.mipmap.pitchon_btn);
        anonymity = PUBLISH_ANONYMITY_ANONYMOUS;
    }

    public void setStartTime(View v) {
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    public void cancelChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
    }

    public void okChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
        mCurrentChooseMonth = mChooseDateTimePicker.getCurrentChooseMonth();
        mCurrentChooseDay = mChooseDateTimePicker.getCurrentChooseDay();
        mCurrentChooseHour = mChooseDateTimePicker.getCurrentChooseHour();
        mCurrentChooseMinute = mChooseDateTimePicker.getCurrentChooseMinute();
        String dateTimeStr = mCurrentChooseMonth + "月" + mCurrentChooseDay + "日" + "-" + mCurrentChooseHour + ":" + (mCurrentChooseMinute < 10 ? "0" + mCurrentChooseMinute : mCurrentChooseMinute);
        setStartTimeStr(dateTimeStr);
    }


    //下一步操作
    public void nextStep(View v) {
        Intent intentPublishDemandAddInfoActivity = new Intent(CommonUtils.getContext(), PublishDemandAddInfoActivity.class);

        //设置发布需求的相关信息
        Bundle publishDemandData = new Bundle();
        publishDemandData.putInt("anonymity", anonymity);
        demandTitle = mActivityPublishDemandBaseinfoBinding.etPublishDemandTitle.getText().toString();
        publishDemandData.putString("demandTitle", demandTitle);
        demandDesc = mActivityPublishDemandBaseinfoBinding.etPublishDemandDesc.toString();
        publishDemandData.putString("demandDesc", demandDesc);
        publishDemandData.putLong("startTime", startTime);
//        mSaplAddPic.getAddedPicTempPath()
        intentPublishDemandAddInfoActivity.putExtras(publishDemandData);

        intentPublishDemandAddInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishDemandAddInfoActivity);
    }

    //返回操作
    public void gotoBack(View v) {
        mActivity.finish();
    }


    private int chooseDateTimeLayerVisibility = View.GONE;
    private String startTimeStr;

    @Bindable
    public int getChooseDateTimeLayerVisibility() {
        return chooseDateTimeLayerVisibility;
    }

    public void setChooseDateTimeLayerVisibility(int chooseDateTimeLayerVisibility) {
        this.chooseDateTimeLayerVisibility = chooseDateTimeLayerVisibility;
        notifyPropertyChanged(BR.chooseDateTimeLayerVisibility);
    }

    @Bindable
    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
        notifyPropertyChanged(BR.startTimeStr);
    }
}
