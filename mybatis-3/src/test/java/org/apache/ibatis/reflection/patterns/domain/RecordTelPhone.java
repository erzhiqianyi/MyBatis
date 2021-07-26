package org.apache.ibatis.reflection.patterns.domain;

public class RecordTelPhone implements Phone {

    private Phone recordTelPhone;

    public RecordTelPhone(Phone recordTelPhone) {
        this.recordTelPhone = recordTelPhone;
    }

    public String callIn() {
        System.out.println("启动录音");
        String info = recordTelPhone.callIn();
        System.out.println("结束录音并保存文件");
        return info;
    }

    @Override
    public Boolean callOut(String info) {
        System.out.println("启动录音");
        Boolean result = recordTelPhone.callOut(info);
        System.out.println("结束录音并保存文件");
        return result;
    }
}
