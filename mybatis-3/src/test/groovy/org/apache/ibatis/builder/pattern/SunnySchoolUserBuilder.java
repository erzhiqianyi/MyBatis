package org.apache.ibatis.builder.pattern;


public class SunnySchoolUserBuilder implements UserBuilder {
    private static final String SCHOOL_NAME = "SunnySchool";
    private static final String EMAIL = "@sunnyschool.com";
    private String name;
    private String email;
    private Integer age;
    private Integer sex;
    private String schoolName;

    @Override
    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public UserBuilder setAge(Integer age) {
        this.age = age;
        return this;
    }

    @Override
    public UserBuilder setSex(Integer sex) {
        this.sex = sex;
        return this;
    }


    @Override
    public User build() {
        if (null != this.name && null == this.email) {
            this.email = this.name.toLowerCase()
                    .replace(" ", "")
                    .concat(EMAIL);
        }
        if (null == this.age) {
            this.age = 7;
        }
        if (null == this.sex) {
            this.sex = 0;
        }
        this.schoolName = SCHOOL_NAME;
        return new User(name,email,age,sex,schoolName);
    }


}
