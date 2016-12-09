package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by acer on 2016/11/29.
 */
public class AddFrriendRlationProtocol extends BaseProtocol<SetBean> {
    private long uid;
    private String extra;

    public AddFrriendRlationProtocol(long uid, String extra) {
        this.uid = uid;
        this.extra = extra;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.ADD_FRIEND_RELATION;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", String.valueOf(uid));
        params.addBodyParameter("extra",extra);
    }

    @Override
    public SetBean parseData(String result) {
        Gson gson = new Gson();
        SetBean setBean = gson.fromJson(result, SetBean.class);
        return setBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
