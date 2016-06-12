package com.kkk.myqq.entity;

/**
 * Created by kkk on 2016/5/25.
 * z3jjlzt.github.io
 */
public class Msg {
    //是否为收到的信息
    boolean isLeft;
    //消息来自
    String fromname;
    //消息目的地
    String toname;
    //消息
    String msg;
    //时间
    Long time;

    public Msg() {
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public String getFromName() {
        return fromname;
    }

    public void setFrom(String fromname) {
        this.fromname = fromname;
    }

    public String getToName() {
        return toname;
    }

    public void setTo(String toname) {
        this.toname = toname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Msg(boolean isLeft, String fromname, String toname, String msg, Long time) {
        this.isLeft = isLeft;
        this.fromname = fromname;
        this.toname = toname;
        this.msg = msg;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "isLeft=" + isLeft +
                ", from='" + fromname + '\'' +
                ", to='" + toname + '\'' +
                ", msg='" + msg + '\'' +
                ", time=" + time +
                '}';
    }
}
