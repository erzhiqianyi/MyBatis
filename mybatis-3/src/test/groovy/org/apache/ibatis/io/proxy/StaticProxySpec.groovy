package org.apache.ibatis.io.proxy


import spock.lang.*

@Title("测试静态代理")
@Narrative(""" 测试静态代理  """)
@Subject([UserProxy, UserInterface])
@Unroll
class StaticProxySpec extends Specification {

    def " proxy userInterface By  UserProxy "() {
        given: " a user "
        User user = new User()
        String name = "Proxy"

        when: " proxy user by UserProxy "
        UserProxy userProxy = new UserProxy(user)

        and: " proxy say hello"
        String result = userProxy.sayHello(name)

        then: " user.sayHello should be invoke"
        result == name


    }
}