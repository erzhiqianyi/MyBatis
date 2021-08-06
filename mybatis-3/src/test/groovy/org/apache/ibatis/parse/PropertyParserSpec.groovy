package org.apache.ibatis.parse

import org.apache.ibatis.parsing.PropertyParser
import org.apache.ibatis.reflection.MetaClass
import spock.lang.*

@Title("测试 GenericTokenParser")
@Narrative(""" 使用spock 重写 GenericTokenParserTest 进行属性解析  """)
@Subject(MetaClass)
@Unroll
class PropertyParserSpec extends Specification {

    def "replace To Variable Value #original"() {
        given: " Properties  "
        Properties props = new Properties();
        props.setProperty(PropertyParser.KEY_ENABLE_DEFAULT_VALUE, "true");
        props.setProperty("key", "value");
        props.setProperty("tableName", "members");
        props.setProperty("orderColumn", "member_id");
        props.setProperty("a:b", "c");

        when: "parse value"
        String parsedValue = PropertyParser.parse(original, props)

        then: " parsed value should be"
        parsedValue == target

        when: " set  KEY_ENABLE_DEFAULT_VALUE  to false"
        props.setProperty(PropertyParser.KEY_ENABLE_DEFAULT_VALUE, "false");

        and: 'parse  ${a:b} '
        String value = PropertyParser.parse('${a:b}', props)

        then: " value should be c "
        value == 'c'

        where: ' parse value are '
        original                                                      | target
        '${key}'                                                      | 'value'
        '${key:aaaa}'                                                 | 'value'
        'SELECT * FROM ${tableName:users} ORDER BY ${orderColumn:id}' | 'SELECT * FROM members ORDER BY member_id'
    }

    def " not Replace #original"() {
        given: " Properties  "
        Properties props = new Properties();
        props.setProperty(PropertyParser.KEY_ENABLE_DEFAULT_VALUE, "true");

        when: " parse value"
        String parsedValue = PropertyParser.parse(original, props)

        then: "parsed value should be"
        parsedValue == target

        when: " parse value with null  Properties "
        parsedValue = PropertyParser.parse(original, null)

        then: " parse value should be"
        parsedValue == target

        where: ' parse value are '
        original | target
        '${key}' | '${key}'

    }

    def " not Replace with default value #original"() {
        given: " Properties  "
        Properties props = new Properties();
        props.setProperty(PropertyParser.KEY_ENABLE_DEFAULT_VALUE, "false");

        when: " parse value"
        String parsedValue = PropertyParser.parse(original, props)

        then: "parsed value should be"
        parsedValue == target

        when: " parse value with null  Properties "
        parsedValue = PropertyParser.parse(original, null)

        then: " parse value should be"
        parsedValue == target

        when: " remove enable default value"
        props.remove(PropertyParser.KEY_ENABLE_DEFAULT_VALUE);

        and: "parse value"
        parsedValue = PropertyParser.parse(original, props)

        then: "  parse value should be"
        parsedValue == target
        where: ' parse value are '
        original | target
        '${a:b}' | '${a:b}'

    }

    def "apply Default Value #original"() {
        given: " Properties  "
        Properties props = new Properties();
        props.setProperty(PropertyParser.KEY_ENABLE_DEFAULT_VALUE, "true");


        when: " parse value"
        String parsedValue = PropertyParser.parse(original, props)

        then: "parsed value should be"
        parsedValue == target
        where: ' parse value are '
        original                                                      | target
        '${key:default}'                                              | 'default'
        'SELECT * FROM ${tableName:users} ORDER BY ${orderColumn:id}' | 'SELECT * FROM users ORDER BY id'
        '${key:}'                                                     | ''
        '${key: }'                                                    | ' '
        '${key::}'                                                    | ':'

    }


    def "apply Custom Separator #original"() {

        given: " Properties  "
        Properties props = new Properties();
        props.setProperty(PropertyParser.KEY_ENABLE_DEFAULT_VALUE, "true");
        props.setProperty(PropertyParser.KEY_DEFAULT_VALUE_SEPARATOR, "?:");

        when: " parse value"
        String parsedValue = PropertyParser.parse(original, props)

        then: "parsed value should be"
        parsedValue == target

        where: ' parse value are '
        original                                                      | target
        '${key?:default}'                                              | 'default'
        'SELECT * FROM ${tableName?:users} ORDER BY ${orderColumn?:id}' | 'SELECT * FROM users ORDER BY id'
        '${key?:}'                                                     | ''
        '${key?: }'                                                    | ' '
        '${key?::}'                                                    | ':'

    }
}