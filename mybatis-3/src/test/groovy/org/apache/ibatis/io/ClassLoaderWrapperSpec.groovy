package org.apache.ibatis.io


import spock.lang.*

@Title("测试ClassLoaderWrapper")
@Narrative(""" 使用spock 重写 ClassLoaderWrapperTest  """)
@Subject(ClassLoaderWrapper)
@Unroll
class ClassLoaderWrapperSpec extends Specification {

    def " class for name "() {
        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()

        when: " get class from wrapper.classForName()"
        Class result = wrapper.classForName(clazz)

        then: " class should be"
        result == tartet

        where: " class are "
        clazz              || tartet
        "java.lang.Object" || Object.class

    }

    def " class for name not Found"() {
        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()

        when: " get class from wrapper.classForName()"
        Class result = wrapper.classForName(clazz)

        then: ""
        thrown(ClassNotFoundException)
        result == tartet

        where: " class are "
        clazz                                   || tartet
        "some.random.class.that.does.not.Exist" || null

    }

    def "class For Name With ClassLoader"() {
        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()
        and: " a classLoader"
        ClassLoader loader = getClass().getClassLoader()


        when: " get class from wrapper.classForName()"
        Class result = wrapper.classForName(clazz, loader)

        then: ""
        result == tartet

        where: " class are "
        clazz              || tartet
        "java.lang.Object" || Object.class

    }

    def "get Resource As URL"() {

        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()


        when: " get class from wrapper.getResourceAsURL()"
        URL result = wrapper.getResourceAsURL(resource)
        println(result)

        then: "url should not be null  "
        result != null

        where: " resource are "
        resource = 'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties'

    }


    def "get Resource As URL Not Found"() {

        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()


        when: " get class from wrapper.getResourceAsURL()"
        URL result = wrapper.getResourceAsURL(resource)
        println(result)

        then: "url should not be null  "
        result == null

        where: " resource are "
        resource = 'some_resource_that_does_not_exist.properties'

    }

    def "get Resource As URL With class loader"() {

        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()


        and: " a classLoader"
        ClassLoader loader = getClass().getClassLoader()

        when: " get class from wrapper.getResourceAsURL()"
        URL result = wrapper.getResourceAsURL(resource,loader)
        println(result)

        then: "url should not be null  "
        result != null

        where: " resource are "
        resource = 'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties'

    }


    def "get Resource As Stream"() {

        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()


        when: " get class from wrapper.getResourceAsStream()"
        InputStream  result = wrapper.getResourceAsStream(resource)
        println(result)

        then: "stream should not be null  "
        result != null

        where: " resource are "
        resource = 'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties'

    }

    def "get Resource As Stream Not Found"() {

        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()


        when: " get stream from wrapper.getResourceAsStream()"
        InputStream  result = wrapper.getResourceAsStream(resource)
        println(result)

        then: "stream should not be null  "
        result == null

        where: " resource are "
        resource = 'some_resource_that_does_not_exist.properties'

    }


    def "get Resource As Stream with classLoader"() {

        given: " a ClassLoaderWrapper "
        ClassLoaderWrapper wrapper = new ClassLoaderWrapper()

        and: " a classLoader"
        ClassLoader loader = getClass().getClassLoader()

        when: " get class from wrapper.getResourceAsStream()"
        InputStream  result = wrapper.getResourceAsStream(resource,loader)
        println(result)

        then: "stream should not be null  "
        result != null

        where: " resource are "
        resource = 'org/apache/ibatis/databases/jpetstore/jpetstore-hsqldb.properties'

    }






}