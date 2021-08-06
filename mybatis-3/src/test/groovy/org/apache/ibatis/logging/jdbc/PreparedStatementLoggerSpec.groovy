package org.apache.ibatis.logging.jdbc

import org.apache.ibatis.logging.Log
import org.apache.ibatis.type.JdbcType
import spock.lang.*

import java.sql.PreparedStatement
import java.sql.ResultSet

@Title("测试 PreparedStatementLogger ")
@Narrative(""" 用Spock 重写  PreparedStatementLoggerTest """)
@Subject(PreparedStatementLogger)
@Unroll
class PreparedStatementLoggerSpec extends Specification {

    def "should Print Parameters"() {
        given: " a mock Log  "
        Log log = Mock()

        and: "a mock  PreparedStatement  "
        PreparedStatement preparedStatement = Mock()

        and: " a mock  ResultSet "
        ResultSet resultSet = Mock()
        PreparedStatement ps = PreparedStatementLogger.newInstance(preparedStatement, log, 1);

        and: " log enable debug"
        log.isDebugEnabled() >> true

        and: "  preparedStatement.executeQuery() return  resultSet"
        preparedStatement.executeQuery("select 1 limit ?") >> resultSet

        when: " ps executeQuery "
        ps.setInt(1, 10)
        ResultSet rs = ps.executeQuery("select 1 limit ?")

        then: " log.debug should be invoke"
        1 * log.debug("==> Parameters: 10(Integer)")
        null != rs
        rs != resultSet

    }

    def "should Print  Null Parameters"() {
        given: " a mock Log  "
        Log log = Mock()

        and: "a mock  PreparedStatement  "
        PreparedStatement preparedStatement = Mock()

        and: " a mock  ResultSet "
        PreparedStatement ps = PreparedStatementLogger.newInstance(preparedStatement, log, 1);

        and: " log enable debug"
        log.isDebugEnabled() >> true

        and: "  preparedStatement.executeQuery() return  resultSet"
        preparedStatement.execute("update name = ? from test") >> true

        when: " ps execute "
        ps.setNull(1, JdbcType.VARCHAR.TYPE_CODE);
        Boolean result = ps.execute("update name = ? from test")

        then: " log.debug should be invoke"
        1 * log.debug("==> Parameters: null")
        result

    }


    def "should not printLog"() {
        given: " a mock Log  "
        Log log = Mock()

        and: "a mock  PreparedStatement  "
        PreparedStatement preparedStatement = Mock()

        and: " a mock  ResultSet "
        PreparedStatement ps = PreparedStatementLogger.newInstance(preparedStatement, log, 1);


        when: " get result set  "
        ps.getResultSet();
        ps.getParameterMetaData();

        then: " log.debug should be invoke"
        0 * log.debug()

    }


    def "should Print Update Count"() {
        given: " a mock Log  "
        Log log = Mock()

        and: "a mock  PreparedStatement  "
        PreparedStatement preparedStatement = Mock()

        and: " a  proxy PreparedStatement  "
        PreparedStatement ps = PreparedStatementLogger.newInstance(preparedStatement, log, 1);

        and: " log enable debug"
        log.isDebugEnabled() >> true

        and: "  preparedStatement.getUpdateCount() return  1"
        preparedStatement.getUpdateCount() >> 1

        when: " ps get update count "
        ps.getUpdateCount()

        then: " log.debug should be invoke"
        1 * log.debug("<==    Updates: 1")

    }


}