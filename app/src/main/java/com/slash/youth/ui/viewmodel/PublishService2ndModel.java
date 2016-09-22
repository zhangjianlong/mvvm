package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slash.youth.databinding.Activity2ndPublishServiceBinding;
import com.slash.youth.domain.PublishedServiceBean;
import com.slash.youth.ui.activity.PublishServiceActivity;
import com.slash.youth.ui.adapter.PublishedServiceAdapter;
import com.slash.youth.ui.holder.PublishedServiceHolder;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class PublishService2ndModel extends BaseObservable {
    Activity2ndPublishServiceBinding mActivity2ndPublishServiceBinding;
    ListView lvPublishedService;
    Activity mActivity;

    public PublishService2ndModel(Activity2ndPublishServiceBinding activity2ndPublishServiceBinding, Activity activity) {
        this.mActivity2ndPublishServiceBinding = activity2ndPublishServiceBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {
        lvPublishedService = mActivity2ndPublishServiceBinding.lvActivity2ndPublishService;
        initData();
        initListener();
    }

    private void initData() {
        //模拟的假数据，实际应该从服务端接口获取
        ArrayList<PublishedServiceBean> listPublishedService = new ArrayList<PublishedServiceBean>();
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        listPublishedService.add(new PublishedServiceBean("做一个互联网公司企业宣传策划案", "报价:¥300", "线上咨询", 28));
        listPublishedService.add(new PublishedServiceBean("搭建大数据集群", "报价:¥300", "线上咨询", 100));
        lvPublishedService.setAdapter(new PublishedServiceAdapter(listPublishedService));
    }

    ItemPublishedServiceModel lastItemModel = null;

    private void initListener() {
        lvPublishedService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PublishedServiceHolder.currentChooseItemIndex = position;
                //上次点击的条目变化
                if (lastItemModel != null) {
                    lastItemModel.setChooseBgColor(0xffF8F8F9);
                    lastItemModel.setChooseIconVisibility(View.INVISIBLE);
                }
                //当前点击的条目变化
                PublishedServiceHolder currentTag = (PublishedServiceHolder) view.getTag();
                ItemPublishedServiceModel currentItemModel = currentTag.mItemPublishedServiceModel;
                currentItemModel.setChooseBgColor(0xff31C5E4);
                currentItemModel.setChooseIconVisibility(View.VISIBLE);

                lastItemModel = currentItemModel;
            }
        });
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void publishService(View v) {
        Intent intentPublishServiceActivity = new Intent(CommonUtils.getContext(), PublishServiceActivity.class);
        intentPublishServiceActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishServiceActivity);
        mActivity.finish();
    }

}