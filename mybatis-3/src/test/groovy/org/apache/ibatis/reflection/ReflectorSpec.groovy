package org.apache.ibatis.reflection

import org.apache.ibatis.reflection.invoker.Invoker
import spock.lang.*

@Title("演示MyBatis 反射基础操作")
@Narrative(""" 使用spock 重写 ReflectorTest,测试MyBatis 封装反射基本操作 """)
@Subject(Reflector)
@Unroll
class ReflectorSpec extends Specification {

    @Shared
    private ReflectorFactory reflectorFactory

    void setupSpec() {
        reflectorFactory = new DefaultReflectorFactory()
    }

    def " get setter and getter type #clazz #field "() {
        given: " a Reflector from Class"
        Reflector reflector = reflectorFactory.findForClass(clazz)

        when: " get getterType and setterType "
        Class getterType = reflector.getGetterType(field)
        Class setterType = reflector.getSetterType(field)

        then: " getterType and  setterType should be  targetType"
        targetType == getterType
        targetType == setterType


        where: " object and field are  "
        clazz         | field  || targetType
        Section.class | 'id'   || Long.class
        Child.class   | 'id'   || String.class
        Child.class   | 'list' || List.class
        Child.class   | 'fld'  || String.class
    }


    def " should not get filed if not exists"() {
        given: " a Reflector from Class"
        Reflector reflector = reflectorFactory.findForClass(clazz)

        when: "check has getter for field"
        boolean hasField = reflector.hasGetter(field)

        then: " hasField should be"
        hasField == expected

        where: " object and field are  "
        clazz         | field   || expected
        Section.class | 'class' || false

    }

    def "should resolve Readonly Setter With Overload"() {
        given: " a Reflector from Class"
        Reflector reflector = reflectorFactory.findForClass(clazz)

        when: "check has getter for field"
        Class targetType = reflector.getSetterType(field)

        then: " hasField should be"
        targetType == expected

        where: " object and field are  "
        clazz           | field || expected
        BeanClass.class | 'id'  || String.class

    }

    def "should Setters With Unrelated Arg Types Throw Exception"() {
        given: " a Reflector from Class"
        Reflector reflector = reflectorFactory.findForClass(ArgBeanClass.class)

        when: " get set able props "
        List<String> setAbleProps = java.util.Arrays.asList(reflector.getSetablePropertyNames())

        then: " prop1 and prop2 should be exists"
        setAbleProps.contains("prop1")
        setAbleProps.contains("prop2")

        when: "find property name by findPropertyName "
        String prop1TypeName = reflector.findPropertyName("PROP1")
        String prop2TypeName = reflector.findPropertyName("PROP2")

        then: " prop type should be"
        prop1TypeName == "prop1"
        prop2TypeName == "prop2"

        when: " get setterType of prop1"
        Class prop1Type = reflector.getSetterType("prop1")

        then: "type should be "
        prop1Type == String.class

        when: "get setInvoker of prop1"
        Invoker invoker = reflector.getSetInvoker("prop1")

        then: "invoker should not be null "
        null != invoker

        when: "get setter type of prop2 "
        Class<?> prop2ParamType = reflector.getSetterType("prop2")

        then: " prop2 param type should be String or Integer or boolean"
        prop2ParamType == String.class || prop1TypeName == Integer.class || prop1TypeName  == boolean.class



    }

    interface Entity<T> {
        T getId()

        void setId(T id)
    }

    static abstract class AbstractEntity implements Entity<Long> {
        private Long id;

        @Override
        Long getId() {
            return id
        }

        @Override
        void setId(Long id) {
            this.id = id
        }
    }

    static class Section extends AbstractEntity implements Entity<Long> {

    }

    static abstract class Parent<T extends Serializable> {
        protected T id;
        protected List<T> list;
        protected T[] array;
        private T fld;
        public T pubFld;

        T getId() {
            return id
        }

        void setId(T id) {
            this.id = id
        }

        List<T> getList() {
            return list
        }

        void setList(List<T> list) {
            this.list = list
        }

        T[] getArray() {
            return array
        }

        void setArray(T[] array) {
            this.array = array
        }

        T getFld() {
            return fld
        }
    }

    static class Child extends Parent<String> {

    }

    interface BeanInterface<T> {
        void setId(T id)
    }

    class BeanClass implements BeanInterface<String> {
        @Override
        void setId(String id) {

        }
    }

    class ArgBeanClass {
        public void setProp1(String arg) {

        }

        public void setProp2(String arg) {

        }

        public void setProp2(Integer arg) {

        }

        public void setProp2(boolean arg) {

        }
    }
}