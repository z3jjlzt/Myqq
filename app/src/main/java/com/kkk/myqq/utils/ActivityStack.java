package com.kkk.myqq.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by kkk on 2016/5/22.
 * z3jjlzt.github.io
 * activity堆栈管理
 */


public class ActivityStack {
    private static ActivityStack sInstance;
    private static Stack<Activity> mActivityStack;

    private ActivityStack() {
        mActivityStack = new Stack<>();
    }

    public static ActivityStack getInstance() {
        if (sInstance == null) {
            synchronized (ActivityStack.class) {
                if (sInstance == null) {
                    sInstance = new ActivityStack();
                }
            }
        }
        return sInstance;
    }

    public static Stack<Activity> getStack(){
        return mActivityStack;
    }

    /**
     * 出栈
     * @param activity
     */
    public void remoteAcitvity(Activity activity){
        mActivityStack.remove(activity);
    }

    /**
     * 入栈
     * @param activity
     */
    public void addAcitvity(Activity activity){
        mActivityStack.push(activity);
    }

    /**
     * 彻底退出所有activity
     */
    public void finishAllActivity(){
        Activity activity;
        while(!mActivityStack.isEmpty()){
            activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }


}
