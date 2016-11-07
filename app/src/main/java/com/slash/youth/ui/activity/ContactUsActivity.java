package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityContactUsBinding;
import com.slash.youth.ui.viewmodel.ContactUsModel;
import com.slash.youth.ui.viewmodel.MyHelpModel;

/**
 * Created by zss on 2016/11/4.
 */
public class ContactUsActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private FrameLayout fl;
    private ActivityContactUsBinding activityContactUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityContactUsBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        ContactUsModel contactUsModel = new ContactUsModel(activityContactUsBinding);
        activityContactUsBinding.setContactUsModel(contactUsModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("联系我们");
        fl = (FrameLayout) findViewById(R.id.fl_title_right);
        fl.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }
}
