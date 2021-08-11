package org.apache.ibatis.builder

import org.apache.ibatis.builder.mapper.CustomMapper
import org.apache.ibatis.builder.typehandler.CustomIntegerTypeHandler
import org.apache.ibatis.builder.xml.XMLConfigBuilder
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource
import org.apache.ibatis.domain.blog.Author
import org.apache.ibatis.domain.blog.Blog
import org.apache.ibatis.domain.blog.mappers.BlogMapper
import org.apache.ibatis.domain.blog.mappers.NestedBlogMapper
import org.apache.ibatis.domain.jpetstore.Cart
import org.apache.ibatis.executor.loader.cglib.CglibProxyFactory
import org.apache.ibatis.executor.loader.javassist.JavassistProxyFactory
import org.apache.ibatis.io.JBoss6VFS
import org.apache.ibatis.io.Resources
import org.apache.ibatis.logging.slf4j.Slf4jImpl
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.mapping.ResultSetType
import org.apache.ibatis.scripting.defaults.RawLanguageDriver
import org.apache.ibatis.session.*
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.apache.ibatis.type.*
import org.junit.jupiter.api.Test
import spock.lang.*

import java.math.RoundingMode
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException
import static com.googlecode.catchexception.apis.BDDCatchException.when
import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.BDDAssertions.then
import static org.junit.jupiter.api.Assertions.assertArrayEquals
import static org.junit.jupiter.api.Assertions.assertTrue

@Title("测试配置文件解析 ")
@Narrative(""" 使用spock 重写  XmlConfigBuilderTest  解析配置文件  """)
@Subject(XMLConfigBuilder)
@Unroll
class XmlConfigBuilderSpec extends Specification {

    def "should Successfully Load Minimal XML ConfigFiles"() {
        given: " resource"
        String resource = "org/apache/ibatis/builder/MinimalMapperConfig.xml";

        when: " read resource  as stream "
        InputStream inputStream = Resources.getResourceAsStream(resource)

        and: " create builder from stream"
        XMLConfigBuilder builder = new XMLConfigBuilder(inputStream);

        and: "build configuration"
        Configuration config = builder.parse();

        then: " config should be default setting "
        null != config
        config.getAutoMappingBehavior() == AutoMappingBehavior.PARTIAL
        config.getAutoMappingUnknownColumnBehavior() == AutoMappingUnknownColumnBehavior.NONE
        config.isCacheEnabled()
        config.getProxyFactory() instanceof JavassistProxyFactory
        !config.isLazyLoadingEnabled()
        !config.isAggressiveLazyLoading()
        config.isMultipleResultSetsEnabled()
        config.isUseColumnLabel()
        !config.isUseGeneratedKeys()
        config.getDefaultExecutorType() == ExecutorType.SIMPLE
        null == config.getDefaultStatementTimeout()
        null == config.getDefaultFetchSize()
        null == config.getDefaultResultSetType()
        !config.isMapUnderscoreToCamelCase()
        !config.isSafeRowBoundsEnabled()
        config.getLocalCacheScope() == LocalCacheScope.SESSION
        config.getJdbcTypeForNull() == JdbcType.OTHER
        config.getLazyLoadTriggerMethods() == new HashSet<>(Arrays.asList("equals", "clone", "hashCode", "toString"))
        config.isSafeResultHandlerEnabled()
        !config.isCallSettersOnNulls()
        null == config.getLogPrefix()
        null == config.getLogImpl()
        null == config.getConfigurationFactory()
        config.getTypeHandlerRegistry().getTypeHandler(RoundingMode.class) instanceof EnumTypeHandler
        !config.isShrinkWhitespacesInSql()
        null == config.getDefaultSqlProviderType()

    }

    enum MyEnum {
        ONE, TWO
    }

    static class EnumOrderTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

        private E[] constants;

        EnumOrderTypeHandler(Class<E> javaType) {
            constants = javaType.getEnumConstants();
        }

        void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
            ps.setInt(i, parameter.ordinal() + 1); // 0 means NULL so add +1
        }

        @Override
        E getNullableResult(ResultSet rs, String columnName) throws SQLException {
            int index = rs.getInt(columnName) - 1;
            return index < 0 ? null : constants[index];
        }

        @Override
        E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            int index = rs.getInt(rs.getInt(columnIndex)) - 1;
            return index < 0 ? null : constants[index];
        }

        @Override
        E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            int index = cs.getInt(columnIndex) - 1;
            return index < 0 ? null : constants[index];
        }
    }


    def "register Java Type Initializing Type Handler"() {
        given: "xml config "
        String MAPPER_CONFIG = '<?xml version="1.0" encoding="UTF-8" ?>\n' +
                '<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">\n' +
                '<configuration>\n' +
                '  <typeHandlers>\n' +
                '    <typeHandler javaType="org.apache.ibatis.builder.XmlConfigBuilderSpec$MyEnum"\n' +
                '      handler="org.apache.ibatis.builder.XmlConfigBuilderSpec$EnumOrderTypeHandler"/>\n' +
                '  </typeHandlers>\n' +
                '</configuration>'

        when: "build config  "
        XMLConfigBuilder builder = new XMLConfigBuilder(new StringReader(MAPPER_CONFIG));
        builder.parse();

        and: " get TypeHandlerRegistry "
        TypeHandlerRegistry typeHandlerRegistry = builder.getConfiguration().getTypeHandlerRegistry();
        TypeHandler<MyEnum> typeHandler = typeHandlerRegistry.getTypeHandler(MyEnum.class);

        then: " handler should be "
        typeHandler instanceof EnumOrderTypeHandler
    }


    def "should Success fully Load XML Config File"() {
        given: " resource"
        String resource = "org/apache/ibatis/builder/CustomizedSettingsMapperConfig.xml";

        when: " read resource  as stream "
        InputStream inputStream = Resources.getResourceAsStream(resource)

        and: " create builder from stream"
        XMLConfigBuilder builder = new XMLConfigBuilder(inputStream);

        and: "build configuration"
        Configuration config = builder.parse();

        then: "config should be "
        config.getAutoMappingBehavior() == AutoMappingBehavior.NONE
        config.getAutoMappingUnknownColumnBehavior() == AutoMappingUnknownColumnBehavior.WARNING
        !config.isCacheEnabled()
        config.getProxyFactory() instanceof CglibProxyFactory
        config.isLazyLoadingEnabled()
        config.isAggressiveLazyLoading()
        !config.isMultipleResultSetsEnabled()
        !config.isUseColumnLabel()
        config.isUseGeneratedKeys()
        config.getDefaultExecutorType() == ExecutorType.BATCH
        config.getDefaultStatementTimeout() == 10
        config.getDefaultFetchSize() == 100
        config.getDefaultResultSetType() == ResultSetType.SCROLL_INSENSITIVE
        config.isMapUnderscoreToCamelCase()
        config.isSafeRowBoundsEnabled()
        config.getLocalCacheScope() == LocalCacheScope.STATEMENT
        config.getJdbcTypeForNull() == JdbcType.NULL
        config.getLazyLoadTriggerMethods() == new HashSet<>(Arrays.asList("equals", "clone", "hashCode", "toString", "xxx"))
        !config.isSafeResultHandlerEnabled()
        config.getDefaultScriptingLanuageInstance() instanceof RawLanguageDriver
        config.isCallSettersOnNulls()
        config.getLogPrefix() == "mybatis_"
        config.getLogImpl().getName() == Slf4jImpl.class.getName()
        config.getVfsImpl().getName() == JBoss6VFS.class.getName()
        config.getConfigurationFactory().getName() == String.class.getName()
        config.isShrinkWhitespacesInSql()
        config.getDefaultSqlProviderType().getName() == XmlConfigBuilderTest.MySqlProvider.class.getName()

        config.getTypeAliasRegistry().getTypeAliases().get("blogauthor") == Author.class
        config.getTypeAliasRegistry().getTypeAliases().get("blog") == Blog.class
        config.getTypeAliasRegistry().getTypeAliases().get("cart") == Cart.class

        config.getTypeHandlerRegistry().getTypeHandler(Integer.class) instanceof CustomIntegerTypeHandler
        config.getTypeHandlerRegistry().getTypeHandler(Long.class) instanceof CustomLongTypeHandler
        config.getTypeHandlerRegistry().getTypeHandler(String.class) instanceof CustomStringTypeHandler
        config.getTypeHandlerRegistry().getTypeHandler(String.class, JdbcType.VARCHAR) instanceof CustomStringTypeHandler
        config.getTypeHandlerRegistry().getTypeHandler(RoundingMode.class) instanceof EnumOrdinalTypeHandler

        ExampleObjectFactory objectFactory = (ExampleObjectFactory) config.getObjectFactory();
        objectFactory.getProperties().size() == 1
        objectFactory.getProperties().getProperty("objectFactoryProperty") == "100"

        config.getObjectWrapperFactory() instanceof CustomObjectWrapperFactory

        config.getReflectorFactory() instanceof CustomReflectorFactory

        ExamplePlugin plugin = (ExamplePlugin) config.getInterceptors().get(0);
        plugin.getProperties().size() == 1
        plugin.getProperties().getProperty("pluginProperty") == "100"

        Environment environment = config.getEnvironment();
        environment.getId() == "development"
        environment.getDataSource() instanceof UnpooledDataSource
        environment.getTransactionFactory() instanceof JdbcTransactionFactory

        config.getDatabaseId() == "derby"

        config.getMapperRegistry().getMappers().size() == 4
        config.getMapperRegistry().hasMapper(CachedAuthorMapper.class)
        config.getMapperRegistry().hasMapper(CustomMapper.class)
        config.getMapperRegistry().hasMapper(BlogMapper.class)
        config.getMapperRegistry().hasMapper(NestedBlogMapper.class)
    }

    def "should Success fully Load XML Config File With Properties Url"() {
        given: " resource"
        String resource = "org/apache/ibatis/builder/PropertiesUrlMapperConfig.xml";

        when: " read resource  as stream "
        InputStream inputStream = Resources.getResourceAsStream(resource)

        and: " create builder from stream"
        XMLConfigBuilder builder = new XMLConfigBuilder(inputStream);

        and: "build configuration"
        Configuration config = builder.parse();

        then: " config should be "
        config.getVariables().get("driver").toString() == "org.apache.derby.jdbc.EmbeddedDriver"
        config.getVariables().get("prop1").toString() == "bbbb"
    }

    def "parseIsTwice"() {
        given: " resource"
        String resource = "org/apache/ibatis/builder/MinimalMapperConfig.xml";

        when: " read resource  as stream "
        InputStream inputStream = Resources.getResourceAsStream(resource)

        and: " create builder from stream"
        XMLConfigBuilder builder = new XMLConfigBuilder(inputStream);

        and: "build configuration"
        Configuration config = builder.parse();

        and: " parse again"
        builder.parse()

        then: " caught exception  Each XMLConfigBuilder can only be used once."
        def caught = thrown(BuilderException.class)
        caught.getMessage().contains('Each XMLConfigBuilder can only be used once.')
    }


    def "unknown Settings"() {
        given: "xml config "
        String MAPPER_CONFIG = '<?xml version="1.0" encoding="UTF-8" ?>\n' +
                '<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">\n' +
                '<configuration>\n' +
                '  <settings>\n' +
                '    <setting name="foo" value="bar"/>\n' +
                '  </settings>\n' +
                '</configuration>'

        when: "build config  "
        XMLConfigBuilder builder = new XMLConfigBuilder(new StringReader(MAPPER_CONFIG));
        builder.parse();

        then: " caught exception that foo is not know"
        def caught = thrown(BuilderException.class)
        caught.getMessage().contains('The setting foo is not known.  Make sure you spelled it correctly (case sensitive).')

    }

    def "unknown Java Type On Type Handler"() {
        given: "xml config "
        String MAPPER_CONFIG = '<?xml version="1.0" encoding="UTF-8" ?>\n' +
                '<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">\n' +
                '<configuration>\n' +
                '  <typeAliases>\n' +
                '    <typeAlias type="a.b.c.Foo"/>\n' +
                '  </typeAliases>\n' +
                '</configuration>'

        when: "build config  "
        XMLConfigBuilder builder = new XMLConfigBuilder(new StringReader(MAPPER_CONFIG));
        builder.parse();

        then: " caught exception that Error registering typeAlias for \\'null\\'. "
        def caught = thrown(BuilderException.class)
        caught.getMessage().contains('Error registering typeAlias for \'null\'. Cause: ')


    }

    def "properties Specify Resource And Url At Same Time"() {
        given: "xml config "
        String MAPPER_CONFIG = '<?xml version="1.0" encoding="UTF-8" ?>\n' +
                '<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">\n' +
                '<configuration>\n' +
                '  <properties resource="a/b/c/foo.properties" url="file:./a/b/c/jdbc.properties"/>\n' +
                '</configuration>'

        when: "build config  "
        XMLConfigBuilder builder = new XMLConfigBuilder(new StringReader(MAPPER_CONFIG));
        builder.parse();

        then: " caught exception that Error registering typeAlias for \\'null\\'. "
        def caught = thrown(BuilderException.class)
        caught.getMessage().contains('The properties element cannot specify both a URL and a resource based property file reference.  Please specify one or the other.')


    }


}