package org.apache.ibatis.reflection.patterns.domain;

public class TelPhone implements Phone{

    @Override
    public String callIn() {
        System.out.println("接收语音");
        return "Call In";

    }

    @Override
    public Boolean callOut(String info) {
        System.out.println("发送语音" + info);
        return true;
    }
}
