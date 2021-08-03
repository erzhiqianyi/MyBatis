package org.apache.ibatis.io.singleton;

public class DclSingleton implements Singleton {

    private volatile static DclSingleton instance;

    private DclSingleton() {
    }

    public static DclSingleton getInstance(){
        if (null == instance){
           synchronized (DclSingleton.class){
               if (null == instance){
                   instance = new DclSingleton();
               }
           }
        }
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
