package org.apache.ibatis.reflection.domain;

public class ReflectRoot {

    public String publicOne;

    private String privateOne;

    public ReflectRoot() {
        this(null, null);
    }

    private ReflectRoot(String publicOne) {
        this(publicOne, null);
    }

    public ReflectRoot(String publicOne, String privateOne) {
        this.publicOne = publicOne;
        this.privateOne = privateOne;
    }

    public String getPublicOne() {
        return publicOne;
    }

    public void setPublicOne(String publicOne) {
        this.publicOne = publicOne;
    }

    public String getPrivateOne() {
        return privateOne;
    }

    public void setPrivateOne(String privateOne) {
        this.privateOne = privateOne;
    }

    public void publicMethodOne() {
        System.out.println("This is a public method one");
    }

    private void privateMethodOne() {
        System.out.println("This is a private method one");
    }


}
