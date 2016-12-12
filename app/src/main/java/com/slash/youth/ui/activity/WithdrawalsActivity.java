package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.LayoutWithdrawalsBinding;
import com.slash.youth.ui.viewmodel.MyAccountModel;
import com.slash.youth.ui.viewmodel.WithdrawalsModel;

/**
 * Created by zss on 2016/11/6.
 */
public class WithdrawalsActivity extends Activity implements View.OnClickListener {
    private TextView title;
    private LayoutWithdrawalsBinding layoutWithdrawalsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutWithdrawalsBinding = DataBindingUtil.setContentView(this, R.layout.layout_withdrawals);
        WithdrawalsModel withdrawalsModel = new WithdrawalsModel(layoutWithdrawalsBinding,this);
        layoutWithdrawalsBinding.setWithdrawalsModel(withdrawalsModel);

        listener();
    }
    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("提现");
        findViewById(R.id.fl_title_right).setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }

}