package org.apache.ibatis.parse

import org.apache.ibatis.io.Resources
import org.apache.ibatis.parsing.XPathParser
import org.apache.ibatis.reflection.MetaClass
import spock.lang.*

@Title("测试 XPathParser")
@Narrative(""" 使用spock 重写 XPathParserSpec  ,解析XML文件  """)
@Subject(MetaClass)
@Unroll
class XPathParserSpec extends Specification {


    private String resource = "resources/nodelet_test.xml";

    def "constructor With InputStream Validation Variables Entity Resolver"() {
        given: " Parser from resource"
        InputStream inputStream = Resources.getResourceAsStream(resource)
        XPathParser parser = new XPathParser(inputStream, false, null, null);


        when: "eval property "
        Long yearOfBirthdate = parser.evalLong("/employee/birth_date/year")
        short monthOfBirthdate = parser.evalShort("/employee/birth_date/month")
        Integer dayOfBirthdate = parser.evalInteger("/employee/birth_date/day")
        Float height = parser.evalFloat("/employee/height")
        String id = parser.evalString("/employee/@id")

        then: "  value  should be "
        yearOfBirthdate == 1970L
        monthOfBirthdate == 6
        dayOfBirthdate == 15
        height == 5.8f
        id == '${id_var}'


    }


}