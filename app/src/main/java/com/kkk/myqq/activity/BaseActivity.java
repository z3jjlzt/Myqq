package com.kkk.myqq.activity;

import android.app.Activity;
import android.os.Bundle;

import com.kkk.myqq.utils.ActivityStack;

import butterknife.ButterKnife;

/**
 * Created by kkk on 2016/5/22.
 * z3jjlzt.github.io
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        ActivityStack.getInstance().addAcitvity(this);
        bindUI(savedInstanceState);
        ButterKnife.bind(this);
        doLogic();
    }

    protected abstract void initVariables();

    protected abstract void bindUI(Bundle savedInstanceState);

    protected abstract void doLogic();
}
