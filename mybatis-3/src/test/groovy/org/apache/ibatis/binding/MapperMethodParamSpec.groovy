package org.apache.ibatis.binding

import org.apache.ibatis.BaseDataTest
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import spock.lang.*

import javax.sql.DataSource

@Title("测试  MapperMethodParam")
@Narrative(""" 使用spock 重写 MapperMethodParamTest      """)
@Subject()
@Unroll
class MapperMethodParamSpec extends Specification {

    @Shared
    private SqlSessionFactory sqlSessionFactory;

    def setupSpec()  {
        DataSource dataSource = BaseDataTest.createUnpooledDataSource(BaseDataTest.BLOG_PROPERTIES);
        BaseDataTest.runScript(dataSource, "org/apache/ibatis/binding/paramtest-schema.sql");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("Production", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(Mapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    def "parameter Name Is Size And Type Is Long"() {
        given: " SqlSession from factory "
        SqlSession session = sqlSessionFactory.openSession()

        and: " mapper "
        Mapper mapper = session.getMapper(Mapper.class);

        when: "insert "
        mapper.insert("foo", Long.MAX_VALUE);

        and: " select size"
        long size = mapper.selectSize("foo")

        then: " size should be "
        size == Long.MAX_VALUE
    }

    def "parameterNameIsSizeUsingHashMap"() {
        given: " SqlSession from factory "
        SqlSession session = sqlSessionFactory.openSession()

        and: " mapper "
        Mapper mapper = session.getMapper(Mapper.class);

        and: " param"
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", "foo");
        params.put("size", Long.MAX_VALUE);

        when: "insertUsingHashMap "
        mapper.insertUsingHashMap(params);

        and: " select size"
        long size = mapper.selectSize("foo")

        then: " size should be "
        size == Long.MAX_VALUE

    }

    interface Mapper {
        @Insert("insert into param_test (id, size) values(#{id}, #{size})")
        void insert(@Param("id") String id, @Param("size") long size);

        @Insert("insert into param_test (id, size) values(#{id}, #{size})")
        void insertUsingHashMap(HashMap<String, Object> params);

        @Select("select size from param_test where id = #{id}")
        long selectSize(@Param("id") String id);
    }

}