package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ItemDemandFlowLogBinding;
import com.slash.youth.domain.DemandFlowLogList;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2016/11/13.
 */
public class ItemDemandLogModel extends BaseObservable {

    ItemDemandFlowLogBinding mItemDemandFlowLogBinding;
    Activity mActivity;
    DemandFlowLogList.LogInfo mLogInfo;

    public ItemDemandLogModel(ItemDemandFlowLogBinding itemDemandFlowLogBinding, Activity activity, DemandFlowLogList.LogInfo logInfo) {
        this.mActivity = activity;
        this.mItemDemandFlowLogBinding = itemDemandFlowLogBinding;
        this.mLogInfo = logInfo;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd HH:mm");
        String timeStr = sdf.format(mLogInfo.cts);
        mItemDemandFlowLogBinding.tvLogRecordTime.setText(timeStr);

        mItemDemandFlowLogBinding.tvLogAction.setText(mLogInfo.action);
    }
}