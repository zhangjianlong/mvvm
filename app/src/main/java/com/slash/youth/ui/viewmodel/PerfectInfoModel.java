package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityPerfectInfoBinding;
import com.slash.youth.ui.activity.HomeActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class PerfectInfoModel extends BaseObservable {

    ActivityPerfectInfoBinding mActivityPerfectInfoBinding;

    public PerfectInfoModel(ActivityPerfectInfoBinding activityPerfectInfoBinding) {
        this.mActivityPerfectInfoBinding = activityPerfectInfoBinding;
        initView();
    }

    private void initView() {

    }

    public void okPerfectInfo(View v){
        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
        intentHomeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentHomeActivity);
    }
}