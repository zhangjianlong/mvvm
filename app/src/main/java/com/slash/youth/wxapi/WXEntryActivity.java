package com.slash.youth.wxapi;

//import android.app.Activity;
//import android.os.Bundle;
//
//import com.slash.youth.global.GlobalConstants;
//import com.slash.youth.utils.CommonUtils;
//import com.slash.youth.utils.ToastUtils;
//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
///**
// * Created by zhouyifeng on 2016/9/17.
// */
//public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
////    微信发送的请求将回调到onReq方法，发送到微信请求的响应结果将回调到onResp方法
//
//    private IWXAPI iwxApi;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        ToastUtils.shortToast("WXEntryActivity....");
//        super.onCreate(savedInstanceState);
//        iwxApi = WXAPIFactory.createWXAPI(CommonUtils.getContext(), GlobalConstants.ThirdAppId.APPID_WECHAT, true);
//        iwxApi.handleIntent(getIntent(), this);
//    }
//
//    @Override
//    public void onReq(BaseReq baseReq) {
//        ToastUtils.shortToast("微信调用onReq");
//    }
//
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        ToastUtils.shortToast("微信调用onResp");
//    }
//}


import com.umeng.socialize.weixin.view.WXCallbackActivity;


public class WXEntryActivity extends WXCallbackActivity {



}
