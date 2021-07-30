package org.apache.ibatis.exceptions

import org.apache.ibatis.binding.BindingException
import org.apache.ibatis.builder.BuilderException
import org.apache.ibatis.cache.CacheException
import org.apache.ibatis.datasource.DataSourceException
import org.apache.ibatis.executor.ExecutorException
import org.apache.ibatis.logging.LogException
import org.apache.ibatis.parsing.ParsingException
import org.apache.ibatis.plugin.PluginException
import org.apache.ibatis.reflection.ReflectionException
import org.apache.ibatis.scripting.ScriptingException
import org.apache.ibatis.session.SqlSessionException
import org.apache.ibatis.transaction.TransactionException
import org.apache.ibatis.type.TypeException
import spock.lang.*

import java.lang.reflect.Constructor

@Title("测试Mybatis 异常包 ")
@Narrative(""" 使用spock 重写 GeneralExceptionsTest 获取指定异常信息  """)
@Subject(IbatisException)
@Unroll
class GeneralExceptionsSpec extends Specification {

    private static final String EXPECTED_MESSAGE = "Test Message";
    private static final Exception EXPECTED_CAUSE = new Exception("Nested Exception");

    def "ExceptionFactory should wrapper  Exceptions as PersistenceException "() {

        when: "  run time exception wrap by ExceptionFactory"
        RuntimeException exception = ExceptionFactory.wrapException(EXPECTED_MESSAGE, EXPECTED_CAUSE)
        thrownException(exception)

        then: " exception should be PersistenceException "
        exception instanceof PersistenceException
        def caught = thrown(exception.getClass())
        exception.getMessage() == caught.getMessage()
        exception.getCause() == caught.getCause()
    }


    def "should Instantiate And Throw All Custom Exceptions from  RuntimeException() #exceptionType "() {
        given: " exceptionType   constructor  "
        Constructor constructor = exceptionType.getDeclaredConstructor()
        when: " Instantiate  exception from constructor  "
        Exception e = (Exception) constructor.newInstance()
        thrownException(e)

        then: " exception should be instantiate  "
        def caught = thrown(e.getClass())
        caught.getMessage() == expectedMessage
        caught.getCause() == expectedMessageCause

        where: " exception type are "
        exceptionType              || expectedMessage | expectedMessageCause
        BindingException.class     || null            | null
        CacheException.class       || null            | null
        DataSourceException.class  || null            | null
        ExecutorException.class    || null            | null
        LogException.class         || null            | null
        ParsingException.class     || null            | null
        BuilderException.class     || null            | null
        PluginException.class      || null            | null
        ReflectionException.class  || null            | null
        PersistenceException.class || null            | null
        SqlSessionException.class  || null            | null
        TransactionException.class || null            | null
        TypeException.class        || null            | null
        ScriptingException.class   || null            | null


    }


    def "should Instantiate And Throw All Custom Exceptions from  RuntimeException(String message) #exceptionType #message "() {
        given: " exceptionType   constructor  "
        Constructor constructor = exceptionType.getDeclaredConstructor(String.class)
        when: " Instantiate  exception from constructor  "
        Exception e = (Exception) constructor.newInstance(message)
        thrownException(e)

        then: " exception should be instantiate  "
        def caught = thrown(e.getClass())
        caught.getMessage() == expectedMessage
        caught.getCause() == expectedMessageCause

        where: " exception type are "
        exceptionType              | message                 | cause || expectedMessage         | expectedMessageCause
        BindingException.class     | "Binding Exception"     | null  || "Binding Exception"     | null
        CacheException.class       | "Cache Exception"       | null  || "Cache Exception"       | null
        DataSourceException.class  | "DataSource jException" | null  || "DataSource jException" | null
        ExecutorException.class    | "Executor Exception"    | null  || "Executor Exception"    | null
        LogException.class         | "Log Exception"         | null  || "Log Exception"         | null
        ParsingException.class     | "Parsing Exception"     | null  || "Parsing Exception"     | null
        BuilderException.class     | "Builder Exception"     | null  || "Builder Exception"     | null
        PluginException.class      | "Plugin Exception"      | null  || "Plugin Exception"      | null
        ReflectionException.class  | "Reflection Exception"  | null  || "Reflection Exception"  | null
        PersistenceException.class | "Persistence Exception" | null  || "Persistence Exception" | null
        SqlSessionException.class  | "SqlSession Exception"  | null  || "SqlSession Exception"  | null
        TransactionException.class | "Transaction Exception" | null  || "Transaction Exception" | null
        TypeException.class        | "Type Exception"        | null  || "Type Exception"        | null
        ScriptingException.class   | "Scripting Exception"   | null  || "Scripting Exception"   | null


    }

    def "should Instantiate And Throw All Custom Exceptions from  RuntimeException(String message,Throwable cause) #exceptionType #message #cause  "() {
        given: " exceptionType   constructor  and throwableException  "
        Constructor constructor = exceptionType.getDeclaredConstructor(String.class, Throwable.class)
        Exception causeException = new Exception(cause)
        when: " Instantiate  exception from constructor  "
        Exception e = (Exception) constructor.newInstance(message, causeException)
        thrownException(e)

        then: " exception should be instantiate  "
        def caught = thrown(e.getClass())
        caught.getMessage() == expectedMessage
        caught.getCause() == causeException

        where: " exception type are "
        exceptionType              | message                 | cause                                       || expectedMessage
        BindingException.class     | "Binding Exception"     | "Nested Exception In Binding Exception"     || "Binding Exception"
        CacheException.class       | "Cache Exception"       | "Nested Exception In Cache Exception"       || "Cache Exception"
        DataSourceException.class  | "DataSource jException" | "Nested Exception In DataSource Exception"  || "DataSource jException"
        ExecutorException.class    | "Executor Exception"    | "Nested Exception In Executor  Exception"   || "Executor Exception"
        LogException.class         | "Log Exception"         | "Nested Exception In Log Exception"         || "Log Exception"
        ParsingException.class     | "Parsing Exception"     | "Nested Exception In Parsing Exception"     || "Parsing Exception"
        BuilderException.class     | "Builder Exception"     | "Nested Exception In Builder Exception"     || "Builder Exception"
        PluginException.class      | "Plugin Exception"      | "Nested Exception In Plugin Exception"      || "Plugin Exception"
        ReflectionException.class  | "Reflection Exception"  | "Nested Exception In Reflection Exception"  || "Reflection Exception"
        PersistenceException.class | "Persistence Exception" | "Nested Exception In Persistence Exception" || "Persistence Exception"
        SqlSessionException.class  | "SqlSession Exception"  | "Nested Exception In SqlSession Exception"  || "SqlSession Exception"
        TransactionException.class | "Transaction Exception" | "Nested Exception In Transaction Exception" || "Transaction Exception"
        TypeException.class        | "Type Exception"        | "Nested Exception In Type Exception"        || "Type Exception"
        ScriptingException.class   | "Scripting Exception"   | "Nested Exception In Scripting Exception"   || "Scripting Exception"


    }

    def "should Instantiate And Throw All Custom Exceptions from  RuntimeException(Throwable cause) #exceptionType #cause  "() {
        given: " exceptionType   constructor  and throwableException  "
        Constructor constructor = exceptionType.getDeclaredConstructor(Throwable.class)
        Exception causeException = new Exception(cause)
        when: " Instantiate  exception from constructor  "
        Exception e = (Exception) constructor.newInstance(causeException)
        thrownException(e)

        then: " exception should be instantiate  "
        def caught = thrown(e.getClass())
        caught.getCause() == causeException

        where: " exception type are "
        exceptionType              | message                 | cause                                       || expectedMessage
        BindingException.class     | "Binding Exception"     | "Nested Exception In Binding Exception"     || "Binding Exception"
        CacheException.class       | "Cache Exception"       | "Nested Exception In Cache Exception"       || "Cache Exception"
        DataSourceException.class  | "DataSource jException" | "Nested Exception In DataSource Exception"  || "DataSource jException"
        ExecutorException.class    | "Executor Exception"    | "Nested Exception In Executor  Exception"   || "Executor Exception"
        LogException.class         | "Log Exception"         | "Nested Exception In Log Exception"         || "Log Exception"
        ParsingException.class     | "Parsing Exception"     | "Nested Exception In Parsing Exception"     || "Parsing Exception"
        BuilderException.class     | "Builder Exception"     | "Nested Exception In Builder Exception"     || "Builder Exception"
        PluginException.class      | "Plugin Exception"      | "Nested Exception In Plugin Exception"      || "Plugin Exception"
        ReflectionException.class  | "Reflection Exception"  | "Nested Exception In Reflection Exception"  || "Reflection Exception"
        PersistenceException.class | "Persistence Exception" | "Nested Exception In Persistence Exception" || "Persistence Exception"
        SqlSessionException.class  | "SqlSession Exception"  | "Nested Exception In SqlSession Exception"  || "SqlSession Exception"
        TransactionException.class | "Transaction Exception" | "Nested Exception In Transaction Exception" || "Transaction Exception"
        TypeException.class        | "Type Exception"        | "Nested Exception In Type Exception"        || "Type Exception"
        ScriptingException.class   | "Scripting Exception"   | "Nested Exception In Scripting Exception"   || "Scripting Exception"


    }


    Exception thrownException(Exception thrown) {
        throw thrown;
    }


}