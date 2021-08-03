package org.apache.ibatis.io.singleton;

public class HungryManSingleton implements Singleton {
    private static HungryManSingleton instance = new HungryManSingleton();

    private HungryManSingleton() {

    }

    public static HungryManSingleton getInstance() {
        return instance;
    }

    private Integer id;

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
