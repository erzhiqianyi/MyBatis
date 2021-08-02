package org.apache.ibatis.type

import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll


@Title("测试 BigIntegerTypeHandler ")
@Narrative("""测试 BigIntegerTypeHandler  """)
@Subject(BigIntegerTypeHandler)
@Unroll
class BigIntegerTypeHandlerSpec extends BaseTypeHandlerSpec {

    private static final TypeHandler<BigInteger> TYPE_HANDLER = new BigIntegerTypeHandler();

    def "should Set Parameter"() {
        given: " a big Integer "
        BigInteger param = new BigInteger(70707)

        when: " use typeHandler set Parameter  "
        TYPE_HANDLER.setParameter(ps, 1, param, null);

        then: " ps setArray should be invoke"
        1 * ps.setBigDecimal(1,param)
    }


    def "should GetResult From ResultSet ByName"() {
        given: " a BigInteger "
        BigInteger bigDecimal = new BigInteger(70707)

        and: "mock rs.get() return bigDecimal "
        rs.getBigDecimal("column") >> bigDecimal

        when: "get result from tpe handler  "
        BigInteger result = TYPE_HANDLER.getResult(rs, "column")

        then: " result should be stringArray"
        result == bigDecimal

    }


    def "should GetResult Null From ResultSet ByName"() {
        // Unnecessary
    }

    def "should GetResult From ResultSet ByPosition"() {
        given: " a BidDecimal "
        BigInteger bigDecimal = new BigInteger(70707)

        and: "mock rs.getBigDecimal() return bigDecimal "
        rs.getBigDecimal(1) >> bigDecimal

        when: "get result from tpe handler  "
        BigDecimal result = TYPE_HANDLER.getResult(rs, 1)

        then: " result should be stringArray"
        result == bigDecimal   }

    def "should GetResult Null From ResultSet ByPosition"() {
        // Unnecessary
    }

    def "should GetResult From CallableStatement"() {
        given: " a BidDecimal "
        BigDecimal bigDecimal = new BigDecimal(1)

        and: "mock rs.getBigDecimal() return bigDecimal "
        cs.getBigDecimal(1) >> bigDecimal

        when: "get result from tpe handler  "
        BigDecimal result = TYPE_HANDLER.getResult(cs,1)

        then: " result should be stringArray"
        result == bigDecimal

    }

    def "should GetResult Null From CallableStatement"(){

        // Unnecessary
    }

}