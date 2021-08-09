package org.apache.ibatis.binding

import org.apache.ibatis.BaseDataTest
import org.apache.ibatis.domain.blog.Author
import org.apache.ibatis.domain.blog.Blog
import org.apache.ibatis.domain.blog.Post
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import spock.lang.Narrative
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Title
import spock.lang.Unroll

import javax.sql.DataSource


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

}