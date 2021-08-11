package org.apache.ibatis.builder


import spock.lang.*

@Title("测试   ParameterExpression ,解析键值对信息")
@Narrative(""" 使用spock 重写  ParameterExpressionTest   ,将参数解析成键值对  """)
@Subject(ParameterExpression)
@Unroll
class ParameterExpressionSpec extends Specification {

    def "simple Property"() {
        when: " create ParameterExpression"
        Map<String, String> result = new ParameterExpression("id");

        then: "  result size should be "
        result.size() == 1

        and: " property is id "
        result.get("property") == "id"
    }

    def "property With Spaces Inside"() {
        when: " create ParameterExpression with spaces "
        Map<String, String> result = new ParameterExpression(" with spaces ");

        then: "  result size should be "
        result.size() == 1

        and: " property is with spaces"
        result.get("property") == "with spaces"
    }

    def "simple Property With Old Style Jdbc Type"() {
        when: " create ParameterExpression with spaces "
        Map<String, String> result = new ParameterExpression("id:VARCHAR");

        then: "  result size should be "
        result.size() == 2

        and: " property is id "
        result.get("property") == "id"

        and: " jdbctype is VARCHAR"
        result.get("jdbcType") == "VARCHAR"
    }

    def "old Style JdbcType WithExtra White spaces"() {
        when: " create ParameterExpression with spaces "
        Map<String, String> result = new ParameterExpression(" id : VARCHAR ");

        then: "  result size should be "
        result.size() == 2

        and: " property is id "
        result.get("property") == "id"

        and: " jdbctype is VARCHAR"
        result.get("jdbcType") == "VARCHAR"
    }

    def "expression With Old Style JdbcType"() {

        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("(id.toString()):VARCHAR");

        then: "  result size should be "
        result.size() == 2

        and: " expression is  id.toString() "
        result.get("expression") == "id.toString()"

        and: " jdbctype is VARCHAR"
        result.get("jdbcType") == "VARCHAR"

    }

    def "simple Property With One Attribute"() {

        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("id,name=value");

        then: "  result size should be "
        result.size() == 2

        and: " property is id "
        result.get("property") == "id"

        and: " name is value"
        result.get("name") == "value"
    }

    def "expression With One Attribute"() {
        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("(id.toString()),name=value");

        then: "  result size should be "
        result.size() == 2

        and: " expression is id.toString() "
        result.get("expression") == "id.toString()"

        and: " name is value"
        result.get("name") == "value"
    }

    def " simple Property With Many Attributes"() {
        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("id, attr1=val1, attr2=val2, attr3=val3");

        then: "  result size should be "
        result.size() == 4

        and: " property is id "
        result.get("property") == "id"

        and: " attrs  are "
        result.get("attr1") == "val1"
        result.get("attr2") == "val2"
        result.get("attr3") == "val3"

    }

    def "expression With Many Attributes"() {
        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("(id.toString()), attr1=val1, attr2=val2, attr3=val3");

        then: "  result size should be "
        result.size() == 4

        and: " expression is id.toString() "
        result.get("expression") == "id.toString()"

        and: " attrs  are "
        result.get("attr1") == "val1"
        result.get("attr2") == "val2"
        result.get("attr3") == "val3"

    }

    def "simple Property With Old Style JdbcType And Attributes"() {
        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("id:VARCHAR, attr1=val1, attr2=val2")

        then: "  result size should be "
        result.size() == 4

        and: " property is id "
        result.get("property") == "id"

        and: "jdbc type is"
        result.get("jdbcType") == "VARCHAR"

        and: " attrs  are "
        result.get("attr1") == "val1"
        result.get("attr2") == "val2"

    }

    def "simple Property With Space And Many Attributes"() {
        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("user name, attr1=val1, attr2=val2, attr3=val3");

        then: "  result size should be "
        result.size() == 4

        and: " property is user name"
        result.get("property") == "user name"

        and: " attrs  are "
        result.get("attr1") == "val1"
        result.get("attr2") == "val2"
        result.get("attr3") == "val3"

    }

    def "should Ignore Leading And Trailing Spaces"() {
        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression(" id , jdbcType =  VARCHAR,  attr1 = val1 ,  attr2 = val2 ");

        then: "  result size should be "
        result.size() == 4

        and: " property is id"
        result.get("property") == "id"

        and: "jdbc type is"
        result.get("jdbcType") == "VARCHAR"

        and: " attrs  are "
        result.get("attr1") == "val1"
        result.get("attr2") == "val2"

    }

    def "invalid Old JdbcType Format"() {
        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("id:");

        then: " caught  BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage().contains("Parsing error in {id:} in position 3")
    }

    def "invalid JdbcType Opt Using Expression"() {
        when: " create ParameterExpression  "
        Map<String, String> result = new ParameterExpression("(expression)+");

        then: " caught  BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage().contains("Parsing error in {(expression)+} in position 12")
    }

}