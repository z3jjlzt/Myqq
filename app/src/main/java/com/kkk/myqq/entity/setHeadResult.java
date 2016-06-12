package com.kkk.myqq.entity;

/**
 * 设置头像返回值
 * Created by kkk on 2016/5/28.
 * z3jjlzt.github.io
 */
public class setHeadResult {

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }


    private int state;
    private String head;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "setHeadResult{" +
                "state=" + state +
                ", head='" + head + '\'' +
                '}';
    }
}
