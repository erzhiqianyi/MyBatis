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

        @Override
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
        final String MAPPER_CONFIG = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
        +"<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\n"
        +"<configuration>\n"
        +"  <typeHandlers>\n"
        +"    <typeHandler javaType=\"org.apache.ibatis.builder.XmlConfigBuilderTest$MyEnum\"\n"
        +"      handler=\"org.apache.ibatis.builder.XmlConfigBuilderTest$EnumOrderTypeHandler\"/>\n"
        +"  </typeHandlers>\n"
        +"</configuration>\n"

        when: "build config  "
        XMLConfigBuilder builder = new XMLConfigBuilder(new StringReader(MAPPER_CONFIG));
        builder.parse();

        and:" get TypeHandlerRegistry "
        then: " "

        TypeHandlerRegistry typeHandlerRegistry = builder.getConfiguration().getTypeHandlerRegistry();
        TypeHandler<XmlConfigBuilderTest.MyEnum> typeHandler = typeHandlerRegistry.getTypeHandler(XmlConfigBuilderTest.MyEnum.class);

        assertTrue(typeHandler instanceof XmlConfigBuilderTest.EnumOrderTypeHandler);
        assertArrayEquals(XmlConfigBuilderTest.MyEnum.values(), ((XmlConfigBuilderTest.EnumOrderTypeHandler<XmlConfigBuilderTest.MyEnum>) typeHandler).constants);
    }


}