package org.apache.ibatis.reflection

import org.apache.ibatis.domain.misc.RichType
import org.apache.ibatis.domain.misc.generics.GenericConcrete
import org.apache.ibatis.reflection.domain.ReflectionTestClass
import spock.lang.*

@Title("测试MetaClass")
@Narrative(""" 使用spock 重写 MetaClassTest  获取MetaClass 信息  """)
@Subject(MetaClass)
@Unroll
class MetaClassSpec extends Specification {

    def " should TestData Type Of Generic Method   "() {
        given: " DefaultReflectorFactory and  meta class "
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        MetaClass metaClass = MetaClass.forClass(classType, reflectorFactory)

        when: " invoke getSetterType and getGetterType "
        Class getterType = metaClass.getGetterType(field)
        Class setterType = metaClass.getSetterType(field)

        then: " getterType and  setterType should be  targetType"
        targetType == getterType
        targetType == setterType


        where: " object and field are  "
        classType                 | field       | targetType
        GenericConcrete.class     | 'id'        | Long.class
        ReflectionTestClass.class | 'publicTwo' | String.class
    }

    def "should Throw Reflection Exception  when field not exists #classType #field   "() {
        given: " DefaultReflectorFactory and  meta class "
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        MetaClass metaClass = MetaClass.forClass(classType, reflectorFactory)

        when: " get getSetterType  "
        metaClass.getGetterType(field)

        then: " caught ReflectionException "
        def caught = thrown(ReflectionException.class)
        caught instanceof ReflectionException
        caught.getMessage() == message

        where: " class and filed are "
        classType      | field     | message
        RichType.class | 'aString' | "There is no getter for property named 'aString' in 'class org.apache.ibatis.domain.misc.RichType'"
    }

    def "should Check Getter Existance #classType #field "() {
        given: " DefaultReflectorFactory and  meta class "
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        MetaClass metaClass = MetaClass.forClass(classType, reflectorFactory)

        when: " check hasGetter and hasSetter "
        boolean hasGetter = metaClass.hasGetter(field)
        boolean hasSetter = metaClass.hasSetter(field)

        then: " hasGetter and hasSetter should be ture "
        hasGetter == true
        hasSetter == true

        where: " class and filed are "
        classType      | field
        RichType.class | 'richField'
        RichType.class | 'richProperty'
        RichType.class | 'richList'
        RichType.class | 'richMap'
        RichType.class | 'richList[0]'
    }


    def "should CheckType For EachGetter and EachSetter #classType  #field  "() {
        given: " DefaultReflectorFactory and  meta class "
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        MetaClass metaClass = MetaClass.forClass(classType, reflectorFactory)

        when: " get getSetterType and getGetterType "
        Class getterType = metaClass.getGetterType(field)
        Class setterType = metaClass.getSetterType(field)

        then: " hasGetter and hasSetter should be ture "
        targetType == getterType
        targetType == setterType

        where: " class and filed are "
        classType      | field          || targetType
        RichType.class | 'richField'    || String.class
        RichType.class | 'richProperty' || String.class
        RichType.class | 'richList'     || List.class
        RichType.class | 'richMap'      || Map.class
        RichType.class | 'richList[0]'  || List.class
    }

    def "should Check GetterName And SetterName #classType "() {
        given: " DefaultReflectorFactory and  meta class "
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        MetaClass metaClass = MetaClass.forClass(classType, reflectorFactory)

        when: " get getGetterNames and getSetterNames  "
        String[] getterType = metaClass.getGetterNames()
        String[] setterType = metaClass.getSetterNames()

        then: " hasGetter and hasSetter should be ture "
        length == getterType.length
        length == setterType.length

        where: " class and filed are "
        classType      || length
        RichType.class || 5
    }

    def "should Check GetterName And SetterName #classType  #property "() {
        given: " DefaultReflectorFactory and  meta class "
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        MetaClass metaClass = MetaClass.forClass(classType, reflectorFactory)

        when: "  findProperty  "
        String propertyName = metaClass.findProperty(property)

        then: " hasGetter and hasSetter should be ture "
        target == propertyName

        where: " class and filed are "
        classType      | property    || target
        RichType.class | 'RICHfield' || 'richField'
    }


}