package org.apache.ibatis.builder.pattern


import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

@Title("演示构建者模式创建对象")
@Subject(UserBuilder)
@Unroll
class BuilderSpec extends Specification {

    def "create SunnySchool User "() {
        given: "user build "
        UserBuilder userBuilder = new SunnySchoolUserBuilder()
        when: " builder user "
        userBuilder.setName("Test")
                .setAge(10)
                .setSex(1)
                .setEmail("Test@Test.com")
        User user = userBuilder.build()

        then: " user should be create"
        user != null
        user.name == "Test"
        user.age == 10
        user.sex == 1
        user.email ==  "Test@Test.com"
    }

}