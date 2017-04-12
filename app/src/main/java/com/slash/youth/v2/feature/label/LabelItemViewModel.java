package com.slash.youth.v2.feature.label;

import android.databinding.ObservableField;

import com.core.op.lib.base.BViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.core.op.lib.messenger.Messenger;
import com.slash.youth.domain.bean.LabelBean;
import com.slash.youth.v2.util.MessageKey;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;

/**
 * Created by acer on 2017/4/6.
 */

public class LabelItemViewModel extends BViewModel {

    public LabelBean data;

    public int index;

    public ObservableField<Boolean> selected = new ObservableField<>();

    public ReplyCommand click = new ReplyCommand(()->{
        selected.set(true);
        Messenger.getDefault().send(index,MessageKey.LABEL_SELECT_STAIR);
    });

    public LabelItemViewModel(RxAppCompatActivity activity, LabelBean data,int index, boolean selected) {
        super(activity);
        this.data = data;
        this.selected.set(selected);
        this.index = index;
    }

    public void setSelected(boolean selected){
        this.selected.set(selected);
    }
}