package org.apache.ibatis.autoconstructor


import org.apache.ibatis.BaseDataTest
import org.apache.ibatis.exceptions.PersistenceException
import org.apache.ibatis.io.Resources
import org.apache.ibatis.reflection.factory.DefaultObjectFactory
import org.apache.ibatis.reflection.factory.ObjectFactory
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import spock.lang.*

@Title("测试Mybatis 自动将数据库查询对象转化成指定返回结果 ")
@Narrative(""" 使用spock 重写 AutoConstructorTest ,测试Mybatis 使用构造器进行结果转换""")
@Subject(AutoConstructorMapper)
@Unroll
class AutoConstructorSpec extends Specification {

    @Shared
    private SqlSessionFactory sqlSessionFactory;

    def setupSpec() {
        // create a SqlSessionFactory ,从配置文件中 创建 SqlSessionFactory
        Reader reader = Resources.getResourceAsReader("org/apache/ibatis/autoconstructor/mybatis-config.xml")
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // populate in-memory database, 创建内存数据库
        BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), "org/apache/ibatis/autoconstructor/CreateDB.sql")

    }


    def "fully Populated Subject "() {
        given: "SqlSession from SqlSession factory  "
        SqlSession sqlSession = sqlSessionFactory.openSession()

        and: " a mapper from SqlSession"
        AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);

        when: " get subject "
        Object subject = mapper.getSubject(1)

        then: " subject should not be null"
        null != subject


    }

    def " primitive Subjects"() {
        given: "SqlSession from SqlSession factory  "
        SqlSession sqlSession = sqlSessionFactory.openSession()

        and: " a mapper from SqlSession"
        AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);

        when: " get subject "
        List<PrimitiveSubject> subjects = mapper.getSubjects()

        then: " subject should not be null"
        //数据有问题,(2, 'b', 10, NULL, 45, 1, CURRENT_TIMESTAMP)有个不存在，导致使用构造器去构造对象时，无法生成对应对象,
        thrown(PersistenceException.class)

    }

    def "PrimitiveConstructor"() {
        given: " a ObjectFactory "
        ObjectFactory factory = new DefaultObjectFactory();

        and: " constructorArgTypes  "
        List<Class<?>> constructorArgTypes = new ArrayList<>();
        constructorArgTypes.add(int.class);
        constructorArgTypes.add(String.class);
        constructorArgTypes.add(int.class);
        constructorArgTypes.add(int.class);
        constructorArgTypes.add(int.class);
        constructorArgTypes.add(boolean.class);
        constructorArgTypes.add(Date.class);

        and: " constructorArgs "
        List<Object> constructorArgs = new ArrayList<>();
        constructorArgs.add(2);
        constructorArgs.add('b');
        constructorArgs.add(10);
        constructorArgs.add(null);
        constructorArgs.add(45);
        constructorArgs.add(true);
        constructorArgs.add(new Date());

        when: "create object from factory"
        factory.create(PrimitiveSubject.class, constructorArgTypes, constructorArgs)

        then: ""
        thrown(PersistenceException.class)
    }


    def " annotated Subject"() {
        given: "SqlSession from SqlSession factory  "
        SqlSession sqlSession = sqlSessionFactory.openSession()

        and: " a mapper from SqlSession"
        AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);

        when: " get subject "
        List<AnnotatedSubject> subject = mapper.getAnnotatedSubjects()

        then: " subject should not be null"
        //AnnotatedSubject 用AutomapConstructor 指定了默认解析构造器，使用指定的构造器进行构造，
        //height 改成用Integer ，为null也可以处理
        subject != null
        subject.size() == 3

    }

    def " bad Subject"() {
        given: "SqlSession from SqlSession factory  "
        SqlSession sqlSession = sqlSessionFactory.openSession()

        and: " a mapper from SqlSession"
        AutoConstructorMapper mapper = sqlSession.getMapper(AutoConstructorMapper.class);

        when: " get subject "
        List<AnnotatedSubject> subject = mapper.getBadSubjects()

        then: " subject should not be null"
        //类型不匹配
        thrown(PersistenceException.class)

    }


}