package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemMyTaskBinding;
import com.slash.youth.domain.AgreeRefundBean;
import com.slash.youth.domain.DelayPayBean;
import com.slash.youth.domain.InterventionBean;
import com.slash.youth.domain.PaymentBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class ItemMyTaskModel extends BaseObservable {
    ItemMyTaskBinding mItemMyTaskBinding;
    public long uid;//当前任务的用户ID
    public long tid;//任务ID (即:需求或者服务ID)
    public long loginUserId;//当前登录的用户ID
    public double amount;
    public int channel;

    public ItemMyTaskModel(ItemMyTaskBinding itemMyTaskBinding) {
        this.mItemMyTaskBinding = itemMyTaskBinding;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        loginUserId = getCurrentLoginUserId();
        amount = getCurrentPayAmount();
        //TODO 给channel赋值 暂时写死，模拟数据
        channel = 0;

    }

    private double getCurrentPayAmount() {
        //TODO 获取当前所要支付的金额 暂时写死，模拟数据
        return 10000;
    }

    //获取当前登录的用户ID
    private long getCurrentLoginUserId() {
        //todo 暂时写死
        return 10000;
    }

    //弹出聊一聊弹框
    public void haveAChat(View v) {
        ToastUtils.shortToast("弹出聊一聊弹框");
    }

    //服务者在未确认之前，修改抢单信息
    public void updateBidInfo(View v) {
        //需要再次弹出抢单页面，重新填写抢单信息
    }

    //当需求方选择服务方，服务方不接受需求方
    public void noAcceptDemand(View v) {
        DemandEngine.servicePartyReject(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    //当需求方选择服务方，服务方接受需求方
    public void acceptDemand(View v) {
        DemandEngine.servicePartyConfirmServant(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", loginUserId + "");
    }

    //服务方每完成一期，可以点击完成
    public void completeDemand(View v) {
        DemandEngine.servicePartyComplete(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    //服务方申诉，当需求方申请退款，服务方如果不同意，可以申诉
    public void complain(View v) {
        DemandEngine.servicePartyIntervention(new BaseProtocol.IResultExecutor<InterventionBean>() {
            @Override
            public void execute(InterventionBean dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    //服务方同意退款
    public void agreeRefund(View v) {
        DemandEngine.servicePartyAgreeRefund(new BaseProtocol.IResultExecutor<AgreeRefundBean>() {
            @Override
            public void execute(AgreeRefundBean dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "");
    }

    //当需求方选择的服务方有一个同意以后，就形成了一对一的关系，需求方需要去支付
    public void pay(View v) {
        DemandEngine.demandPartyPrePayment(new BaseProtocol.IResultExecutor<PaymentBean>() {
            @Override
            public void execute(PaymentBean dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", amount + "", channel + "");
    }

    //当服务方完成最后一期后，如果需求方不满意，可以延期支付，回滚一期，延期支付的机会有一次
    public void delayPay(View v) {
        int fid = 4;//TODO 模拟假数据,暂时不知道这个字段是什么意思
        DemandEngine.delayPay(new BaseProtocol.IResultExecutor<DelayPayBean>() {
            @Override
            public void execute(DelayPayBean dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", fid + "");
    }

    //服务方完成每一期任务，需求方确认完成
    public void confirmComplete(View v) {
        int fid = 4;//TODO 模拟假数据,暂时不知道这个字段是什么意思
        DemandEngine.demandPartyConfirmComplete(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {

            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", fid + "");
    }

    //任务完成后，需求方进行评价
    public void comment(View v) {

    }


    /**
     * @param status 需求 or 服务状态
     * @param type   需求或者服务类型 1需求 2服务
     * @param roleid 表示是我抢的单子 还是 我发布的任务 1发布者 2抢单者 （这个字段比较重要，用于判断单子类型）
     */
    public void displayCurrentBigStatusAndButtons(int status, int type, int roleid) {
        if (status == 1) {
            setStatusText("预约中");
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 2) {
            setStatusText("已过期");
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_huise);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 3) {
            setStatusText("已过期");
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_huise);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 4) {//目前服务端接口的任务状态列表中没有这个值
            setStatusText("预约中");//带选择
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

        } else if (status == 5) {
            setStatusText("预约中");//待确认
            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 6) {
            setStatusText("待支付");//待支付
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 7) {
            setStatusText("服务中");//进行中
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 8) {
            setStatusText("待评价");//已完成
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_bg);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 9) {
            setStatusText("已过期");//已退款
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_huise);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        } else if (status == 10) {
            setStatusText("已过期");//已淘汰
            mItemMyTaskBinding.tvMyTaskStatus.setBackgroundResource(R.mipmap.state_huise);

            if (type == 1) {//当前任务是需求
                if (roleid == 1) {//需求发布者

                } else if (roleid == 2) {//抢需求者

                }
            } else if (type == 2) {//当前任务是服务

            }

        }
    }

    private String taskTitle;//任务标题
    private String startTime;//任务开始时间
    private int addVvisibility;//是否显示加V认证
    private String username;//用户名
    private String instalmentText;//分期显示的文本
    private String quote;//报价
    private int publishDemandStatusPointVisibility;//我发的需求状态小圆点是否可见（里面有数字，新增的抢单者数量）
    private int bidDemandStatusPointVisibility;//我抢的需求状态小圆点是否可见（没有需求，有状态变化，只需要显示圆点）
    private String bidnum;//抢单数量，服务端返回的是所有的抢单数量，这里需要新增的抢单数量
    private String statusText;
    private String instalmentratioStr;//显示的分期支付比例


    @Bindable
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
        notifyPropertyChanged(BR.taskTitle);
    }

    @Bindable
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        notifyPropertyChanged(BR.startTime);
    }

    @Bindable
    public int getAddVvisibility() {
        return addVvisibility;
    }

    public void setAddVvisibility(int addVvisibility) {
        this.addVvisibility = addVvisibility;
        notifyPropertyChanged(BR.addVvisibility);
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
    public String getInstalmentText() {
        return instalmentText;
    }

    public void setInstalmentText(String instalmentText) {
        this.instalmentText = instalmentText;
        notifyPropertyChanged(BR.instalmentText);
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
    public int getPublishDemandStatusPointVisibility() {
        return publishDemandStatusPointVisibility;
    }

    public void setPublishDemandStatusPointVisibility(int publishDemandStatusPointVisibility) {
        this.publishDemandStatusPointVisibility = publishDemandStatusPointVisibility;
        notifyPropertyChanged(BR.publishDemandStatusPointVisibility);
    }

    @Bindable
    public int getBidDemandStatusPointVisibility() {
        return bidDemandStatusPointVisibility;
    }

    public void setBidDemandStatusPointVisibility(int bidDemandStatusPointVisibility) {
        this.bidDemandStatusPointVisibility = bidDemandStatusPointVisibility;
        notifyPropertyChanged(BR.bidDemandStatusPointVisibility);
    }

    @Bindable
    public String getBidnum() {
        return bidnum;
    }

    public void setBidnum(String bidnum) {
        this.bidnum = bidnum;
        notifyPropertyChanged(BR.bidnum);
    }

    @Bindable
    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
        notifyPropertyChanged(BR.statusText);
    }

    @Bindable
    public String getInstalmentratioStr() {
        return instalmentratioStr;
    }

    public void setInstalmentratioStr(String instalmentratioStr) {
        this.instalmentratioStr = instalmentratioStr;
        notifyPropertyChanged(BR.instalmentratioStr);
    }
}
