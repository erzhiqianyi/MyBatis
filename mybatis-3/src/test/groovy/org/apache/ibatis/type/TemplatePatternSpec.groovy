package org.apache.ibatis.type


import org.apache.ibatis.reflection.MetaClass
import spock.lang.*

@Title("演示模板模式")
@Narrative(""" 演示模式模式，定义一套打扫卫生的模板，打扫卫生流程为：准备，实施，善后和汇报""")
@Subject(MetaClass)
@Unroll
class TemplatePatternSpec extends Specification {

    def " wipe glass "() {
        given: " a Cleaning"
        Cleaning cleaning = new WipeGlass()

        when: "clean "
        cleaning.clean()

        then: "glass should be wipe  "
    }

    def "wipe blackboard"() {
        given: " a Cleaning"
        Cleaning cleaning = new WipeBlackBoard()

        when: "clean  "
        cleaning.clean()

        then: "glass should be wipe  "

    }

    abstract class Cleaning {
        void clean() {
            prepare()
            implement()
            windup()
            report()
        }

        abstract void prepare()

        abstract void implement()

        abstract void windup()

        void report() {
            System.out.println("告诉别人已经打扫完成")
        }

    }

    class WipeGlass extends Cleaning {

        @Override
        void prepare() {
            System.out.println("找到抹布")
            System.out.println("清洗抹布")
        }

        @Override
        void implement() {
            System.out.println("擦玻璃")
        }

        @Override
        void windup() {
            System.out.println("清理窗台")
        }
    }

    class WipeBlackBoard extends Cleaning {

        @Override
        void prepare() {
            System.out.println("找到黑板檫")
        }

        @Override
        void implement() {
            System.out.println("用力擦黑板")
        }

        @Override
        void windup() {
            System.out.println("清理粉笔灰")
        }
    }

}