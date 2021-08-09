package org.apache.ibatis.binding

import javassist.util.proxy.Proxy
import net.sf.cglib.proxy.Factory
import org.apache.ibatis.BaseDataTest
import org.apache.ibatis.cursor.Cursor
import org.apache.ibatis.domain.blog.Author
import org.apache.ibatis.domain.blog.Blog
import org.apache.ibatis.domain.blog.DraftPost
import org.apache.ibatis.domain.blog.Post
import org.apache.ibatis.domain.blog.Section
import org.apache.ibatis.exceptions.PersistenceException
import org.apache.ibatis.executor.result.DefaultResultHandler
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.RowBounds
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import javax.sql.DataSource
import java.lang.reflect.Method

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException
import static com.googlecode.catchexception.apis.BDDCatchException.caughtException
import static com.googlecode.catchexception.apis.BDDCatchException.when
import static com.googlecode.catchexception.apis.BDDCatchException.when
import static org.assertj.core.api.BDDAssertions.then
import static org.assertj.core.api.BDDAssertions.then
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertNotEquals
import static org.junit.jupiter.api.Assertions.assertNotEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertNotSame
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertSame
import static org.junit.jupiter.api.Assertions.assertSame
import static org.junit.jupiter.api.Assertions.assertSame
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue
import static org.junit.jupiter.api.Assertions.assertTrue


@Title("测试  参数绑定")
@Narrative(""" 使用spock 重写  BindingTest   """)
@Subject()
@Unroll
class BindingSpec extends Specification {

    @Shared
    private SqlSessionFactory sqlSessionFactory

    def setupSpec() {
        DataSource dataSource = BaseDataTest.createBlogDataSource();
        BaseDataTest.runScript(dataSource, BaseDataTest.BLOG_DDL);
        BaseDataTest.runScript(dataSource, BaseDataTest.BLOG_DATA);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("Production", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setLazyLoadingEnabled(true);
        configuration.setUseActualParamName(false); // to test legacy style reference (#{0} #{1})
        configuration.getTypeAliasRegistry().registerAlias(Blog.class);
        configuration.getTypeAliasRegistry().registerAlias(Post.class);
        configuration.getTypeAliasRegistry().registerAlias(Author.class);
        configuration.addMapper(BoundBlogMapper.class);
        configuration.addMapper(BoundAuthorMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

    }

    def "should Select Blog With Posts Using SubSelect"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: "select Blog With Posts Using SubSelect"
        Blog blog = mapper.selectBlogWithPostsUsingSubSelect(1)

        then: " blog should be "
        null != blog
        blog.getId() == 1
        null != blog.getAuthor()
        101 == blog.getAuthor().getId()
        "jim" == blog.getAuthor().getUsername()
        "********" == blog.getAuthor().getPassword()
        2 == blog.getPosts().size()

    }

    def "should Find Posts In List"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " post ids are  "
        Integer[] ids = new Integer[3]
        ids[0] = 1
        ids[1] = 3
        ids[2] = 5

        when: "select Blog With Posts Using SubSelect"
        List<Post> posts = mapper.findPostsInArray(ids)

        then: " blog should be "
        posts.size() == 3

    }

    def "should Find Three Specific Posts"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        when: "select Blog With Posts Using SubSelect"
        List<Post> posts = mapper.findThreeSpecificPosts(1, new RowBounds(1, 1), 3, 5)

        then: " blog should be "
        posts.size() == 1
        3 == posts.get(0).getId()

    }


    def "should Insert Author With Select Key"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: "insert author "
        int rows = mapper.insertAuthor(author);

        then: " rows should be one"
        rows == 1

    }

    def "verify Error Message From Select Key"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: " insert Author Invalid SelectKey"
        mapper.insertAuthorInvalidSelectKey(author)

        then: "should  throw  error message"
        def caught = thrown(PersistenceException.class)
        caught.getMessage().contains("### The error may exist in org/apache/ibatis/binding/BoundAuthorMapper.xml"
                + System.lineSeparator() +
                "### The error may involve org.apache.ibatis.binding.BoundAuthorMapper.insertAuthorInvalidSelectKey!selectKey"
                + System.lineSeparator() +
                "### The error occurred while executing a query"
        )
    }

    def "verify Error Message From Insert After SelectKey"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: " insert Author Invalid SelectKey"
        mapper.insertAuthorInvalidInsert(author)

        then: "should throw error message  "
        def caught = thrown(PersistenceException.class)
        caught.getMessage().contains("### The error may exist in org/apache/ibatis/binding/BoundAuthorMapper.xml" + System.lineSeparator() +
                "### The error may involve org.apache.ibatis.binding.BoundAuthorMapper.insertAuthorInvalidInsert" + System.lineSeparator() +
                "### The error occurred while executing an update")


    }


}