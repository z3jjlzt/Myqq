package com.kkk.myqq.entity;

/**
 * 返回值
 * Created by kkk on 2016/5/28.
 * z3jjlzt.github.io
 */
public class LoginResult {

    public static final int LOGIN_NO_EXIST = 0;
    public static final int LOGIN_ERR_PASS = 1;
    public static final int LOGIN_SUCCESS = 2;

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
