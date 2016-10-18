package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityPublishDemandSuccessBinding;
import com.slash.youth.domain.AutoRecommendServicePartBean;
import com.slash.youth.ui.adapter.RecommendServicePartAdapter;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandSuccessModel extends BaseObservable {

    ActivityPublishDemandSuccessBinding mActivityPublishDemandSuccessBinding;
    Activity mActivity;

    public PublishDemandSuccessModel(ActivityPublishDemandSuccessBinding activityPublishDemandSuccessBinding, Activity activity) {
        this.mActivityPublishDemandSuccessBinding = activityPublishDemandSuccessBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    ArrayList<AutoRecommendServicePartBean> listRecommendServicePart = new ArrayList<AutoRecommendServicePartBean>();

    private void initData() {
        getRecommendServicePartData();
    }

    private void initView() {
        mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setVerticalScrollBarEnabled(false);
        mActivityPublishDemandSuccessBinding.lvRecommendServicePart.setAdapter(new RecommendServicePartAdapter(listRecommendServicePart));
    }

    public void getRecommendServicePartData() {
        //模拟数据，系统自动推荐的优质服务方，实际应该由服务端接口返回(5到10个)
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
        listRecommendServicePart.add(new AutoRecommendServicePartBean());
    }
}
