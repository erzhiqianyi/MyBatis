package org.apache.ibatis.reflection.domain;

import org.checkerframework.checker.signature.qual.Identifier;

public class ReflectionTestClass extends ReflectionTestClassRoot {

    @Identifier
    public String publicTwo;

    private String privateTwo;

    public ReflectionTestClass() {
        this(null, null, null, null);
    }

    @Deprecated
    public ReflectionTestClass(String privateTwo) {
        this(null, null, null,privateTwo );
    }

    private ReflectionTestClass(String publicOne, String privateOne, String publicTwo, String privateTwo) {
        super(publicOne, privateOne);
        this.publicTwo = publicTwo;
        this.privateTwo = privateTwo;
    }

    public String getPublicTwo() {
        return publicTwo;
    }

    public void setPublicTwo(String publicTwo) {
        this.publicTwo = publicTwo;
    }

    public String getPrivateTwo() {
        return privateTwo;
    }

    public void setPrivateTwo(String privateTwo) {
        this.privateTwo = privateTwo;
    }

    public void publicMethodTwo() {
        System.out.println("This is a public method two");
    }

    private void privateMethodTwo() {
        System.out.println("This is a public method two");
    }


    public String returnValue(String value) {
        return value;
    }

    @Override
    public String toString() {
        return "Reflect{" +
                "publicTwo='" + publicTwo + '\'' +
                ", privateTwo='" + privateTwo + '\'' +
                '}';
    }

    private String showPrivateTwo() {
        return privateTwo;
    }
}
