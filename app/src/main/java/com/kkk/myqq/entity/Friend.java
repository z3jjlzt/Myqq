package com.kkk.myqq.entity;

/**
 * Created by kkk on 2016/5/23.
 * z3jjlzt.github.io
 */
public class Friend {

    /**
     * name : tom
     * signature : happy everyday
     * head : Myqq/tom.jpg
     */

    private String name;
    private String signature;
    private String head;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHead() {
        return head;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", signature='" + signature + '\'' +
                ", head='" + head + '\'' +
                '}';
    }

    public void setHead(String head) {
        this.head = head;
    }
}
