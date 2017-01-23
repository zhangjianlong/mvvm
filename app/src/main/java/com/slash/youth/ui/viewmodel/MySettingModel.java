package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMySettingBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.RecodeBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SetMsgBean;
import com.slash.youth.domain.SetTimeBean;
import com.slash.youth.engine.AccountManager;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.MySettingProtocol;
import com.slash.youth.http.protocol.SetMsgProtocol;
import com.slash.youth.http.protocol.SetTimeProtocol;
import com.slash.youth.ui.activity.FindPassWordActivity;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.ui.activity.MySettingActivity;
import com.slash.youth.ui.activity.RevisePasswordActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import org.greenrobot.greendao.internal.LongHashMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zss on 2016/11/3.
 */
public class MySettingModel extends BaseObservable {
    private ActivityMySettingBinding activityMySettingBinding;
    private HashMap<String, String> paramsMap = new HashMap<>();
    private int status;
    private String endtime;
    private String starttime;
    private int timeStatus;
    private int msgStatus;
    private MySettingActivity mySettingActivity;
    private boolean isTimeOpen;//时间设置
    private boolean isMsgOpen;//消息设置
    private String startTime = "22:00";
    private String endTime = "08:00";
    private String SET_OK = "设置成功";
    private String SET_FAIL = "设置失败";
    private String currentTime;
    private String findPassWord = "找回交易密码";
    private String setPassWord = "设置交易密码";
    private int logoutDialogVisibility = View.GONE;
    private int type;
    private int hintDialogVisibility = View.GONE;

    public MySettingModel(ActivityMySettingBinding activityMySettingBinding, MySettingActivity mySettingActivity) {
        this.activityMySettingBinding = activityMySettingBinding;
        this.mySettingActivity = mySettingActivity;
        initView();
        initData();
    }

    //判断是否有交易密码
    private void testPassWord() {
        AccountManager.getTradePasswordStatus(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                int rescode = dataBean.rescode;
                if (rescode == 0) {
                    int status = dataBean.data.status;
                    switch (status) {
                        case 1://1表示当前有交易密码
                            activityMySettingBinding.viewRevise.setVisibility(View.VISIBLE);
                            activityMySettingBinding.rlRevise.setVisibility(View.VISIBLE);
                            activityMySettingBinding.tvSetAndfindPassword.setText(findPassWord);
                            type = 1;
                            break;
                        case 2:// 2表示当前没有交易密码
                            activityMySettingBinding.viewRevise.setVisibility(View.GONE);
                            activityMySettingBinding.rlRevise.setVisibility(View.GONE);
                            activityMySettingBinding.tvSetAndfindPassword.setText(setPassWord);
                            type = 2;
                            break;
                        case 3://3有密码处于审核中
                            activityMySettingBinding.viewRevise.setVisibility(View.VISIBLE);
                            activityMySettingBinding.rlRevise.setVisibility(View.VISIBLE);
                            activityMySettingBinding.tvSetAndfindPassword.setText(setPassWord);
                            type = 3;
                            break;
                    }
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result：" + result);
            }
        });
    }

    private void initView() {
        testPassWord();
    }

    @Bindable
    public int getLogoutDialogVisibility() {
        return logoutDialogVisibility;
    }

    public void setLogoutDialogVisibility(int logoutDialogVisibility) {
        this.logoutDialogVisibility = logoutDialogVisibility;
        notifyPropertyChanged(BR.logoutDialogVisibility);
    }

    @Bindable
    public int getHintDialogVisibility() {
        return hintDialogVisibility;
    }

    public void setHintDialogVisibility(int hintDialogVisibility) {
        this.hintDialogVisibility = hintDialogVisibility;
        notifyPropertyChanged(BR.hintDialogVisibility);
    }

    private void initData() {
        //第一次网络获取状态，展示时间
        getTimeState();
        //信息展示
        getMsgState();
    }

    private void getTimeState() {
        SetTimeProtocol setTimeProtocol = new SetTimeProtocol();
        setTimeProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetTimeBean>() {
            @Override
            public void execute(SetTimeBean dataBean) {
                int rescode = dataBean.getRescode();
                if (rescode == 0) {
                    SetTimeBean.DataBean data = dataBean.getData();
                    SetTimeBean.DataBean.DataBean1 timeData = data.getData();
                    int timeDnd = timeData.getDnd(); //1表示已经设置 0表示未设置
                    switchTimeDnd2Boolean(timeDnd);
                    endtime = timeData.getEndtime();
                    starttime = timeData.getStarttime();
                    if (starttime != null && endtime != null) {
                        activityMySettingBinding.tvTime.setText(starttime + "-" + endtime);
                    }
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
    }

    private void getMsgState() {
        SetMsgProtocol setMsgProtocol = new SetMsgProtocol();
        setMsgProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetMsgBean>() {
            @Override
            public void execute(SetMsgBean dataBean) {
                int rescode = dataBean.getRescode();
                if (rescode == 0) {
                    SetMsgBean.DataBean data = dataBean.getData();
                    SetMsgBean.DataBean.DataBean1 msgData = data.getData();
                    int msgDnd = msgData.getDnd();//1表示已经设置 0表示未设置
                    switchMsgDnd2Boolean(msgDnd);
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
    }

    //设置消息
    public void setMsg(View view) {
        //点击时间弹出对话框
        showDialog();
    }

    //点击设置时间
    public void setTime(View view) {
        if (isTimeOpen) {
            activityMySettingBinding.ivToggleOpen.setVisibility(View.VISIBLE);
            activityMySettingBinding.ivToggleClose.setVisibility(View.GONE);
            timeStatus = 1;
        } else {
            activityMySettingBinding.ivToggleOpen.setVisibility(View.GONE);
            activityMySettingBinding.ivToggleClose.setVisibility(View.VISIBLE);
            timeStatus = 0;
        }
        isTimeOpen = !isTimeOpen;
        //点击状态，传回后端保存
        paramsMap.put("status", String.valueOf(timeStatus));
        paramsMap.put("starttime", startTime);
        paramsMap.put("endtime", endTime);
        int state = setData(GlobalConstants.HttpUrl.SET_TIME_SET, paramsMap);
    }

    private void showDialog() {
        DialogUtils.showDialogFive(mySettingActivity, "是否开启免打扰？", "开启后，不接受陌生用户信息，不影响抢单和预约", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                if (isMsgOpen) {
                    activityMySettingBinding.ivToggleOpen1.setVisibility(View.VISIBLE);
                    activityMySettingBinding.ivToggleClose1.setVisibility(View.GONE);
                    msgStatus = 1;
                } else {
                    activityMySettingBinding.ivToggleOpen1.setVisibility(View.GONE);
                    activityMySettingBinding.ivToggleClose1.setVisibility(View.VISIBLE);
                    msgStatus = 0;
                }
                isMsgOpen = !isMsgOpen;
                //点击状态，传回后端保存
                paramsMap.put("status", String.valueOf(msgStatus));
                int state = setData(GlobalConstants.HttpUrl.SET_MSG_SET, paramsMap);
            }

            @Override
            public void CancleDown() {
                LogKit.d("cannel");
            }
        });
    }

    //设置初始化状态
    public void setOriginalTimeState(boolean isOpen) {
        if (isOpen) {
            activityMySettingBinding.ivToggleOpen.setVisibility(View.VISIBLE);
            activityMySettingBinding.ivToggleClose.setVisibility(View.GONE);
        } else {
            activityMySettingBinding.ivToggleOpen.setVisibility(View.GONE);
            activityMySettingBinding.ivToggleClose.setVisibility(View.VISIBLE);
        }
    }

    //设置初始化状态
    public void setOriginalMsgState(boolean isOpen) {
        if (isOpen) {
            activityMySettingBinding.ivToggleOpen1.setVisibility(View.VISIBLE);
            activityMySettingBinding.ivToggleClose1.setVisibility(View.GONE);
        } else {
            activityMySettingBinding.ivToggleOpen1.setVisibility(View.GONE);
            activityMySettingBinding.ivToggleClose1.setVisibility(View.VISIBLE);
        }
    }

    //选择设置dnd到boolean
    private void switchTimeDnd2Boolean(int timeDnd) {
        switch (timeDnd) {
            case 0://未设置
                setOriginalTimeState(false);
                isTimeOpen = true;
                break;
            case 1://已设置
                setOriginalTimeState(true);
                isTimeOpen = false;
                break;
        }
    }

    private void switchMsgDnd2Boolean(int msgDnd) {
        switch (msgDnd) {
            case 0://未设置
                setOriginalMsgState(false);
                isMsgOpen = true;
                break;
            case 1://已设置
                setOriginalMsgState(true);
                isMsgOpen = false;
                break;
        }
    }

    //设置数据
    private int setData(String url, HashMap<String, String> paramsMap) {
        MySettingProtocol mySettingProtocol = new MySettingProtocol(url, paramsMap);
        mySettingProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
            @Override
            public void execute(SetBean dataBean) {
                int rescode = dataBean.rescode;
                SetBean.DataBean data = dataBean.getData();
                //获取状态
                status = data.getStatus();
                if (status == 1) {
                    LogKit.d(SET_OK);
                } else {
                    LogKit.d(SET_FAIL);
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
        return status;
    }

    //修改密码
    public void revisePassWord(View view) {
        Intent intentRevisePasswordActivity = new Intent(CommonUtils.getContext(), RevisePasswordActivity.class);
        mySettingActivity.startActivityForResult(intentRevisePasswordActivity, 2);
    }

    //设置密码
    public void setPassWord(View view) {
        switch (type) {
            case 1:
                //找回交易密码
                testFindPassWord(type);
                break;
            case 2:
                jumpIntoActivity(type);//设置交易密码
                break;
            case 3:
                //弹窗
                setHintDialogVisibility(View.VISIBLE);
                break;
        }
    }

    //验证找回密码的状态
    private void testFindPassWord(final int type) {
        MyManager.testFindPassWord(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                int rescode = dataBean.rescode;
                if (rescode == 0) {
                    CommonResultBean.Data data = dataBean.data;
                    int status = data.status;
                    switch (status) {
                        case 1://1有审核中的交易密码
                            activityMySettingBinding.tvFindPassWordHint.setText("密码找回在审核中，请等待审核");
                            setHintDialogVisibility(View.VISIBLE);
                            break;
                        case 2://2没有审核中的交易密码
                            jumpIntoActivity(type);
                            break;
                    }
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:" + result);
            }
        });
    }

    private void jumpIntoActivity(int type) {
        Intent intentFindPassWordActivity = new Intent(CommonUtils.getContext(), FindPassWordActivity.class);
        intentFindPassWordActivity.putExtra("type", type);
        mySettingActivity.startActivityForResult(intentFindPassWordActivity, Constants.MYSETTING_SETPASSWORD);
    }

    //提示确定
    public void hintsure(View view) {
        setHintDialogVisibility(View.GONE);
    }

    //退出程序
    public void finishApp(View view) {
        setLogoutDialogVisibility(View.VISIBLE);
    }

    //确认
    public void sure(View view) {
        logout(LoginManager.token);
        setLogoutDialogVisibility(View.GONE);

        SlashApplication application = (SlashApplication) CommonUtils.getApplication();
        ArrayList<Activity> listActivities = application.listActivities;
        for (Activity activity : listActivities) {
            if (activity != null) {
                activity.finish();
                activity = null;
                listActivities.remove(activity);
            } else {
                listActivities.remove(activity);
            }
        }


        Intent intentLoginActivity = new Intent(CommonUtils.getContext(), LoginActivity.class);
        mySettingActivity.startActivity(intentLoginActivity);

    }

    //取消
    public void cannel(View view) {
        setLogoutDialogVisibility(View.GONE);
    }

    //登录退出的接口
    private void logout(String token) {
        LoginManager.logout(new onLogout(), token);
    }

    public class onLogout implements BaseProtocol.IResultExecutor<RecodeBean> {
        @Override
        public void execute(RecodeBean dataBean) {
            int rescode = dataBean.getRescode();
            switch (rescode) {
                case 0:
                    ToastUtils.shortToast("退出成功");
                    break;
                case 1:
                    setLogoutDialogVisibility(View.GONE);
                    LogKit.d("退出失败");
                    ToastUtils.shortToast("退出失败");
                    break;
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }
}
