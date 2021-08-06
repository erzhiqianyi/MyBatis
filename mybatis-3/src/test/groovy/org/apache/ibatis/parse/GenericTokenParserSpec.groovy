package org.apache.ibatis.parse

import org.apache.ibatis.parsing.GenericTokenParser
import org.apache.ibatis.parsing.GenericTokenParserTest
import org.apache.ibatis.parsing.TokenHandler
import org.apache.ibatis.reflection.MetaClass
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll


@Title("测试GenericTokenParser")
@Narrative(""" 使用spock 重写 GenericTokenParserTest 进行解析   """)
@Subject(MetaClass)
@Unroll
class GenericTokenParserSpec extends Specification {

    static class VariableTokenHandler implements TokenHandler {
        private Map<String, String> variables = new HashMap<>();

        VariableTokenHandler(Map<String, String> variables) {
            this.variables = variables;
        }

        @Override
        String handleToken(String content) {
            return variables.get(content);
        }
    }

    def "should Demonstrate Generic Token Replacement #original "() {
        given: "  GenericTokenParser "
        String openToken = '${'
        String closeToken = '}'
        GenericTokenParser parser = new GenericTokenParser(openToken, closeToken, new GenericTokenParserTest.VariableTokenHandler(new HashMap<String, String>() {
            {
                put("first_name", "James");
                put("initial", "T");
                put("last_name", "Kirk");
                put("var{with}brace", "Hiya");
                put("", "");
            }
        }))
        when: " parse"
        String parsed = parser.parse(original)

        then: " parsed value should be "
        parsed == target

        where: ' parse value are '
        original                                              | target
        '${first_name} ${initial} ${last_name} reporting.'    | 'James T Kirk reporting.'
        'Hello captain ${first_name} ${initial} ${last_name}' | 'Hello captain James T Kirk'
        '${first_name} ${initial} ${last_name}'               | 'James T Kirk'
        '${first_name}${initial}${last_name}'                 | 'JamesTKirk'
        '{}${first_name}${initial}${last_name}'               | '{}JamesTKirk'
        '}${first_name}${initial}${last_name}'                | '}JamesTKirk'
        '}${first_name}{{${initial}}}${last_name}'            | '}James{{T}}Kirk'
        '}${first_name}}${initial}{${last_name}'              | '}James}T{Kirk'
        '}${first_name}}${initial}{${last_name}'              | '}James}T{Kirk'
        '}${first_name}}${initial}{${last_name}{{}}'          | '}James}T{Kirk{{}}'
        '}${first_name}}${initial}{${last_name}{{}}${}'       | '}James}T{Kirk{{}}'
        '{$$something}${first_name}${initial}${last_name}'    | '{$$something}JamesTKirk'
        '${'                                                  | '${'
        '${\\\\}'                                             | '${\\\\}'
        '${var{with\\}brace}'                                 | 'Hiya'
        '${}'                                                 | ''
        '}'                                                   | '}'
        'Hello ${ this is a test.'                            | 'Hello ${ this is a test.'
        'Hello } this is a test.'                             | 'Hello } this is a test.'
        'Hello } ${ this is a test.'                          | 'Hello } ${ this is a test.'

    }


}