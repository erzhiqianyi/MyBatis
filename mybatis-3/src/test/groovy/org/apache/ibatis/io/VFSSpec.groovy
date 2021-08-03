package org.apache.ibatis.io

import org.apache.ibatis.reflection.MetaClass
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import java.lang.reflect.Method


@Title("测试VFS")
@Narrative(""" 使用spock 重写 VFSTest ,在多线程环境下获取VFS实例 """)
@Subject(MetaClass)
@Unroll
class VFSSpec extends Specification {

    def " get Instance Should Not Be Null "() {
        when: " get instance from VFS"
        VFS vfs = VFS.getInstance()
        then: " instance should be exists "
        vfs != null
    }

    def " get instance should not be null In multi thread Env"() {
        given: " thread count "
        int threadCount = 3

        and: "threads"
        Thread[] threads = new Thread[threadCount]
        InstanceGetterProcedure[] procedures = new InstanceGetterProcedure[threadCount];

        for (int i = 0; i < threadCount; i++) {
            String threadName = "Thread##" + i
            procedures[i] = new InstanceGetterProcedure()
            threads[i] = new Thread(properties[i], threadName)

        }
        when: " start thread"
        for (Thread thread : threads) {
            thread.start()
        }
        for (Thread thread : threads) {
            thread.join()
        }
        then: " all instance should be same"
        procedures[0].instanceGot == procedures[1].instanceGot
        procedures[1].instanceGot == procedures[2].instanceGot
        procedures[0].instanceGot == procedures[2].instanceGot
    }

    class InstanceGetterProcedure implements Runnable {
        volatile VFS instanceGot;

        @Override
        void run() {
            instanceGot = VFS.getInstance()
        }
    }

    def " get  method #methodName "() {
        when: " get method from VFS.getMethod"
        Method method = VFS.getMethod(VFS.class, methodName, type)
        boolean methodExists = null != method

        then: " method should exists "
        methodExists == exists

        where: " methodName are"
        methodName | type         || exists
        "list"     | String.class || true
        "listXxx"  | String.class || false
    }

    def "invoke method"() {
        when: " get vfs from VFS.invoke() "
        VFS vfs = VFS.invoke(VFS.class.getMethod("getInstance"),VFS.class)

        then:" vfs should exists"
        null != vfs

        when:  " invoke not exists method getInstance "
        VFS.invoke(VFS.class.getMethod("getInstance"), VFS.class, "unnecessaryArgument");

        then: " throw RuntimeException"
        thrown(RuntimeException.class)

        when: "invoke  Resources.getResourceAsProperties  on a not exists resource "
        VFS.invoke(Resources.class.getMethod("getResourceAsProperties", String.class), Resources.class, "invalidResource");

        then:" throw IOException"
        thrown(IOException)
    }
}