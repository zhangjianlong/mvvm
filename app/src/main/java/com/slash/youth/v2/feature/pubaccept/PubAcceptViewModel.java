package com.slash.youth.v2.feature.pubaccept;


import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;

import com.core.op.lib.base.BAViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.core.op.lib.di.PerActivity;
import com.core.op.lib.utils.AppToast;
import com.core.op.lib.utils.StrUtil;
import com.slash.youth.R;
import com.slash.youth.databinding.ActPubacceptBinding;
import com.slash.youth.utils.CommonUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;

import javax.inject.Inject;

@PerActivity
public class PubAcceptViewModel extends BAViewModel<ActPubacceptBinding> {
    public final ObservableField<String> title = new ObservableField<>(CommonUtils.getContext().getString(R.string.app_pub_accept_title));
    public final ObservableField<String> templateInput = new ObservableField<>();
    public final ObservableField<String> profileTemplate = new ObservableField<>();
    public final ObservableField<String> templateSize = new ObservableField<>(CommonUtils.getContext().getString(R.string.app_profile_template_limit));
    private String[] templates = CommonUtils.getContext().getResources().getStringArray(R.array.pub_accept_template);
    private int index = 0;

    public TextWatcher templateWatch = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            templateSize.set(temp.length() + "/300");
        }
    };


    @Inject
    public PubAcceptViewModel(RxAppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void afterViews() {
        profileTemplate.set(templates[index]);
    }

    public ReplyCommand changeTemplate = new ReplyCommand(() -> {
        if (index >= templates.length - 1) {
            index = 0;
        }
        index = index + 1;
        profileTemplate.set(templates[index]);
    });


    public ReplyCommand useTemplate = new ReplyCommand(() -> {
        templateInput.set(templates[index]);
    });

    public void savaTemplate() {
        String content = templateInput.get();
        if (content == null || StrUtil.isEmpty(content.toString().trim())) {
            AppToast.show(CommonUtils.getContext(), CommonUtils.getContext().getString(R.string.app_pub_accept_empty));
            return;
        }
        content = content.toString().trim();
        HashMap<String, String> data = new HashMap<>();
        data.put("", content);
    }
}