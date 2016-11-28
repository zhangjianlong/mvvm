package com.slash.youth.engine;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CheckPhoneVerificationCodeProtocol;
import com.slash.youth.http.protocol.GetPhoneVerificationCodeProtocol;
import com.slash.youth.http.protocol.PhoneLogin;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.AuthActivity;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/31.
 */
public class LoginManager {
    public static long currentLoginUserId = 10000;//实际应该在登录状态中获取

    public static IWXAPI iwxApi;
    public static Tencent mTencent;


    static {
        //微信初始化
        iwxApi = WXAPIFactory.createWXAPI(CommonUtils.getContext(), GlobalConstants.ThirdAppId.APPID_WECHAT, true);
        iwxApi.registerApp(GlobalConstants.ThirdAppId.APPID_WECHAT);

        //QQ初始化
        mTencent = Tencent.createInstance(GlobalConstants.ThirdAppId.APPID_QQ, CommonUtils.getContext());
    }

    //调用发送手机验证码接口，将验证码发送到手机上
    public static void getPhoneVerificationCode(String phoneNum) {
        GetPhoneVerificationCodeProtocol getPhoneVerificationCodeProtocol = new GetPhoneVerificationCodeProtocol(phoneNum);
        getPhoneVerificationCodeProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {
            }
        });
    }

    //验证手机上收到的验证码
    public static void checkPhoneVerificationCode(String phoneNum, String pin) {
        CheckPhoneVerificationCodeProtocol checkPhoneVerificationCodeProtocol = new CheckPhoneVerificationCodeProtocol(phoneNum, pin);
        checkPhoneVerificationCodeProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<String>() {
            @Override
            public void execute(String dataBean) {
                ToastUtils.shortToast(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        });
    }

    public static void loginPhoneNum(String phone, String pin, String _3pToken, String userInfo) {
        PhoneLogin phoneLogin = new PhoneLogin(phone, pin, _3pToken, userInfo);
        phoneLogin.getDataFromServer(new BaseProtocol.IResultExecutor<String>() {

            @Override
            public void execute(String dataBean) {
              //  LogKit.v(dataBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        });
    }

    public boolean Login() {
        //TODO 具体的登录逻辑，判断是否登录成功


        return true;
    }


    /**
     * 第三方 微信登录
     */
    public static void loginWeChat() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "slash_youth_login";
        iwxApi.sendReq(req);

    }

    /**
     * 第三方 QQ登录
     */
    public static void loginQQ(LoginActivity.QQLoginUiListener qqLoginUiListener, LoginActivity loginActivity) {
//        if (!mTencent.isSessionValid())
//        {
//            mTencent.login(this, Scope, listener);
//        }
        mTencent.login(loginActivity, "all", qqLoginUiListener);
    }

    /**
     * 第三方 新浪微博登录
     */
    public static void loginWeiBo() {

    }


    //第三方QQ授权
    public  void authorizateQQ( Activity activity) {
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.QQ, umAuthListener);
    }

    //微信授权
    public  void authorizateWEIXIN( Activity activity) {
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    //微博授权
  /*  public  void authorizateWEIBO( Activity activity) {
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.SINA, umAuthListener);
    }*/


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





}




