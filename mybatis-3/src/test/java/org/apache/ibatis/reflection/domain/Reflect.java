package org.apache.ibatis.reflection.domain;

import org.checkerframework.checker.signature.qual.FieldDescriptor;
import org.checkerframework.checker.signature.qual.Identifier;

@Deprecated
public class Reflect extends ReflectRoot {

    @Identifier
    public String publicTwo;

    private String privateTwo;

    public Reflect() {
        this(null, null, null, null);
    }

    @Deprecated
    public Reflect(String publicTwo) {
        this(null, null, publicTwo, null);
    }

    private Reflect(String publicOne, String privateOne, String publicTwo, String privateTwo) {
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


    public String returnValue(String value){
       return  value;
    }
    @Override
    public String toString() {
        return "Reflect{" +
                "publicTwo='" + publicTwo + '\'' +
                ", privateTwo='" + privateTwo + '\'' +
                '}';
    }
}
