package org.apache.ibatis.binding

import org.apache.ibatis.BaseDataTest
import org.apache.ibatis.domain.blog.Author
import org.apache.ibatis.domain.blog.Post
import org.apache.ibatis.domain.blog.Section
import org.apache.ibatis.executor.BatchResult
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.*
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import spock.lang.*

import javax.sql.DataSource

@Title("测试  Flush")
@Narrative(""" 使用spock 重写  FlushTest    """)
@Subject()
@Unroll
class FlushSpec extends Specification {


    @Shared
    private SqlSessionFactory sqlSessionFactory;


    def setupSpec() {
        DataSource dataSource = BaseDataTest.createBlogDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("Production", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setDefaultExecutorType(ExecutorType.BATCH);
        configuration.getTypeAliasRegistry().registerAlias(Post.class);
        configuration.getTypeAliasRegistry().registerAlias(Author.class);
        configuration.addMapper(BoundAuthorMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    def "invoke Flush Statements Via Mapper"() {
        given: " SqlSession from factory "
        SqlSession session = sqlSessionFactory.openSession()

        and: "BoundAuthorMapper  "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        when: "insert author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);
        List<Integer> ids = new ArrayList<>();
        mapper.insertAuthor(author);
        ids.add(author.getId());

        and: "repeat insert"
        mapper.insertAuthor(author);
        ids.add(author.getId());

        and: "repeat insert"
        mapper.insertAuthor(author);
        ids.add(author.getId());

        and: "repeat insert"
        mapper.insertAuthor(author);
        ids.add(author.getId());

        and: "repeat insert"
        mapper.insertAuthor(author);
        ids.add(author.getId());

        and: " flush "
        List<BatchResult> results = mapper.flush();

        then: " reuslt shuold not be null "
        results.size() == 1
        results.get(0).getUpdateCounts().length == ids.size()

        when: " select author "
        List<Author> authors = []
        for (int id : ids) {
            Author selectedAuthor = mapper.selectAuthor(id);
            authors.add(selectedAuthor)
        }
        session.rollback()

        then: "authors should not null"
        authors.size() == ids.size()

    }


}