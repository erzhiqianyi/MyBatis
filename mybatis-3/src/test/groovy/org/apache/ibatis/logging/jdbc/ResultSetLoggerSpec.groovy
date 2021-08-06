package org.apache.ibatis.logging.jdbc

import org.apache.ibatis.logging.Log
import spock.lang.*

import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Types

@Title("测试 ResultSetLogger ")
@Narrative(""" 用Spock 重写  ResultSetLoggerTest  """)
@Subject(ResultSetLogger)
@Unroll
class ResultSetLoggerSpec extends Specification {

    private ResultSet rs;

    private Log log;

    private ResultSetMetaData metaData;

    def init(int type) {
        rs = Mock()
        log = Mock()
        metaData = Mock()
        rs.next() >> true
        rs.getMetaData() >> metaData
        metaData.getColumnCount() >> 1
        metaData.getColumnType(1) >> type
        metaData.getColumnLabel(1) >> "ColumnName"
        log.isTraceEnabled() >> true
        ResultSet resultSet = ResultSetLogger.newInstance(rs, log, 1);
        resultSet.next()
    }

    def "should Not PrintBlobs"() {
        when: " init LONGNVARCHAR "
        init(Types.LONGNVARCHAR)

        then: "Blobs should not be print "
        0 * log.trace("<==    Columns: ColumnName")
        0 * log.trace("<==        Row: <<BLOB>>")
    }

}