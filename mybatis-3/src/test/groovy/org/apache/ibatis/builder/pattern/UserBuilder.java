package org.apache.ibatis.builder.pattern;


public interface UserBuilder {
    UserBuilder setName(String name);

    UserBuilder setEmail(String email);

    UserBuilder setAge(Integer age);

    UserBuilder setSex(Integer sex);

    User build();
}
