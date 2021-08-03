package org.apache.ibatis.io.singleton;

public enum EnumSingleton implements Singleton{
    Instance;
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
