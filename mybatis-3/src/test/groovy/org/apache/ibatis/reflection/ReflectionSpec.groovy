package org.apache.ibatis.reflection

import com.sun.corba.se.spi.ior.ObjectKey
import org.apache.ibatis.reflection.domain.ReflectionTestClass
import spock.lang.*

import java.lang.annotation.Annotation
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

@Title("演示反射基础操作")
@Narrative(""" 使用spock 测试反射基础操作  """)
@Subject([Class, Method, Field, Constructor, Annotation])
@Unroll
class ReflectionSpec extends Specification {

    @Shared
    private String classNameSpace;

    void setupSpec() {
        classNameSpace = "org.apache.ibatis.reflection.domain.ReflectionTestClass"
    }


    def " get Class "() {

        when: " get class from Class.forName() "
        Class one = Class.forName(classNameSpace)

        and: " get class from Class.class "
        Class two = ReflectionTestClass.class

        and: "get class from  object.getClass() "
        ReflectionTestClass testClass = new ReflectionTestClass()
        Class three = testClass.getClass()

        then: " all class should be equals"
        one == two
        one == three
        two == three

    }

    def "get Filed "() {
        given: " a Class "
        Class clazz = Class.forName(classNameSpace)

        when: " get fields from getFields() "
        Field[] fields = clazz.getFields()
        then: " fields  amount should be 2 "
        2 == fields.length

        and: " filed are publicTwo , publicOne "
        "publicTwo" == clazz.getField(fields[0].name).name
        "publicOne" == clazz.getField(fields[1].name).name

        when: " get fields from getDeclaredFields()"
        Field[] declaredFields = clazz.getDeclaredFields()

        then: " declaredFields  amount should be 2"
        declaredFields.length == 2

        and: " filed are publicTwo and privateTwo"
        "publicTwo" == clazz.getDeclaredField(declaredFields[0].name).name
        "privateTwo" == clazz.getDeclaredField(declaredFields[1].name).name

    }

    def "get constructor"() {
        given: " a Class "
        Class clazz = Class.forName(classNameSpace)

        when: " get constructor from getConstructors() "
        Constructor[] constructor = clazz.getConstructors()
        sortConstructor(constructor)

        then: " constructor   amount should be 2 "
        constructor.length == 2

        and: " constructor are  ReflectionTestClassRoot()  and ReflectionTestClass(String privateTwo) "
        constructor[0] == clazz.getConstructor()
        constructor[1] == clazz.getConstructor(String.class)

        when: " get constructor from  getDeclaredConstructors() "
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors()
        sortConstructor(declaredConstructors)

        then: "declaredConstructors  amount should be 2"
        declaredConstructors.length == 3

        and: "declaredConstructors  are ReflectionTestClass() , ReflectionTestClass(String privateTwo) and ReflectionTestClass(String publicOne, String privateOne, String publicTwo, String privateTwo)  "
        declaredConstructors[0] == clazz.getDeclaredConstructor()
        declaredConstructors[1] == clazz.getDeclaredConstructor(String.class)
        declaredConstructors[2] == clazz.getDeclaredConstructor(String.class, String.class, String.class, String.class)


    }


    def "get method"() {
        given: " a Class "
        Class clazz = Class.forName(classNameSpace)

        when: " get method from getMethods() "
        Method[] method = clazz.getMethods()
        sortMethod(method)

        then: " method   amount should be 20 "
        method.length == 20

        and: " method are "
        method[0] == clazz.getMethod("equals", Object.class)
        method[1] == clazz.getMethod("getClass")
        method[2] == clazz.getMethod("getPrivateOne")
        method[3] == clazz.getMethod("getPrivateTwo")
        method[4] == clazz.getMethod("getPublicOne")
        method[5] == clazz.getMethod("getPublicTwo")
        method[6] == clazz.getMethod("hashCode")
        method[7] == clazz.getMethod("notify")
        method[8] == clazz.getMethod("notifyAll")
        method[9] == clazz.getMethod("publicMethodOne")
        method[10] == clazz.getMethod("publicMethodTwo")
        method[11] == clazz.getMethod("returnValue", String.class)
        method[12] == clazz.getMethod("setPrivateOne", String.class)
        method[13] == clazz.getMethod("setPrivateTwo", String.class)
        method[14] == clazz.getMethod("setPublicOne", String.class)
        method[15] == clazz.getMethod("setPublicTwo", String.class)
        method[16] == clazz.getMethod("toString")
        method[17] == clazz.getMethod("wait")
        method[18] == clazz.getMethod("wait", long.class, int.class)
        method[19] == clazz.getMethod("wait", long.class)

        when: " get method from getMethods() "
        Method[] declaredMethods = clazz.getDeclaredMethods()
        sortMethod(declaredMethods)

        then: "  declaredMethods   amount should be 20 "
        declaredMethods.length == 9

        and: " method are "
        declaredMethods[0] == clazz.getDeclaredMethod("getPrivateTwo")
        declaredMethods[1] == clazz.getDeclaredMethod("getPublicTwo")
        declaredMethods[2] == clazz.getDeclaredMethod("privateMethodTwo")
        declaredMethods[3] == clazz.getDeclaredMethod("publicMethodTwo")
        declaredMethods[4] == clazz.getDeclaredMethod("returnValue", String.class)
        declaredMethods[5] == clazz.getDeclaredMethod("setPrivateTwo", String.class)
        declaredMethods[6] == clazz.getDeclaredMethod("setPublicTwo", String.class)
        declaredMethods[7] == clazz.getDeclaredMethod("showPrivateTwo")
        declaredMethods[8] == clazz.getDeclaredMethod("toString")

    }

    def " invoke method #param "() {
        given: " a Class "
        Class clazz = Class.forName(classNameSpace)

        when: " create a instance from Class.newInstance() "
        Object object = clazz.newInstance()

        and: " get method from Class.getMethod()"
        Method method = clazz.getMethod("returnValue", String.class)

        and: "invoke method from Method.invoke() "
        String value = method.invoke(object, param)

        then: "value should be  "
        value == result

        where: " method param are "
        param   || result
        "Hello" || "Hello"

    }

    def "create object"() {
        given: " a Class "
        Class clazz = Class.forName(classNameSpace)

        when: " create object from Class.newInstance()"
        Object one = clazz.newInstance()

        and: " create object from Constructor "
        Constructor constructor = clazz.getConstructor()
        Object two = constructor.newInstance()

        then: " object one and two should be  ReflectionTest"
        one instanceof ReflectionTestClass
        two instanceof ReflectionTestClass
    }

    def " use reflect create object and access filed and invoke method"() {
        given: " a Class "
        Class clazz = Class.forName(classNameSpace)

        when: " create object from Class.newInstance()"
        Object object = clazz.newInstance()

        and: " get private field from getDeclaredFields "
        Field field = clazz.getDeclaredField("privateTwo")

        and: " set field access to true "
        field.setAccessible(true)

        and:" set object field value to "
        field.set(object,param)

        and:" set field access to false"
        field.setAccessible(false)

        and:"get  method from getDeclaredMethod  "
        Method method  = clazz.getDeclaredMethod("showPrivateTwo")

        and: "set method access to true"
        method.setAccessible(true)

        and: " invoke method get result  value "
        String value = method.invoke(object)

        then: " value should be "
        value == result

        where: " method param are "
        param   || result
        "Hello" || "Hello"

    }

    def sortConstructor(Constructor[] constructor) {
        Arrays.sort(constructor, new Comparator<Constructor>() {
            @Override
            int compare(Constructor o1, Constructor o2) {
                return o1.getParameterCount() - o2.getParameterCount()
            }
        })
    }

    def sortMethod(Method[] methods) {
        Arrays.sort(methods, new Comparator<Method>() {
            @Override
            int compare(Method o1, Method o2) {
                return o1.getName() <=> o2.getName()
            }
        })
    }
}