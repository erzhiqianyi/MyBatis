package org.apache.ibatis.logging.jdbc

import org.apache.ibatis.logging.Log
import spock.lang.*

import java.sql.Array

@Title("测试BaseJdbcLogger")
@Narrative(""" 用Spock 重写  BaseJdbcLogger """)
@Subject(BaseJdbcLogger)
@Unroll
class BaseJdbcLoggerSpec extends Specification {


    def "should Describe Primitive Array Parameter"() {
        given: " a mock log"
        Log log = Mock()

        and: " a stub Array "
        Array array = Mock()

        and: " a BaseJdbcLogger"
        BaseJdbcLogger logger = new BaseJdbcLogger(log, 1) {}
        and:" a int[] "
        int[] param = new int[3]
        param[0]  = 1
        param[1]  = 2
        param[2]  = 3

        and:" stub array.getArray() return "
        array.getArray() >> param

        when:" logger.setColumn()"
        logger.setColumn("1",array)

        then:"logger.getParameterValueString() should start with "
        logger.getParameterValueString().startsWith("[1, 2, 3]")

    }

    def "should Describe Ojbect Array Parameter"() {
        given: " a mock log"
        Log log = Mock()

        and: " a stub Array "
        Array array = Mock()

        and: " a BaseJdbcLogger"
        BaseJdbcLogger logger = new BaseJdbcLogger(log, 1) {}
        and:" a int[] "
        String[] param = new String[3]
        param[0]  = "one"
        param[1]  = "two"
        param[2]  = "three"

        and:" stub array.getArray() return "
        array.getArray() >> param

        when:" logger.setColumn()"
        logger.setColumn("1",array)

        then:"logger.getParameterValueString() should start with "
        logger.getParameterValueString().startsWith("[one, two, three]")

    }




}