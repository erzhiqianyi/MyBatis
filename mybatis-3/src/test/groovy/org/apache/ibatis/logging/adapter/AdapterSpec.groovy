package org.apache.ibatis.logging.adapter

import org.apache.ibatis.reflection.Reflector
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll


@Title("演示适配器模式基本原理")
@Narrative(""" 使用适配器适配目标对象 """)
@Subject(Reflector)
@Unroll
class AdapterSpec extends Specification {
    def " say hi by adapter"() {
        given: " the adaptee "
        Adaptee adaptee = Mock(Adaptee.class)

        and: " adapter from adaptee "
        Target target = new Adapter(adaptee)

        when: " say hi"
        target.sayHi()

        then:" adpatee.sayHello() should execute"
        1 * adaptee.sayHello()
    }
}