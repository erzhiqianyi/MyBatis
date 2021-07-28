package org.apache.ibatis.annotation;

@Customize(className = "CustomizeTest")
public class CustomizeTest {
    @Customize(filedSort = 0)
    private String name;

    @Customize(ignoreMethod = true)
    public String getName() {
        return name;
    }

    @Customize(ignoreMethod = false)
    public void setName(String name) {
        this.name = name;
    }

    public void changeName(@Customize(paramId = 0l) String name) {
        setName(name);
    }

    public CustomizeTest(@Customize(constructorName = "create") String name) {
        this.name = name;
    }
}
