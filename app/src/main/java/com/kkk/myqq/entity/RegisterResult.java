package com.kkk.myqq.entity;

/**
 * 注册返回值
 * Created by kkk on 2016/5/28.
 * z3jjlzt.github.io
 */
public class RegisterResult {
    public static final int REGISERT_EXIST = 0;
    public static final int REGISERT_SUCCESS = 1;

    @Override
    public String toString() {
        return "RegisterResult{" +
                "isSuccess=" + isSuccess +
                ", err_type='" + err_type + '\'' +
                '}';
    }

    /**
     * isSuccess : 1
     * err_type :
     */

    private int isSuccess;
    private String err_type;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErr_type() {
        return err_type;
    }

    public void setErr_type(String err_type) {
        this.err_type = err_type;
    }
}
