package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyBidServiceBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.ServiceInstalmentListBean;
import com.slash.youth.domain.ServiceOrderInfoBean;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.CommentActivity;
import com.slash.youth.ui.activity.PaymentActivity;
import com.slash.youth.ui.activity.RefundActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyBidServiceModel extends BaseObservable {

    ActivityMyBidServiceBinding mActivityMyBidServiceBinding;
    Activity mActivity;

    private long tid;
    private long soid;
    private long suid;
    private int fid;//当前是第几期
    private double orderQuote = -1;//必须是服务订单信息接口返回的报价才是准确的
    private int quoteunit = -1;
    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};

    public MyBidServiceModel(ActivityMyBidServiceBinding activityMyBidServiceBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityMyBidServiceBinding = activityMyBidServiceBinding;
        initData();
        initView();
    }

    MyTaskBean myTaskBean;

    private void initData() {
//        tid = mActivity.getIntent().getLongExtra("tid", -1);
        myTaskBean = (MyTaskBean) mActivity.getIntent().getSerializableExtra("myTaskBean");
        tid = myTaskBean.tid;//tid就是soid
        soid = tid;//tid（任务id）就是soid(服务订单id)
        fid = myTaskBean.instalmentcurr;//通过调试接口发现，这个字段当type=2为服务的时候，好像不准，一直都是0

        getDataFromServer();
    }

    private void getDataFromServer() {
        getTaskItemData();
        getServiceDetailFromServer();//通过tid获取服务详情信息
        getServiceOrderInfoData();//根据soid(即tid)获取服务订单状态信息
    }

    private void initView() {

    }

    public void goBack(View v) {
        mActivity.finish();
    }

    /**
     * 打开聊天界面
     *
     * @param v
     */
    public void havaAChat(View v) {

    }

    /**
     * 评价按钮操作
     *
     * @param v
     */
    public void comment(View v) {
        Intent intentCommentActivity = new Intent(CommonUtils.getContext(), CommentActivity.class);

        Bundle commentInfo = new Bundle();
        commentInfo.putLong("tid", tid);
        commentInfo.putInt("type", 2);
        commentInfo.putLong("suid", suid);
        intentCommentActivity.putExtras(commentInfo);

        mActivity.startActivity(intentCommentActivity);
    }

    /**
     * 申请退款按钮
     *
     * @param v
     */
    public void refund(View v) {
        Intent intentRefundActivity = new Intent(CommonUtils.getContext(), RefundActivity.class);

        intentRefundActivity.putExtra("tid", tid);
        intentRefundActivity.putExtra("type", 2);//1需求 2服务

        mActivity.startActivity(intentRefundActivity);
    }

    /**
     * 延期支付按钮
     *
     * @param v
     */
    public void rectifyPayment(View v) {
        setRectifyLayerVisibility(View.VISIBLE);
    }

    /**
     * 确认完成按钮
     *
     * @param v
     */
    public void confirmFinish(View v) {
//        LogKit.v("soid:" + soid + "  fid:" + myTaskBean.instalmentcurr + "");
        ServiceEngine.confirmComplete(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("确认完成成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("确认完成失败");
            }
        }, soid + "", fid + "");
    }

    /**
     * 支付按钮，打开支付界面
     *
     * @param v
     */
    public void openPaymentActivity(View v) {
//        Intent intentServicePaymentActivity = new Intent(CommonUtils.getContext(), ServicePaymentActivity.class);
//
//        intentServicePaymentActivity.putExtra("soid", soid);
//        intentServicePaymentActivity.putExtra("amount", myTaskBean.quote);
//
//        mActivity.startActivity(intentServicePaymentActivity);

        Intent intentPaymentActivity = new Intent(CommonUtils.getContext(), PaymentActivity.class);

        Bundle payInfo = new Bundle();
        payInfo.putLong("tid", tid);
        payInfo.putDouble("quote", orderQuote);
        payInfo.putInt("type", 2);//1需求 2服务
        intentPaymentActivity.putExtras(payInfo);

        mActivity.startActivity(intentPaymentActivity);
    }

    /**
     * 右上角 关闭延期支付浮层
     *
     * @param v
     */
    public void closeRectifyLayer(View v) {
        setRectifyLayerVisibility(View.GONE);
    }

    /**
     * 取消延期支付
     *
     * @param v
     */
    public void cancelRectifyPayment(View v) {
        setRectifyLayerVisibility(View.GONE);
    }

    /**
     * 确定延期支付
     *
     * @param v
     */
    public void okRectifyPayment(View v) {
        ServiceEngine.delayPay(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("延期支付成功");
                setRectifyLayerVisibility(View.GONE);
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("延期支付失败:" + result);
            }
        }, soid + "", fid + "");
    }

    /**
     * 获取对应的单个任务条目信息（从任务列表穿过的myTaskBean可能不是最新的数据）
     */
    private void getTaskItemData() {
        MyTaskEngine.getMyTaskItem(new BaseProtocol.IResultExecutor<MyTaskItemBean>() {
            @Override
            public void execute(MyTaskItemBean dataBean) {
                myTaskBean = dataBean.data.taskinfo;
                tid = myTaskBean.tid;//tid就是soid
                soid = tid;//tid（任务id）就是soid(服务订单id)
                fid = myTaskBean.instalmentcurr;//通过调试接口发现，这个字段当type=2为服务的时候，好像不准，一直都是0
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", "2", "2");
    }


    /**
     * 通过tid获取服务详情信息
     */
    private void getServiceDetailFromServer() {
        MyTaskEngine.getServiceDetailByTid(new BaseProtocol.IResultExecutor<ServiceDetailBean>() {
            @Override
            public void execute(ServiceDetailBean dataBean) {
                ServiceDetailBean.Service service = dataBean.data.service;
                //服务标题，布局文件中有两个地方需要设置
                setServiceTitle(service.title);
                //闲置时间
                SimpleDateFormat sdfIdleTime = new SimpleDateFormat("MM月dd日 hh:mm");
                String starttimeStr = sdfIdleTime.format(service.starttime);
                String endtimeStr = sdfIdleTime.format(service.endtime);
                setIdleTime("闲置时间:" + starttimeStr + "-" + endtimeStr);
                //报价 这里不能使用服务详情接口返回的报价
                quoteunit = service.quoteunit;
                CommonUtils.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (orderQuote != -1) {
                            if (quoteunit == 9) {
                                setQuote(orderQuote + "元");
                            } else if (quoteunit > 0 && quoteunit < 9) {
                                setQuote(orderQuote + "元/" + optionalPriceUnit[quoteunit - 1]);
                            }
                        }
                    }
                });
                //分期
                //这里不能用service详情的instalment，要用任务列表item的instalment
                //但是 目前任务列表item中的分期信息（分期比例）也不对，"instalmentcurr": 0, "instalmentcurrfinish": 0, "instalmentratio": "",
                if (myTaskBean.instalment == 1) {//开启分期
                    setInstalmentVisibility(View.VISIBLE);
                    String instalmentRatioStr = "";
                    String[] ratios = myTaskBean.instalmentratio.split(",");
                    for (int i = 0; i < ratios.length; i++) {
                        String ratio = ratios[i];
                        if (TextUtils.isEmpty(ratio)) {
                            continue;
                        }
                        if (i < ratios.length - 1) {
                            instalmentRatioStr += ratio + "%/";
                        } else {
                            instalmentRatioStr += ratio + "%";
                        }
                    }
                    setInstalmentRatio(instalmentRatioStr);
                } else {//未开启分期
                    setInstalmentVisibility(View.INVISIBLE);
                }
                //纠纷处理方式（似乎协商处理就显示）
                if (service.bp == 2) {//协商
                    setBpConsultVisibility(View.VISIBLE);
                } else if (service.bp == 1) {//平台
                    setBpConsultVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", "2");
    }

    /**
     * 根据soid(即tid)获取服务订单状态信息
     */
    private void getServiceOrderInfoData() {
        //这个接口好像不能使用，可以使用“v1/api/service/orderinfo”接口获取订单信息，里面有status
//        ServiceEngine.getServiceOrderStatus(new BaseProtocol.IResultExecutor<ServiceOrderStatusBean>() {
//            @Override
//            public void execute(ServiceOrderStatusBean dataBean) {
//                int status = dataBean.data.service.status;
//
//            }
//
//            @Override
//            public void executeResultError(String result) {
//
//            }
//        }, soid + "");

        ServiceEngine.getServiceOrderInfo(new BaseProtocol.IResultExecutor<ServiceOrderInfoBean>() {
            @Override
            public void execute(ServiceOrderInfoBean dataBean) {
                orderQuote = dataBean.data.order.quote;
                CommonUtils.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (quoteunit != -1) {
                            if (quoteunit == 9) {
                                setQuote(orderQuote + "元");
                            } else if (quoteunit > 0 && quoteunit < 9) {
                                setQuote(orderQuote + "元/" + optionalPriceUnit[quoteunit - 1]);
                            }
                        }
                    }
                });

                int status = dataBean.data.order.status;
                displayStatusCycle(status);
                displayStatusButton(dataBean);//显示对应不同状态的操作按钮
                suid = dataBean.data.order.suid;
            }

            @Override
            public void executeResultError(String result) {

            }
        }, soid + "");
    }

    ArrayList<ServiceInstalmentListBean.InstalmentInfo> instalmentInfoList;

    private void displayStatusButton(ServiceOrderInfoBean dataBean) {
        ServiceOrderInfoBean.Order order = dataBean.data.order;

        switch (order.status) {
            case 2:/*服务者确认*/
            case 3:/*需求方支付中*/
                setCommentVisibility(View.GONE);
                setConfirmFinishVisibility(View.GONE);
                setPaymentVisibility(View.VISIBLE);
                break;
            case 5:/*订单进行中*/
            case 6:/*订单完成*/
                setCommentVisibility(View.GONE);
                setPaymentVisibility(View.GONE);
                //首先隐藏确认完成条目,然后再根据分期完成情况列表接口返回的数据来判断是显示还是隐藏
                setConfirmFinishVisibility(View.GONE);
                setRectifyVisibility(View.GONE);
                //获取分期情况
                ServiceEngine.getServiceInstalmentList(new BaseProtocol.IResultExecutor<ServiceInstalmentListBean>() {
                    @Override
                    public void execute(ServiceInstalmentListBean dataBean) {
                        instalmentInfoList = dataBean.data.list;
                        int totalInstalment = instalmentInfoList.size();//总的分期数
                        for (int i = 0; i < totalInstalment; i++) {
                            ServiceInstalmentListBean.InstalmentInfo instalmentInfo = instalmentInfoList.get(i);
                            if (instalmentInfo.status != 2) {
                                fid = instalmentInfo.id;
                                if (instalmentInfo.status == 1) {
                                    setConfirmFinishVisibility(View.VISIBLE);
                                    if (fid == totalInstalment) {//如果是最后一期
                                        if (myTaskBean.rectify != 1) {//还没有延过期
                                            setRectifyVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void executeResultError(String result) {
                        ToastUtils.shortToast("获取服务订单的分期信息失败");
                    }
                }, soid + "");
                break;
            case 7:/*订单确认完成*/
                setConfirmFinishVisibility(View.GONE);
                setPaymentVisibility(View.GONE);
                if (order.iscommit == 0) {//未评论
                    setCommentVisibility(View.VISIBLE);
                } else {//已评论
                    setCommentVisibility(View.GONE);
                }
                break;
            case 1:/*初始化订单*/
            case 8:/*申请退款*/
            case 9:/*同意退款*/
            case 10:/*平台申诉处理*/
            case 4:/*订单已经取消*/
            case 11:/*服务方拒绝*/
            default:
                setCommentVisibility(View.GONE);
                setConfirmFinishVisibility(View.GONE);
                setPaymentVisibility(View.GONE);
                break;
        }
    }

    private void displayStatusCycle(int status) {
        switch (status) {
            case 1:/*初始化订单*/
            case 2:/*服务者确认*/
                //预约中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 3:/*需求方支付中*/
                //预支付 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 5:/*订单进行中*/
            case 6:/*订单完成*/
            case 8:/*申请退款*/
            case 9:/*同意退款*/
            case 10:/*平台申诉处理*/
                //服务中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 7:/*订单确认完成*/
                //评价中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4);
                break;
            case 4:/*订单已经取消*/
            case 11:/*服务方拒绝*/
            default:
                //失效 过期 状态 四个圈全都是灰色
                setStatusProgress(R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
        }
    }

    /**
     * 设置 4个圈 表示的大状态进度
     */
    private void setStatusProgress(int bigStateReservationBg, int bigStateReservationTextColor, int bigStatePaymentBg, int bigStatePaymentTextColor, int bigStateServiceBg, int bigStateServiceTextColor, int bigStateCommentBg, int bigStateCommentTextColor) {
        mActivityMyBidServiceBinding.tvServiceReservationing.setBackgroundResource(bigStateReservationBg);
        mActivityMyBidServiceBinding.tvServiceReservationing.setTextColor(bigStateReservationTextColor);
        mActivityMyBidServiceBinding.tvServicePayment.setBackgroundResource(bigStatePaymentBg);
        mActivityMyBidServiceBinding.tvServicePayment.setTextColor(bigStatePaymentTextColor);
        mActivityMyBidServiceBinding.tvServiceServiceing.setBackgroundResource(bigStateServiceBg);
        mActivityMyBidServiceBinding.tvServiceServiceing.setTextColor(bigStateServiceTextColor);
        mActivityMyBidServiceBinding.tvServiceComment.setBackgroundResource(bigStateCommentBg);
        mActivityMyBidServiceBinding.tvServiceComment.setTextColor(bigStateCommentTextColor);
    }

    private String serviceTitle;
    private String idleTime;
    private String quote;
    private int instalmentVisibility;
    private String instalmentRatio;
    private int bpConsultVisibility;

    private int rectifyVisibility = View.GONE;//延期支付按钮是否可见
    private int commentVisibility = View.GONE;
    private int confirmFinishVisibility = View.GONE;
    private int paymentVisibility = View.GONE;
    private int rectifyLayerVisibility = View.GONE;//延期支付的浮层是否可见，默认为不可见

    @Bindable
    public int getRectifyLayerVisibility() {
        return rectifyLayerVisibility;
    }

    public void setRectifyLayerVisibility(int rectifyLayerVisibility) {
        this.rectifyLayerVisibility = rectifyLayerVisibility;
        notifyPropertyChanged(BR.rectifyLayerVisibility);
    }

    @Bindable
    public int getCommentVisibility() {
        return commentVisibility;
    }

    public void setCommentVisibility(int commentVisibility) {
        this.commentVisibility = commentVisibility;
        notifyPropertyChanged(BR.commentVisibility);
    }

    @Bindable
    public int getConfirmFinishVisibility() {
        return confirmFinishVisibility;
    }

    public void setConfirmFinishVisibility(int confirmFinishVisibility) {
        this.confirmFinishVisibility = confirmFinishVisibility;
        notifyPropertyChanged(BR.confirmFinishVisibility);
    }

    @Bindable
    public int getPaymentVisibility() {
        return paymentVisibility;
    }

    public void setPaymentVisibility(int paymentVisibility) {
        this.paymentVisibility = paymentVisibility;
        notifyPropertyChanged(BR.paymentVisibility);
    }

    @Bindable
    public int getRectifyVisibility() {
        return rectifyVisibility;
    }

    public void setRectifyVisibility(int rectifyVisibility) {
        this.rectifyVisibility = rectifyVisibility;
        notifyPropertyChanged(BR.rectifyVisibility);
    }

    @Bindable
    public int getBpConsultVisibility() {
        return bpConsultVisibility;
    }

    public void setBpConsultVisibility(int bpConsultVisibility) {
        this.bpConsultVisibility = bpConsultVisibility;
        notifyPropertyChanged(BR.bpConsultVisibility);
    }

    @Bindable
    public String getInstalmentRatio() {
        return instalmentRatio;
    }

    public void setInstalmentRatio(String instalmentRatio) {
        this.instalmentRatio = instalmentRatio;
        notifyPropertyChanged(BR.instalmentRatio);
    }

    @Bindable
    public int getInstalmentVisibility() {
        return instalmentVisibility;
    }

    public void setInstalmentVisibility(int instalmentVisibility) {
        this.instalmentVisibility = instalmentVisibility;
        notifyPropertyChanged(BR.instalmentItemVisibility);
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
    public String getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(String idleTime) {
        this.idleTime = idleTime;
        notifyPropertyChanged(BR.idleTime);
    }

    @Bindable
    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
        notifyPropertyChanged(BR.serviceTitle);
    }
}

