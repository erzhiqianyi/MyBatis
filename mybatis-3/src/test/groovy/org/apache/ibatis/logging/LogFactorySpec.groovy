package org.apache.ibatis.logging

import org.apache.ibatis.io.Resources
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl
import org.apache.ibatis.logging.log4j.Log4jImpl
import org.apache.ibatis.logging.log4j2.Log4j2Impl
import org.apache.ibatis.logging.nologging.NoLoggingImpl
import org.apache.ibatis.logging.slf4j.Slf4jImpl
import org.apache.ibatis.logging.stdout.StdOutImpl
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import spock.lang.*

@Title("测试LogFactory")
@Narrative(""" 用Spock 重写 LogFactoryTest  """)
@Subject(LogFactory)
@Unroll
class LogFactorySpec extends Specification {

    def " should Use target loger"() {
        given: " LogFactory  and  use commonsLogging"
        LogFactory.useCommonsLogging();

        when: "get Log"
        Log log = LogFactory.getLog(Object.class)

        and: " log something"
        logSomething(log)

        then:" log name should be "
        log.getClass().getName() ==  JakartaCommonsLoggingImpl.class.getName()


    }

    def " should Use Log4J"() {
        given: " LogFactory  and  use useLog4JLogging"
        LogFactory.useLog4JLogging();

        when: "get Log"
        Log log = LogFactory.getLog(Object.class)

        and: " log something"
        logSomething(log)

        then:" log name should be "
        log.getClass().getName() ==  Log4jImpl.class.getName()

    }

    def " should Use Log4J2"() {
        given: " LogFactory  and  use Log4J2Logging "
        LogFactory.useLog4J2Logging();

        when: "get Log"
        Log log = LogFactory.getLog(Object.class)

        and: " log something"
        logSomething(log)

        then:" log name should be "
        log.getClass().getName() ==  Log4j2Impl.class.getName()

    }

    def " should Use JdKLogging"() {
        given: " LogFactory  and  use JdkLogging "
        LogFactory.useJdkLogging();

        when: "get Log"
        Log log = LogFactory.getLog(Object.class)

        and: " log something"
        logSomething(log)

        then:" log name should be "
        log.getClass().getName() ==  Jdk14LoggingImpl.class.getName()

    }


    def " should Use  Slf4jLogging"() {
        given: " LogFactory  and  use useSlf4jLogging"
        LogFactory.useSlf4jLogging();

        when: "get Log"
        Log log = LogFactory.getLog(Object.class)

        and: " log something"
        logSomething(log)

        then:" log name should be "
        log.getClass().getName() ==  Slf4jImpl.class.getName()

    }

    def " should Use  UseStdOut"() {
        given: " LogFactory  and  use UseStdOut"
        LogFactory.useStdOutLogging();

        when: "get Log"
        Log log = LogFactory.getLog(Object.class)

        and: " log something"
        logSomething(log)

        then:" log name should be "
        log.getClass().getName() ==  StdOutImpl.class.getName()

    }


    def " should Use NoLogging  "() {
        given: " LogFactory  and  use NoLogging"
        LogFactory.useNoLogging();

        when: "get Log"
        Log log = LogFactory.getLog(Object.class)

        and: " log something"
        logSomething(log)

        then:" log name should be "
        log.getClass().getName() == NoLoggingImpl.class.getName()

    }

    def " should  Read Log Impl From Settings "() {

        given: " a config file "
        String config = "org/apache/ibatis/logging/mybatis-config.xml"

        and:" read config "
        Reader reader = Resources.getResourceAsReader(config)
        new SqlSessionFactoryBuilder().build(reader)

        when: "get Log"
        Log log = LogFactory.getLog(Object.class)

        and: " log something"
        logSomething(log)

        then:" log name should be "
        log.getClass().getName() == NoLoggingImpl.class.getName()

    }




    private void logSomething(Log log) {
        log.warn("Warning message.");
        log.debug("Debug message.");
        log.error("Error message.");
        log.error("Error with Exception.", new Exception("Test exception."));
    }

}