package org.apache.ibatis.type

import spock.lang.Narrative
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import java.sql.Array
import java.sql.Connection
import java.sql.Types

@Title("测试ArrayTypeHandler")
@Narrative("""测试 ArrayTypeHandler""")
@Subject(ArrayTypeHandler)
@Unroll
class ArrayTypeHandlerSpec extends BaseTypeHandlerSpec {

    private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();

    def "should Set Parameter"() {
        given: " a mock array "
        Array mockArray = Mock()

        when: " use typeHandler set Parameter  "
        TYPE_HANDLER.setParameter(ps, 1, mockArray, null);

        then: " ps setArray should be invoke"
        1 * ps.setArray(1, mockArray)
    }


    def "should Set String Array Parameter"() {
        given: " a mock array "
        Array mockArray = Mock(Array.class)

        and: " stub connection  "
        Connection connection = Stub(Connection.class)

        and: "stub connection.createArrayOf() return mockArray"
        String[] param = ["Hello"]
        connection.createArrayOf("VARCHAR", param) >> mockArray

        and: "stub ps.getConnection() return stub connection"
        ps.getConnection() >> connection

        when: " use typeHandler set Parameter  "
        TYPE_HANDLER.setParameter(ps, 1, param, JdbcType.ARRAY);

        then: " ps setArray should be invoke"
        1 * ps.setArray(1, mockArray)
        1 * mockArray.free()
    }

    def "should Set Null Parameter "() {
        when: " use typeHandler set Parameter with a null param  "
        TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.ARRAY);

        then: " ps setArray should be invoke"
        1 * ps.setNull(1, Types.ARRAY)
    }

    def "should Fail For Non Array Parameter"() {
        when: " use typeHandler set Parameter with a unsupported parameter type  "
        TYPE_HANDLER.setParameter(ps, 1, null, null);

        then: " ps setArray should be invoke"
        thrown(TypeException)

    }

    def "should GetResult From ResultSet ByName"() {
        given: " a mock array "
        Array mockArray = Mock()

        and: "mock getArray() return stringArray"
        String[] stringArray = ["a", "b"]
        mockArray.getArray() >> stringArray

        and: "mock rs.getArray() return mockArray"
        rs.getArray("column") >> mockArray

        when: "get result from tpe handler  "
        String[] result = TYPE_HANDLER.getResult(rs, "column")

        then: " result should be stringArray"
        result == stringArray

        and: " mockArray is free"
        1 * mockArray.free()

    }


    def "should GetResult Null From ResultSet ByName"() {
        given: " a mock array "
        Array mockArray = Mock()


        and: "mock getArray() return null"
        String[] stringArray = ["a", "b"]
        mockArray.getArray() >> null

        and: "mock rs.getArray() return mockArray"
        rs.getArray("column") >> mockArray

        when: "get result from tpe handler  "
        Object result = TYPE_HANDLER.getResult(rs, "column")

        then: " result should be stringArray"
        null == result


    }

    def "should GetResult From ResultSet ByPosition"() {
        given: " a mock array "
        Array mockArray = Mock()

        and: "mock getArray() return stringArray"
        String[] stringArray = ["a", "b"]
        mockArray.getArray() >> stringArray

        and: "mock rs.getArray() return mockArray"
        rs.getArray(1) >> mockArray

        when: "get result from tpe handler  "
        String[] result = TYPE_HANDLER.getResult(rs, 1)

        then: " result should be stringArray"
        result == stringArray

    }

    def "should GetResult Null From ResultSet ByPosition"() {
        given: " a mock array "
        Array mockArray = Mock()

        and: "mock getArray() return null"
        mockArray.getArray() >> null

        and: "mock rs.getArray() return mockArray"
        rs.getArray(1) >> mockArray

        when: "get result from tpe handler  "
        String[] result = TYPE_HANDLER.getResult(rs, 1)

        then: " result should be stringArray"
        result == null

    }

    def "should GetResult From CallableStatement"() {
        given: " a mock array "
        Array mockArray = Mock()

        and: "mock getArray() return stringArray"
        String[] stringArray = ["a", "b"]
        mockArray.getArray() >> stringArray

        and: "mock rs.getArray() return mockArray"
        cs.getArray(1) >> mockArray

        when: "get result from tpe handler  "
        String[] result = TYPE_HANDLER.getResult(cs, 1)

        then: " result should be stringArray"
        result == stringArray


    }

    def "should GetResult Null From CallableStatement"(){

        given: " a mock array "
        Array mockArray = Mock()

        and: "mock getArray() return null"
        mockArray.getArray() >> null

        and: "mock rs.getArray() return mockArray"
        cs.getArray(1) >> mockArray

        when: "get result from tpe handler  "
        String[] result = TYPE_HANDLER.getResult(cs, 1)

        then: " result should be stringArray"
        result == null

    }



}