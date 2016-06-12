package com.kkk.myqq.entity;

/**
 * 返回值
 * Created by kkk on 2016/5/28.
 * z3jjlzt.github.io
 */
public class SetSignResult {

    public static final int SET_FAIL = 0;
    public static final int SET_SUCCESS = 1;


    /**
     * state : 0
     */

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
