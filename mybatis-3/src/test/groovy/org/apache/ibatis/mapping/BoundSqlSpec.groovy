package org.apache.ibatis.mapping

import org.apache.ibatis.session.Configuration
import spock.lang.*

@Title("测试 BoundSql ")
@Narrative(""" 用Spock 重写  BoundSqlTest ,boundSql 参数设置鹤读取   """)
@Subject(BoundSql)
@Unroll
class BoundSqlSpec extends Specification {


    def "Has Additional Parameter"() {
        given: " BoundSql "
        List<ParameterMapping> params = Collections.emptyList();
        BoundSql boundSql = new BoundSql(new Configuration(), "some sql", params, new Object());

        when: " add map param"
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        boundSql.setAdditionalParameter("map", map);

        then: "has map param "
        boundSql.hasAdditionalParameter("map")
        boundSql.hasAdditionalParameter("map.key1")
        boundSql.hasAdditionalParameter("map.key2")
        
        when: " add bean param"
        Person bean = new Person();
        bean.id = 1;
        boundSql.setAdditionalParameter("person", bean)

        then: "has person bean param "
        boundSql.hasAdditionalParameter("person")
        boundSql.hasAdditionalParameter("person.id")
        boundSql.hasAdditionalParameter("person.name")

        and:" not have pet  "
        !boundSql.hasAdditionalParameter("pet")
        !boundSql.hasAdditionalParameter("pet.name")
        
        when:" add array param "
        String[] array = ["User1", "User2"]
        boundSql.setAdditionalParameter("array", array);

        then:" has array param"
        boundSql.hasAdditionalParameter("array[0]")
        boundSql.hasAdditionalParameter("array[99]")

        



    }

    static class Person {
        public Integer id;
    }

}