package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewHomeContactsVisitorBinding;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.viewmodel.HomeContactsVisitorModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeContactsVisitorHolder extends BaseHolder<HomeContactsVisitorBean.DataBean.ListBean> implements View.OnClickListener {

    private HomeContactsVisitorModel mHomeContactsVisitorModel;
    private ItemListviewHomeContactsVisitorBinding itemListviewHomeContactsVisitorBinding;
    private Long uid;

    @Override
    public View initView() {
        itemListviewHomeContactsVisitorBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_home_contacts_visitor, null, false);
        mHomeContactsVisitorModel = new HomeContactsVisitorModel(itemListviewHomeContactsVisitorBinding);
        itemListviewHomeContactsVisitorBinding.setHomeContactsVisitorModel(mHomeContactsVisitorModel);
        return itemListviewHomeContactsVisitorBinding.getRoot();
    }

    @Override
    public void refreshView(HomeContactsVisitorBean.DataBean.ListBean data) {
        String avatar = data.getAvatar();
        if(avatar!=null){
            BitmapKit.bindImage(itemListviewHomeContactsVisitorBinding.ivVisitorIcon, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }

        int isauth = data.getIsauth();
        switch (isauth){
            case 1:
                itemListviewHomeContactsVisitorBinding.ivVisitorV.setVisibility(View.GONE);
                break;
            case 0:
                itemListviewHomeContactsVisitorBinding.ivVisitorV.setVisibility(View.VISIBLE);
                break;
        }

        String name = data.getName();
        itemListviewHomeContactsVisitorBinding.tvVisitorName.setText(name);

        String company = data.getCompany();
        String position = data.getPosition();
        itemListviewHomeContactsVisitorBinding.tvVisitorPosition.setText(company+""+position);

        uid = new Long(data.getUid());
        //点击头像到个人中心页面
       // itemListviewHomeContactsVisitorBinding.ivVisitorIcon.setOnClickListener(this);

        String direction = data.getDirection();
        String industry = data.getIndustry();
        if(!TextUtils.isEmpty(direction)){
            itemListviewHomeContactsVisitorBinding.tvVisitorDirection.setText(direction);
        }


        long uts = data.getUts();//最后访问时间，毫秒时间戳
        String time = TimeUtils.getTime(uts);
        itemListviewHomeContactsVisitorBinding.tvContactsVisitorTime.setText(time);
    }

    @Override
    public void onClick(View v) {
       /* Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("Uid",uid);
        intentUserInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentUserInfoActivity);*/
    }
}
