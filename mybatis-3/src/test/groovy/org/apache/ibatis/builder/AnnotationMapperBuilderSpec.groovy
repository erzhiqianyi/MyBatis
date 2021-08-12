package org.apache.ibatis.builder

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.mapping.ResultSetType
import org.apache.ibatis.mapping.StatementType
import org.apache.ibatis.session.Configuration
import spock.lang.*

@Title("测试   AnnotationMapperBuilder   ,解析注解")
@Narrative(""" 使用spock 重写   AnnotationMapperBuilderTest    ,解析注解 """)
@Subject(ParameterExpression)
@Unroll
class AnnotationMapperBuilderSpec extends Specification {

    def "with Options"() {
        given: "Configuration "
        Configuration configuration = new Configuration();
        MapperAnnotationBuilder builder = new MapperAnnotationBuilder(configuration, Mapper.class);
        builder.parse();

        when: " get  selectWithOptions  "
        MappedStatement mappedStatement = configuration.getMappedStatement("selectWithOptions");

        then: " mappedStatement  should be "
        mappedStatement.getFetchSize() == 200
        mappedStatement.getTimeout() == 10
        mappedStatement.getStatementType() == StatementType.STATEMENT
        mappedStatement.getResultSetType() == ResultSetType.SCROLL_INSENSITIVE
        mappedStatement.isFlushCacheRequired()
        !mappedStatement.isUseCache()
        mappedStatement.getResultSets().contains("resultSets")

        when: " get  insertWithOptions "
        mappedStatement = configuration.getMappedStatement("insertWithOptions");

        then: "mappedStatement  should be  "
        mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator
        mappedStatement.getKeyColumns().contains("key_column")
        mappedStatement.getKeyProperties().contains("keyProperty");
    }

    def "with Options And Without Options Attributes When Specify DefaultValue"() {
        given: "Configuration "
        Configuration configuration = new Configuration();
        configuration.setDefaultResultSetType(ResultSetType.SCROLL_INSENSITIVE);
        MapperAnnotationBuilder builder = new MapperAnnotationBuilder(configuration, AnnotationMapperBuilderTest.Mapper.class);
        builder.parse();

        when: "get selectWithOptionsAndWithoutOptionsAttributes "
        MappedStatement mappedStatement = configuration.getMappedStatement("selectWithOptionsAndWithoutOptionsAttributes");

        then: "statement result set type should be "
        mappedStatement.getResultSetType() == ResultSetType.SCROLL_INSENSITIVE
    }


    def "with Options And Without Options Attributes When Not Specify Default Value"() {
        given: "Configuration "
        Configuration configuration = new Configuration();
        MapperAnnotationBuilder builder = new MapperAnnotationBuilder(configuration, AnnotationMapperBuilderTest.Mapper.class);
        builder.parse();

        when: "get  selectWithOptionsAndWithoutOptionsAttributes"
        MappedStatement mappedStatement = configuration.getMappedStatement("selectWithOptionsAndWithoutOptionsAttributes");

        then: " result set type should be "
        mappedStatement.getResultSetType() == ResultSetType.DEFAULT
    }

    def "without Options When Specify Default Value"() {
        given: "Configuration "
        Configuration configuration = new Configuration();
        configuration.setDefaultResultSetType(ResultSetType.SCROLL_INSENSITIVE);
        MapperAnnotationBuilder builder = new MapperAnnotationBuilder(configuration, AnnotationMapperBuilderTest.Mapper.class);
        builder.parse();

        when: "get selectWithoutOptions "
        MappedStatement mappedStatement = configuration.getMappedStatement("selectWithoutOptions");

        then: " result set type should be "
        mappedStatement.getResultSetType() == ResultSetType.SCROLL_INSENSITIVE
    }

    def "without Options When Not Specify Default Value"() {
        given: "Configuration "
        Configuration configuration = new Configuration();
        MapperAnnotationBuilder builder = new MapperAnnotationBuilder(configuration, AnnotationMapperBuilderTest.Mapper.class);
        builder.parse();

        when: "get selectWithoutOptions "
        MappedStatement mappedStatement = configuration.getMappedStatement("selectWithoutOptions");

        then: " result set type should be "
        mappedStatement.getResultSetType() == ResultSetType.DEFAULT
    }

    interface Mapper {

        @Insert("insert into test (name) values(#{name})")
        @Options(useGeneratedKeys = true, keyColumn = "key_column", keyProperty = "keyProperty")
        void insertWithOptions(String name);

        @Select("select * from test")
        @Options(fetchSize = 200, timeout = 10, statementType = StatementType.STATEMENT, resultSetType = ResultSetType.SCROLL_INSENSITIVE, flushCache = Options.FlushCachePolicy.TRUE, useCache = false, resultSets = "resultSets")
        String selectWithOptions(Integer id);

        @Select("select * from test")
        @Options
        String selectWithOptionsAndWithoutOptionsAttributes(Integer id);

        @Select("select * from test")
        String selectWithoutOptions(Integer id);

    }
}