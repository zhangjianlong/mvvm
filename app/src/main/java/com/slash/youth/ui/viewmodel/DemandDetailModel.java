package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityDemandDetailBinding;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailLocationActivity;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishDemandSuccessActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2016/10/24.
 */
public class DemandDetailModel extends BaseObservable {

//    public static final int VISUAL_ANGLE_SERVICE = 10000;//服务者视角
//    public static final int VISUAL_ANGLE_DAMAND = 10001;//需求者视角

    ActivityDemandDetailBinding mActivityDemandDetailBinding;
    Activity mActivity;
    //    private int current_visual_angle;//当前视角
//    private boolean isDemandOffShelf;//需求是否下架
//    private LatLng demandLatLng;//发布需求时获取的经纬度，由服务端返回
    long demandId;

    public DemandDetailModel(ActivityDemandDetailBinding activityDemandDetailBinding, Activity activity) {
        this.mActivityDemandDetailBinding = activityDemandDetailBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        demandId = mActivity.getIntent().getLongExtra("demandId", -1);
//        ToastUtils.shortToast("demandId:" + demandId);
        getDemandDetailDataFromServer();

//        current_visual_angle = VISUAL_ANGLE_DAMAND;//获取当前者视角，应该根据服务端数据来判断，如果是自己发的需求，就是需求者视角，如果不是自己发的，就是服务者视角
//        isDemandOffShelf = false;//由服务端数据获取当前浏览的需求是否已经下架
//        demandLatLng = new LatLng(31.317866, 120.71596);//模拟经纬度，实际由服务端返回
    }


    private void initView() {
        mActivityDemandDetailBinding.svDemandDetailContent.setVerticalScrollBarEnabled(false);

    }

    private void displayTags(String tag1, String tag2, String tag3) {
        //加载第一个tag
        if (TextUtils.isEmpty(tag1)) {
            mActivityDemandDetailBinding.tvDemandDetailTag1.setVisibility(View.GONE);
        } else {
            mActivityDemandDetailBinding.tvDemandDetailTag1.setVisibility(View.VISIBLE);
            String[] tagInfo = tag1.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag1;
            }
            mActivityDemandDetailBinding.tvDemandDetailTag1.setText(tagName);
        }
        //加载第二个tag
        if (TextUtils.isEmpty(tag2)) {
            mActivityDemandDetailBinding.tvDemandDetailTag2.setVisibility(View.GONE);
        } else {
            mActivityDemandDetailBinding.tvDemandDetailTag2.setVisibility(View.VISIBLE);
            String[] tagInfo = tag2.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag2;
            }
            mActivityDemandDetailBinding.tvDemandDetailTag2.setText(tagName);
        }//加载第三个tag
        if (TextUtils.isEmpty(tag3)) {
            mActivityDemandDetailBinding.tvDemandDetailTag3.setVisibility(View.GONE);
        } else {
            mActivityDemandDetailBinding.tvDemandDetailTag3.setVisibility(View.VISIBLE);
            String[] tagInfo = tag3.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag3;
            }
            mActivityDemandDetailBinding.tvDemandDetailTag3.setText(tagName);
        }
    }

    public void displayServicePic(String pic1FileId, String pic2FileId, String pic3FileId, String pic4FileId, String pic5FileId, String pic6FileId) {
        if (!TextUtils.isEmpty(pic1FileId)) {
            //加载第1张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox1.setVisibility(View.VISIBLE);
            LogKit.v("pic1FileId:" + pic1FileId);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic1, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic1FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox1.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic2FileId)) {
            //加载第2张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox2.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic2, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic2FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox2.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic3FileId)) {
            //加载第3张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox3.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic3, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic3FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox3.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic4FileId)) {
            //加载第4张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox4.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic4, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic4FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox4.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic5FileId)) {
            //加载第5张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox5.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic5, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic5FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox5.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(pic6FileId)) {//这种情况应该不存在，因为最多只能上传5张
            //加载第6张图片
            mActivityDemandDetailBinding.flDemandDetailPicbox6.setVisibility(View.VISIBLE);
            BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandDetailPic6, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic6FileId);
        } else {
            mActivityDemandDetailBinding.flDemandDetailPicbox6.setVisibility(View.INVISIBLE);
        }

    }

    DemandDetailBean demandDetailBean;

    /**
     * 获取需求详情信息
     */
    private void getDemandDetailDataFromServer() {
        DemandEngine.getDemandDetail(new BaseProtocol.IResultExecutor<DemandDetailBean>() {
            @Override
            public void execute(DemandDetailBean dataBean) {
                demandDetailBean = dataBean;
                DemandDetailBean.Demand demand = dataBean.data.demand;
                if (LoginManager.currentLoginUserId == demand.uid) {//需求者视角
                    setTopShareBtnVisibility(View.GONE);
                    setTopDemandBtnVisibility(View.VISIBLE);
                    setBottomBtnServiceVisibility(View.GONE);
                    setBottomBtnDemandVisibility(View.VISIBLE);
                } else {//服务者视角
                    setTopShareBtnVisibility(View.VISIBLE);
                    setTopDemandBtnVisibility(View.GONE);
                    setBottomBtnServiceVisibility(View.VISIBLE);
                    setBottomBtnDemandVisibility(View.GONE);
                }
                setDemandTitle(demand.title);
                SimpleDateFormat sdf = new SimpleDateFormat("开始:MM月dd日 hh:mm");
                String starttimeStr = sdf.format(demand.starttime);
                setDemandStartTime(starttimeStr);
                setQuote("¥" + demand.quote + "元");
                //浏览量暂时无法获取,接口中好像没有浏览量字段
                if (demand.pattern == 1) {//线下
                    setOfflineItemVisibility(View.VISIBLE);
                    setDemandPlace("约定地点" + demand.place);
                } else if (demand.pattern == 0) {//线上
                    setOfflineItemVisibility(View.GONE);
                }
                if (demand.instalment == 0) {//不开启
                    setInstalmentItemVisibility(View.GONE);
                } else {//开启分期
                    setInstalmentItemVisibility(View.VISIBLE);
                }
                //技能标签
                String[] tags = demand.tag.split(",");
                if (tags.length == 0) {//这种情况应该不存在
                    displayTags("", "", "");
                } else if (tags.length == 1) {
                    displayTags(tags[0], "", "");
                } else if (tags.length == 2) {
                    displayTags(tags[0], tags[1], "");
                } else if (tags.length == 3) {
                    displayTags(tags[0], tags[1], tags[2]);
                } else {//这种情况应该不存在
                    displayTags(tags[0], tags[1], tags[2]);
                }
                //发布时间
                SimpleDateFormat sdfPublishTime = new SimpleDateFormat("发布时间:MM月dd日 hh:mm");//发布时间:9月18日 8:30
                String publishTimeStr = sdfPublishTime.format(demand.cts);
                setDemandPublishTime(publishTimeStr);
                //详情描述
                setDemandDesc(demand.desc);
                //详情图片
                String[] picFileIds = demand.pic.split(",");
                if (picFileIds.length <= 0) {//这种情况应该不存在，因为至少传一张图片
                    mActivityDemandDetailBinding.llDemandDetailPicLine1.setVisibility(View.GONE);
                    mActivityDemandDetailBinding.llDemandDetailPicLine2.setVisibility(View.GONE);
                } else if (picFileIds.length > 0 && picFileIds.length <= 3) {
                    mActivityDemandDetailBinding.llDemandDetailPicLine1.setVisibility(View.VISIBLE);
                    mActivityDemandDetailBinding.llDemandDetailPicLine2.setVisibility(View.GONE);
                    if (picFileIds.length == 1) {
                        displayServicePic(picFileIds[0], null, null, null, null, null);
                    } else if (picFileIds.length == 2) {
                        displayServicePic(picFileIds[0], picFileIds[1], null, null, null, null);
                    } else if (picFileIds.length == 3) {
                        displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], null, null, null);
                    }
                } else {
                    mActivityDemandDetailBinding.llDemandDetailPicLine1.setVisibility(View.VISIBLE);
                    mActivityDemandDetailBinding.llDemandDetailPicLine2.setVisibility(View.VISIBLE);
                    if (picFileIds.length == 4) {
                        displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], null, null);
                    } else if (picFileIds.length == 5) {
                        displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], picFileIds[4], null);
                    } else if (picFileIds.length == 6) {//这种情况应该不存在，因为最多就只能传5张图片
                        displayServicePic(picFileIds[0], picFileIds[1], picFileIds[2], picFileIds[3], picFileIds[4], picFileIds[5]);
                    }
                }
                //纠纷处理方式
                if (demand.bp == 1) {
                    setDisputeHandingType("平台方式");
                } else if (demand.bp == 2) {
                    setDisputeHandingType("协商方式");
                }
                //上架、下架显示 用isonline字段判断
//                    if(demand.isonline)

                getDemandUserInfo(demand.uid);//获取需求发布者的信息
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("查看需求详情失败:" + result);
            }
        }, demandId + "");
    }

    /**
     * 获取需求发布者的信息
     *
     * @param uid
     */
    private void getDemandUserInfo(long uid) {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                BitmapKit.bindImage(mActivityDemandDetailBinding.ivDemandUserAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar);
                if (uinfo.isauth == 0) {//未认证
                    setIsAuthVisibility(View.INVISIBLE);
                } else if (uinfo.isauth == 1) {//已认证
                    setIsAuthVisibility(View.VISIBLE);
                }
                setUsername(uinfo.name);
                setFanscount("粉丝数" + uinfo.fanscount);
                setTaskcount("顺利成交单数" + uinfo.achievetaskcount + "/" + uinfo.totoltaskcount);//顺利成交单数9/12
                String userPlace = "";
                if (uinfo.province.equals(uinfo.city)) {
                    userPlace = uinfo.province;
                } else {
                    userPlace = uinfo.province + uinfo.city;
                }
                setDemandUserPlace(userPlace);
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("获取需求发布者信息失败:" + result);
            }
        }, uid + "", "0");
    }


    public void goBack(View v) {
        mActivity.finish();
    }

    //进行分享需求操作
    public void shareDemand(View v) {
        ToastUtils.shortToast("Share Demand");
    }

    //底部分享按钮的操作，需求者视角的时候从才会显示
    public void shareDemandBottom(View v) {
        ToastUtils.shortToast("Share Demand Bottom");
    }

    //修改需求内容，会跳转到发布需求的页面，并在发布需求页面自动填充已有的内容
    public void updateDemand(View v) {
        Intent intentPublishDemandBaseInfo = new Intent(CommonUtils.getContext(), PublishDemandBaseInfoActivity.class);
//        intentPublishDemandBaseInfo.putExtra("update", "update");
        intentPublishDemandBaseInfo.putExtra("demandDetailBean", demandDetailBean);
        mActivity.startActivity(intentPublishDemandBaseInfo);
        mActivity.finish();
        if (PublishDemandSuccessActivity.mActivity != null) {
            PublishDemandSuccessActivity.mActivity.finish();
            PublishDemandSuccessActivity.mActivity = null;
        }
    }

    //下架需求操作
    public void offShelfDemand(View v) {
        //调用服务端下架接口，下架成功后显示下架logo
        setOffShelfLogoVisibility(View.VISIBLE);
    }

    //跳转到个人信息界面
    public void gotoUserInfo(View v) {
        ToastUtils.shortToast("跳转至个人信息界面");
    }

    //打开聊天功能
    public void haveAChat(View v) {
        ToastUtils.shortToast("聊一聊");
    }

    //收藏需求
    public void collectDemand(View v) {
        //需要调用服务端收藏接口
        ToastUtils.shortToast("收藏需求");
    }

    //立即抢单
    public void grabDemand(View v) {
        //调用服务端接口进行抢单操作
        ToastUtils.shortToast("立即抢单");
    }

    //定位需求详情中的地址
    public void openDemandDetailLocation(View v) {
        Intent intentDemandDetailLocationActivity = new Intent(CommonUtils.getContext(), DemandDetailLocationActivity.class);
//        intentDemandDetailLocationActivity.putExtra("demandLatLng", demandLatLng);
        mActivity.startActivity(intentDemandDetailLocationActivity);
    }

    private int bottomBtnServiceVisibility;//服务者视角的底部按钮是否显示隐藏
    private int bottomBtnDemandVisibility;//需求者视角的底部按钮是否显示隐藏
    private int topShareBtnVisibility;//服务者视角的顶部分享按钮是否可见
    private int topDemandBtnVisibility;//需求者视角的顶部修改和下架按钮是否可见
    private int offShelfLogoVisibility;//已经下架的需求需要显示下架Logo

    private String demandTitle;//需求标题
    private String demandStartTime;//需求开始时间 开始:9月18日 8:30
    private String quote;//报价 ¥300元
    private String viewCount;//浏览量 300人浏览

    private int offlineItemVisibility;
    private String demandPlace;//"约定地点星湖街328号星湖广场"
    private int instalmentItemVisibility;

    private int isAuthVisibility;
    private String username;
    private String fanscount;
    private String taskcount;
    private String demandUserPlace;

    private String demandPublishTime;
    private String demandDesc;
    private String disputeHandingType;

    @Bindable
    public String getDisputeHandingType() {
        return disputeHandingType;
    }

    public void setDisputeHandingType(String disputeHandingType) {
        this.disputeHandingType = disputeHandingType;
        notifyPropertyChanged(BR.disputeHandingType);
    }

    @Bindable
    public String getDemandDesc() {
        return demandDesc;
    }

    public void setDemandDesc(String demandDesc) {
        this.demandDesc = demandDesc;
        notifyPropertyChanged(BR.demandDesc);
    }

    @Bindable
    public String getDemandPublishTime() {
        return demandPublishTime;
    }

    public void setDemandPublishTime(String demandPublishTime) {
        this.demandPublishTime = demandPublishTime;
        notifyPropertyChanged(BR.demandPublishTime);
    }

    @Bindable
    public String getDemandUserPlace() {
        return demandUserPlace;
    }

    public void setDemandUserPlace(String demandUserPlace) {
        this.demandUserPlace = demandUserPlace;
        notifyPropertyChanged(BR.demandUserPlace);
    }

    @Bindable
    public String getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(String taskcount) {
        this.taskcount = taskcount;
        notifyPropertyChanged(BR.taskcount);
    }

    @Bindable
    public String getFanscount() {
        return fanscount;
    }

    public void setFanscount(String fanscount) {
        this.fanscount = fanscount;
        notifyPropertyChanged(BR.fanscount);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public int getIsAuthVisibility() {
        return isAuthVisibility;
    }

    public void setIsAuthVisibility(int isAuthVisibility) {
        this.isAuthVisibility = isAuthVisibility;
        notifyPropertyChanged(BR.isAuthVisibility);
    }

    @Bindable
    public int getInstalmentItemVisibility() {
        return instalmentItemVisibility;
    }

    public void setInstalmentItemVisibility(int instalmentItemVisibility) {
        this.instalmentItemVisibility = instalmentItemVisibility;
        notifyPropertyChanged(BR.instalmentItemVisibility);
    }

    @Bindable
    public String getDemandPlace() {
        return demandPlace;
    }

    public void setDemandPlace(String demandPlace) {
        this.demandPlace = demandPlace;
        notifyPropertyChanged(BR.demandPlace);
    }

    @Bindable
    public int getOfflineItemVisibility() {
        return offlineItemVisibility;
    }

    public void setOfflineItemVisibility(int offlineItemVisibility) {
        this.offlineItemVisibility = offlineItemVisibility;
        notifyPropertyChanged(BR.offlineItemVisibility);
    }

    @Bindable
    public int getBottomBtnServiceVisibility() {
        return bottomBtnServiceVisibility;
    }

    public void setBottomBtnServiceVisibility(int bottomBtnServiceVisibility) {
        this.bottomBtnServiceVisibility = bottomBtnServiceVisibility;
        notifyPropertyChanged(BR.bottomBtnServiceVisibility);
    }

    @Bindable
    public int getBottomBtnDemandVisibility() {
        return bottomBtnDemandVisibility;
    }

    public void setBottomBtnDemandVisibility(int bottomBtnDemandVisibility) {
        this.bottomBtnDemandVisibility = bottomBtnDemandVisibility;
        notifyPropertyChanged(BR.bottomBtnDemandVisibility);
    }

    @Bindable
    public int getTopShareBtnVisibility() {
        return topShareBtnVisibility;
    }

    public void setTopShareBtnVisibility(int topShareBtnVisibility) {
        this.topShareBtnVisibility = topShareBtnVisibility;
        notifyPropertyChanged(BR.topShareBtnVisibility);
    }

    @Bindable
    public int getTopDemandBtnVisibility() {
        return topDemandBtnVisibility;
    }

    public void setTopDemandBtnVisibility(int topDemandBtnVisibility) {
        this.topDemandBtnVisibility = topDemandBtnVisibility;
        notifyPropertyChanged(BR.topDemandBtnVisibility);
    }

    @Bindable
    public int getOffShelfLogoVisibility() {
        return offShelfLogoVisibility;
    }

    public void setOffShelfLogoVisibility(int offShelfLogoVisibility) {
        this.offShelfLogoVisibility = offShelfLogoVisibility;
        notifyPropertyChanged(BR.offShelfLogoVisibility);
    }

    @Bindable
    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
        notifyPropertyChanged(BR.viewCount);
    }

    @Bindable
    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
        notifyPropertyChanged(BR.quote);
    }

    @Bindable
    public String getDemandStartTime() {
        return demandStartTime;
    }

    public void setDemandStartTime(String demandStartTime) {
        this.demandStartTime = demandStartTime;
        notifyPropertyChanged(BR.demandStartTime);
    }

    @Bindable
    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
        notifyPropertyChanged(BR.demandTitle);
    }
}