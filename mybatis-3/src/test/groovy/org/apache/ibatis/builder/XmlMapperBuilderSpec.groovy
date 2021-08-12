package org.apache.ibatis.builder

import org.apache.ibatis.builder.xml.XMLMapperBuilder
import org.apache.ibatis.io.Resources
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.mapping.ResultSetType
import org.apache.ibatis.mapping.StatementType
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.type.TypeHandler
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import spock.lang.*

import java.sql.Connection
import java.util.regex.Pattern

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException
import static com.googlecode.catchexception.apis.BDDCatchException.when
import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.BDDAssertions.then

@Title("测试  XmlMapperBuilder ,解析注解")
@Narrative(""" 使用spock 重写   XmlMapperBuilderTest  , 解析 Mapper """)
@Subject(ParameterExpression)
@Unroll
class XmlMapperBuilderSpec extends Specification {

    def "should Success fully Load XML Mapper File"() {
        given: "resource  "
        String resource = "org/apache/ibatis/builder/AuthorMapper.xml";
        Configuration configuration = new Configuration();

        when: " parse resource"
        InputStream inputStream = Resources.getResourceAsStream(resource)
        XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        builder.parse();


        then: " mapper should be pared"

    }

    def "mapped Statement With Options"() {
        given: "resource  "
        String resource = "org/apache/ibatis/builder/AuthorMapper.xml";
        Configuration configuration = new Configuration();
        when: " parse resource "
        InputStream inputStream = Resources.getResourceAsStream(resource)
        XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        builder.parse();

        and: " get mappedStatement  "
        MappedStatement mappedStatement = configuration.getMappedStatement("selectWithOptions");

        then: " statement should be "
        mappedStatement.getFetchSize() == 200
        mappedStatement.getTimeout() == 10
        mappedStatement.getStatementType() == StatementType.PREPARED
        mappedStatement.getResultSetType() == ResultSetType.SCROLL_SENSITIVE
        !mappedStatement.isFlushCacheRequired()
        !mappedStatement.isUseCache()
    }


    def "mappedStatement Without Options When Specify Default Value"() {
        given: " resource"
        String resource = "org/apache/ibatis/builder/AuthorMapper.xml";
        Configuration configuration = new Configuration();
        configuration.setDefaultResultSetType(ResultSetType.SCROLL_INSENSITIVE);

        when: " parse resource"
        InputStream inputStream = Resources.getResourceAsStream(resource);
        XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        builder.parse();
        inputStream.close();

        and: " get selectAuthor "
        MappedStatement mappedStatement = configuration.getMappedStatement("selectAuthor");

        then: " result type should be "
        mappedStatement.getResultSetType() == ResultSetType.SCROLL_INSENSITIVE
    }

    def "parse Expression"() {
        given: "Configuration "
        Configuration configuration = new Configuration()
        BaseBuilder builder = new BaseBuilder(configuration) {}

        when: "parse Expression "
        Pattern pattern = builder.parseExpression("[0-9]", "[a-z]");

        then: "pattern should matcher "
        pattern.matcher("0").find()
        !pattern.matcher("a").find()

        when: "parse Expression "
        pattern = builder.parseExpression(null, "[a-z]");

        then: "pattern should matcher "
        !pattern.matcher("0").find()
        pattern.matcher("a").find()
    }

    def "resolveJdbcTypeWithUndefinedValue"() {
        given: "Configuration "
        Configuration configuration = new Configuration()
        BaseBuilder builder = new BaseBuilder(configuration) {}

        when: " resolveJdbcType "
        builder.resolveJdbcType("aaa")

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage().startsWith("Error resolving JdbcType. Cause: java.lang.IllegalArgumentException: No enum")
        caught.getMessage().endsWith("org.apache.ibatis.type.JdbcType.aaa")
    }

    def "resolve ResultSet Type With Undefined Value"() {
        given: "Configuration "
        Configuration configuration = new Configuration()
        BaseBuilder builder = new BaseBuilder(configuration) {}

        when: " resolveJdbcType "
        builder.resolveResultSetType("bbb")

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage().startsWith("Error resolving ResultSetType. Cause: java.lang.IllegalArgumentException: No enum")
        caught.getMessage().endsWith("org.apache.ibatis.mapping.ResultSetType.bbb")
    }

    def "resolve ParameterMode With Undefined Value"() {

        given: "Configuration "
        Configuration configuration = new Configuration()
        BaseBuilder builder = new BaseBuilder(configuration) {}

        when: " resolveJdbcType "
        builder.resolveParameterMode("ccc")

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage().startsWith("Error resolving ParameterMode. Cause: java.lang.IllegalArgumentException: No enum")
        caught.getMessage().endsWith("org.apache.ibatis.mapping.ParameterMode.ccc")
    }

    def "create Instance With Abstract Class"() {
        given: "Configuration "
        Configuration configuration = new Configuration()
        BaseBuilder builder = new BaseBuilder(configuration) {}

        when: "create instance "
        builder.createInstance("org.apache.ibatis.builder.BaseBuilder")

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage() == "Error creating instance. Cause: java.lang.NoSuchMethodException: org.apache.ibatis.builder.BaseBuilder.<init>()"
    }

    def "resolve Class With Not Found"() {
        given: "Configuration "
        Configuration configuration = new Configuration()
        BaseBuilder builder = new BaseBuilder(configuration) {}

        when: "  resolveClass"
        builder.resolveClass("ddd")


        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage() == "Error resolving class. Cause: org.apache.ibatis.type.TypeException: Could not resolve type alias 'ddd'.  Cause: java.lang.ClassNotFoundException: Cannot find class: ddd"

    }

    def "resolve TypeHandler TypeHandler Alias Is Null"() {
        given: "Configuration "
        Configuration configuration = new Configuration()
        BaseBuilder builder = new BaseBuilder(configuration) {}

        when: "resolveTypeHandler"
        TypeHandler<?> typeHandler = builder.resolveTypeHandler(String.class, (String) null);

        then: "type handler is null "
        null == typeHandler
    }

    def "resolve TypeHandler No Assignable"() {

        given: "Configuration "
        Configuration configuration = new Configuration()
        BaseBuilder builder = new BaseBuilder(configuration) {}

        when: "resolveTypeHandler"
        builder.resolveTypeHandler(String.class, "integer")

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage() == "Type java.lang.Integer is not a valid TypeHandler because it does not implement TypeHandler interface"

    }

    def "set Current Namespace Value Is Null"() {
        given: "builder"
        MapperBuilderAssistant builder = new MapperBuilderAssistant(new Configuration(), "resource");

        when: " setCurrentNamespace "
        builder.setCurrentNamespace(null)

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage() == "The mapper element requires a namespace attribute to be specified."
    }

    def "use CacheRef Namespace Is Null"() {
        given: "MapperBuilderAssistant "
        MapperBuilderAssistant builder = new MapperBuilderAssistant(new Configuration(), "resource");

        when: "useCacheRef"
        builder.useCacheRef(null)

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage() == "cache-ref element requires a namespace attribute."
    }

    def "use Cache RefNamespace Is Undefined"() {
        given: "MapperBuilderAssistant "
        MapperBuilderAssistant builder = new MapperBuilderAssistant(new Configuration(), "resource");

        when: "useCacheRef"
        builder.useCacheRef("eee")

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage() == "No cache for namespace 'eee' could be found."

    }

    def "should Failed Load XML Mapper File"() {
        given: " resource "
        String resource = "org/apache/ibatis/builder/ProblemMapper.xml";
        Configuration configuration = new Configuration();

        when: " create XMLMapperBuilder "
        InputStream inputStream = Resources.getResourceAsStream(resource)
        XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        builder.parse()


        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)
        caught.getMessage().contains("Error parsing Mapper XML. The XML location is 'org/apache/ibatis/builder/ProblemMapper.xml'")
    }


    def "erorr Result Map Location"() {
        given: " resource "
        String resource = "org/apache/ibatis/builder/ProblemResultMapper.xml";
        Configuration configuration = new Configuration();

        when: " create XMLMapperBuilder "
        InputStream inputStream = Resources.getResourceAsStream(resource)
        XMLMapperBuilder builder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        builder.parse();

        and: " get findProblemTypeTest "
        configuration.getMappedStatement("findProblemTypeTest")

        then: " caught BuilderException "
        def caught = thrown(BuilderException.class)

    }
}