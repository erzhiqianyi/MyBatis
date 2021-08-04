package org.apache.ibatis.logging.adapter;

public class Adapter implements Target{
    private Adaptee adaptee;

    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void sayHi() {
        adaptee.sayHello();
    }
}
