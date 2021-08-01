package org.apache.ibatis.reflection

import org.apache.ibatis.reflection.typeparam.Level0Mapper
import org.apache.ibatis.reflection.typeparam.Level1Mapper
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


@Title("演示MyBatis  TypeParameterResolver 处理返回结果")
@Narrative(""" 使用spock 重写 TypeParameterResolverTest 处理返回结果 """)
@Subject(TypeParameterResolver)
@Unroll
class TypeParameterResolverSpec extends Specification {

    def " get return type "() {
        given: " a class and method from class "

        Method method
        if (null != methodParam) {
            method = clazz.getMethod(methodName, methodParam)
        } else {
            method = clazz.getMethod(methodName)
        }

        when: "resolve return type "
        Type result = TypeParameterResolver.resolveReturnType(method, clazz);


        then: "return type should be"
        result == returnType

        where: " class and method are"
        clazz              | methodName              | methodParam   || returnType
        Level0Mapper.class | 'simpleSelect'          | null          || Double.class
        Level1Mapper.class | 'simpleSelectVoid'      | Integer.class || void.class
        Level1Mapper.class | 'simpleSelectPrimitive' | int.class     || double.class
        Level1Mapper.class | 'simpleSelect'          | null          || Double.class
    }

}