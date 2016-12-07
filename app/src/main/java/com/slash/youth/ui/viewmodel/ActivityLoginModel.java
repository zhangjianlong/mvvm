package com.slash.youth.ui.viewmodel;


import android.Manifest;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityLoginBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.ui.activity.HomeActivity;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.HomeActivity;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UmengTool;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by zhouyifeng on 2016/9/5.
 */
public class ActivityLoginModel extends BaseObservable {
    public static final int PAGE_STATE_REGISTER = 1000;//"新手注册"状态
    public static final int PAGE_STATE_GOTOLOGIN = 1001;//"我有账号,去登录"状态

    ActivityLoginBinding mActivityLoginBinding;
    LoginActivity.QQLoginUiListener qqLoginUiListener;
    LoginActivity loginActivity;
    SsoHandler mSsoHandler;
    int currentPageState = PAGE_STATE_GOTOLOGIN;//当前的页面状态，默认为"我有账号,去登录"状态


    public ActivityLoginModel(ActivityLoginBinding activityLoginBinding, LoginActivity.QQLoginUiListener qqLoginUiListener, LoginActivity loginActivity, SsoHandler ssoHandler) {
        this.mActivityLoginBinding = activityLoginBinding;
        this.qqLoginUiListener = qqLoginUiListener;
        this.loginActivity = loginActivity;
        this.mSsoHandler = ssoHandler;
        initView();
    }

    private void initView() {
        setRegisterAndLoginTextVisibility();
    }


    /**
     * 登录按钮点击事件
     *
     * @param v
     */
    public void login(View v) {
        //TODO 具体的登录逻辑，等服务端相关接口完成以后再实现
//                Intent intentPerfectInfoActivity = new Intent(CommonUtils.getContext(), PerfectInfoActivity.class);
//        intentPerfectInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentPerfectInfoActivity);

        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
        intentHomeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentHomeActivity);

//        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
//        String pin = mActivityLoginBinding.etActivityLoginVerificationCode.getText().toString();
//        LoginManager.checkPhoneVerificationCode(phoenNum, pin);

        //这里跳转至聊天界面只是为了测试聊天界面

     /*   Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("chatCmdName", "sendShareTask");
        intentChatActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentChatActivity);
*/

       // Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
////        intentChatActivity.putExtra("chatCmdName", "sendShareTask");
//
//        Bundle taskInfoBundle = new Bundle();
//        taskInfoBundle.putLong("tid", 111);
//        taskInfoBundle.putInt("type", 1);
//        taskInfoBundle.putString("title", "APP开发");
//        intentChatActivity.putExtra("taskInfo", taskInfoBundle);
//
//        intentChatActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        CommonUtils.getContext().startActivity(intentChatActivity);



    }

    public void sendPhoneVerificationCode(View v) {
        String phoenNum = mActivityLoginBinding.etActivityLoginPhonenum.getText().toString();
        LogKit.v(phoenNum);
        //调用发送手机验证码接口，将验证码发送到手机上
        LoginManager.getPhoneVerificationCode(phoenNum);

    }

    public void wechatLogin(View v) {
      //  LoginManager.loginWeChat();


        UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.WEIXIN, umAuthListener);


        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(loginActivity, mPermissionList, 123);
        }

    }

    public void qqLogin(View v) {
//        LoginManager.loginQQ(qqLoginUiListener, loginActivity);


        UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.QQ, umAuthListener);


    }

    public void weiboLogin(View v) {
//        mSsoHandler.authorize(new SlashWeiboAuthListener());

        //验证包名和签名,是微信那边的
        UmengTool.getSignature(loginActivity);

       /* UMShareAPI mShareAPI = UMShareAPI.get(loginActivity);
        mShareAPI.doOauthVerify(loginActivity, SHARE_MEDIA.SINA, umAuthListener);*/
    }


    public class SlashWeiboAuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                String token = mAccessToken.getToken();
                String uid = mAccessToken.getUid();
                LogKit.v("weibo token:" + token + "    weibo uid:" + uid);
            } else {
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                String code = values.getString("code", "");

            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            ToastUtils.shortToast("Authorize succeed");
            switch (platform){
                case QQ:
                    String QQ_access_token = data.get("access_token");
                    String uid = data.get("uid");
                    SpUtils.setString("QQ_token",QQ_access_token);
                    SpUtils.setString("QQ_uid",uid);
                    break;
                case WEIXIN:
                    String WEIXIN_access_token = data.get("access_token");
                    String openid = data.get("unionid");
                    SpUtils.setString("WEIXIN_token",WEIXIN_access_token);
                    SpUtils.setString("WEIXIN_uid",openid);
                    break;
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToastUtils.shortToast("Authorize fail");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ToastUtils.shortToast("Authorize cancel");
        }
    };

    private int registerVisibility;
    private int gotoLoginVisibility;

    @Bindable
    public int getGotoLoginVisibility() {
        return gotoLoginVisibility;
    }

    public void setGotoLoginVisibility(int gotoLoginVisibility) {
        this.gotoLoginVisibility = gotoLoginVisibility;
        notifyPropertyChanged(BR.gotoLoginVisibility);
    }

    @Bindable
    public int getRegisterVisibility() {
        return registerVisibility;
    }

    public void setRegisterVisibility(int registerVisibility) {
        this.registerVisibility = registerVisibility;
        notifyPropertyChanged(BR.registerVisibility);
    }

    //跳转到“新手注册”状态
    public void register(View v) {
        currentPageState = PAGE_STATE_REGISTER;
        setRegisterAndLoginTextVisibility();
    }

    //跳转到“我有账号,去登录”状态
    public void gotoLogin(View v) {
        currentPageState = PAGE_STATE_GOTOLOGIN;
        setRegisterAndLoginTextVisibility();
    }

    public void setRegisterAndLoginTextVisibility() {
        if (currentPageState == PAGE_STATE_GOTOLOGIN) {
            setRegisterVisibility(View.VISIBLE);
            setGotoLoginVisibility(View.INVISIBLE);
        } else if (currentPageState == PAGE_STATE_REGISTER) {
            setRegisterVisibility(View.INVISIBLE);
            setGotoLoginVisibility(View.VISIBLE);
        }
    }
}
