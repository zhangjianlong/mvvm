package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.net.sip.SipSession;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.slash.youth.databinding.ActivityRevisePasswordBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SetPassWordProtocol;
import com.slash.youth.ui.activity.RevisePasswordActivity;
import com.slash.youth.utils.LogKit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zss on 2016/11/3.
 */
public class RevisePassWordModel extends BaseObservable {
    private ActivityRevisePasswordBinding activityRevisePasswordBinding;
    public Map<String, String> oldPassWordMap = new HashMap<>();
    public Map<String, String> newPassWordMap = new HashMap<>();
    public Map<String, String> surePassWordMap = new HashMap<>();


    public RevisePassWordModel(ActivityRevisePasswordBinding activityRevisePasswordBinding) {
        this.activityRevisePasswordBinding = activityRevisePasswordBinding;
        initData();
        listener();
    }

    private void initData() {

    }

    private void listener() {
        getOldPassWord(activityRevisePasswordBinding.etOldPassword);
        getNewPassWord(activityRevisePasswordBinding.etNewPassword);
        getSurePassWord(activityRevisePasswordBinding.etCheckNewPassword);
    }
    //原来的密码
    private void getOldPassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String  passWord =  s.toString();
                oldPassWordMap.put("oldpass",passWord);
            }
        });
    }
    //新的密码
    private void getNewPassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String  passWord =  s.toString();
                newPassWordMap.put("newpass",passWord);
            }
        });
    }
    //确认密码
    private void getSurePassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String  passWord =  s.toString();
                surePassWordMap.put("surepass",passWord);
            }
        });
    }

    //设置密码
    public void setPassWord(String oldpass,String newpass){
        SetPassWordProtocol setPassWordProtocol = new SetPassWordProtocol(oldpass,newpass);
        setPassWordProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
            @Override
            public void execute(SetBean dataBean) {
                int rescode = dataBean.rescode;
                if(rescode == 0){
                    SetBean.DataBean data = dataBean.getData();
                    int status = data.getStatus();
                    switch (status){
                        case 1:
                            LogKit.d("设置成功");

                            break;
                        case 2:
                            LogKit.d("设置失败");
                            break;
                    }
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
    }

}