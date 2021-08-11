package org.apache.ibatis.builder


import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.session.Configuration
import spock.lang.*

@Title("测试  SqlSourceBuilder ,构建 sqlSource")
@Narrative(""" 使用spock 重写   SqlSourceBuilderTest ,构建 sqlSource  """)
@Subject(SqlSourceBuilder)
@Unroll
class SqlSourceBuilderSpec extends Specification {

    private Configuration configuration;
    private SqlSourceBuilder sqlSourceBuilder;
    private final String sqlFromXml = "\t\n\n  SELECT * \n        FROM user\n \t        WHERE user_id = 1\n\t  ";

    def setup() {
        configuration = new Configuration();
        sqlSourceBuilder = new SqlSourceBuilder(configuration);
    }

    def "Shrink White spaces In Sql Is False"() {
        when: " build SqlSource"
        SqlSource sqlSource = sqlSourceBuilder.parse(sqlFromXml, null, null);

        and: "get bound sql"
        BoundSql boundSql = sqlSource.getBoundSql(null);
        String actual = boundSql.getSql();

        then: "sql should be "
        actual == sqlFromXml
    }


    def "Shrink White spaces In Sql Is True"() {
        when: " build SqlSource"
        configuration.setShrinkWhitespacesInSql(true);
        SqlSource sqlSource = sqlSourceBuilder.parse(sqlFromXml, null, null);

        and: "get bound sql"
        BoundSql boundSql = sqlSource.getBoundSql(null);
        String actual = boundSql.getSql();

        then: "sql should be "
        actual == "SELECT * FROM user WHERE user_id = 1"

    }
}