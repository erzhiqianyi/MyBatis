package org.apache.ibatis.io.singleton;

public class LazyManSingleton implements Singleton {

    private Integer id;

    private static LazyManSingleton instance;

    private LazyManSingleton() {
    }

    public static LazyManSingleton getInstance() {
        if (null == instance) {
            instance = new LazyManSingleton();
        }
        return instance;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
