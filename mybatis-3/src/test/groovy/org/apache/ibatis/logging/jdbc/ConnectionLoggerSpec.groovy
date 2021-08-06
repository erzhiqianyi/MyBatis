package org.apache.ibatis.logging.jdbc

import org.apache.ibatis.logging.Log
import spock.lang.*

import java.sql.Connection

@Title("测试 ConnectionLogger")
@Narrative(""" 用Spock 重写  ConnectionLoggerTest """)
@Subject(ConnectionLogger)
@Unroll
class ConnectionLoggerSpec extends Specification {
    def "should Print Prepare Statement"() {
        given: " a mock Connection  "
        Connection connection = Mock()

        and: " a mock Log"
        Log log = Mock()

        and: " a connection instance from ConnectionLogger"
        Connection conn = ConnectionLogger.newInstance(connection, log, 1)

        and: " log enable debug"
        log.isDebugEnabled() >> true

        when: " prepare  statement"
        conn.prepareStatement("select 1")

        then: " log.debug should be invoke"
        1 * log.debug("==>  Preparing: select 1")


    }

    def "should Print Prepare Call"() {
        given: " a mock Connection  "
        Connection connection = Mock()

        and: " a mock Log"
        Log log = Mock()

        and: " a connection instance from ConnectionLogger"
        Connection conn = ConnectionLogger.newInstance(connection, log, 1)

        and: " log enable debug"
        log.isDebugEnabled() >> true

        when: " prepare  statement"
        conn.prepareCall("{ call test() }")

        then: " log.debug should be invoke"
        1 * log.debug("==>  Preparing: { call test() }")

    }

    def " should No tPrint Create Statement "() {
        given: " a mock Connection  "
        Connection connection = Mock()

        and: " a mock Log"
        Log log = Mock()

        and: " a connection instance from ConnectionLogger"
        Connection conn = ConnectionLogger.newInstance(connection, log, 1)

        when: " prepare  statement"
        conn.createStatement()

        then: " log.debug should not invoke"
        0 * log.debug("")

    }


}