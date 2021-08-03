package org.apache.ibatis.io

import org.apache.ibatis.annotations.CacheNamespace
import org.apache.ibatis.reflection.MetaClass
import org.junit.jupiter.api.Test
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import java.security.AccessController
import java.security.PrivilegedAction

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue


@Title("测试ResolverUtil")
@Narrative(""" 使用spock 重写 ResolverUtilTest ,测试类过滤 """)
@Subject(MetaClass)
@Unroll
class ResolverUtilSpec extends Specification {
    @Shared
    private ClassLoader currentContextClassLoader


    def setupSpec() {
        currentContextClassLoader = Thread.currentThread().getContextClassLoader()
    }

    def "get classes"() {
        when: "get matches Classes from ResolverUtil.getClasses()"
        Set<Class> classes = new ResolverUtil<>().getClasses()

        then: "classes size should be 0 "
        classes.size() == 0
    }

    def "get class loader "() {
        when: "get classLoader from ResolverUtil.getClassLoader()"
        ClassLoader classLoader = new ResolverUtil<>().getClassLoader()

        then: "classLoader should be current context class loader "
        classLoader == currentContextClassLoader

    }

    def "set class loader"() {
        given: " a ResolverUtil "
        ResolverUtil resolverUtil = new ResolverUtil()

        when: " set classLoader"
        resolverUtil.setClassLoader(currentContextClassLoader)

        then: "classLoader should be current context class loader"
        resolverUtil.getClassLoader() == currentContextClassLoader

    }

    def " find implementation with null package name"() {
        given: " a ResolverUtil "
        ResolverUtil<VFS> resolverUtil = new ResolverUtil<>()

        when: " findImplementations"
        resolverUtil.findImplementations(VFS.class, null)
        Set<Class> classes = new ResolverUtil<>().getClasses()

        then: " matches  class should be 0"
        classes.size() == 0
    }

    def "find implementation #clazz"() {

        when: " findImplementations "
        boolean isAssignableFrom = VFS.class.isAssignableFrom(clazz)

        then: " classes isAssignableFrom should be true"
        isAssignableFrom

        where: " classes are "
        clazz << new ResolverUtil<>().findImplementations(VFS.class, "org.apache.ibatis.io").getClasses()
    }

    def "find Annotated With Null Package Name"() {
        given: " a ResolverUtil "
        ResolverUtil<VFS> resolverUtil = new ResolverUtil<>()

        when: " findAnnotated "
        resolverUtil.findAnnotated(CacheNamespace.class, null)
        Set<Class> classes = new ResolverUtil<>().getClasses()

        then: " matches  class should be 0"
        classes.size() == 0

    }

    def " find Annotated #clazz"() {
        when: " get annotation "
        boolean hasAnnotation = clazz.getAnnotation(CacheNamespace.class)

        then: " classes isAssignableFrom should be true"
        hasAnnotation

        where: " classes are "
        clazz << new ResolverUtil<>().findAnnotated(CacheNamespace.class, this.getClass().getPackage().getName()).getClasses()

    }

    def " find #clazz"() {
        when: " findImplementations "
        boolean isAssignableFrom = VFS.class.isAssignableFrom(clazz)

        then: " classes isAssignableFrom should be true"
        isAssignableFrom

        where: " classes are "
        clazz << new ResolverUtil<VFS>().findImplementations(VFS.class, "org.apache.ibatis.io").getClasses()

    }

    def "get PackagePath #packageName"() {
        given: " a ResolverUtil "
        ResolverUtil resolverUtil = new ResolverUtil()

        when: " get  null package path "
        String packagePath = resolverUtil.getPackagePath(packageName)

        then: "packagePath  should be "
        packagePath == actual
        where: "packageName are"
        packageName            || actual
        "org.apache.ibatis.io" || "org/apache/ibatis/io"
        null                   || null
    }

    def "add IfMatching"(){
        given: " a ResolverUtil "
        ResolverUtil resolverUtil = new ResolverUtil()

        when: " invoke addIfMatching "
        resolverUtil.addIfMatching(new ResolverUtil.IsA(VFS.class),"org/apache/ibatis/io/DefaultVFS.class" )
        resolverUtil.addIfMatching(new ResolverUtil.IsA(VFS.class),"org/apache/ibatis/io/VFS.class" )

        and:" get classes "
        Set<Class<? extends VFS>> classSets = resolverUtil.getClasses();

        then: "match classes should be  2"
        classSets.size() == 2

        and:" class is isAssignableFrom"
        VFS.class.isAssignableFrom(classSets[0])
        VFS.class.isAssignableFrom(classSets[1])
    }



    @CacheNamespace(readWrite = false)
    private interface TestMapper {
        //test ResolverUtil.findAnnotated method
    }

}