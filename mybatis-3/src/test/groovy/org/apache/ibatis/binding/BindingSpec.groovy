package org.apache.ibatis.binding


import net.sf.cglib.proxy.Factory
import org.apache.ibatis.BaseDataTest
import org.apache.ibatis.cursor.Cursor
import org.apache.ibatis.domain.blog.*
import org.apache.ibatis.exceptions.PersistenceException
import org.apache.ibatis.executor.result.DefaultResultHandler
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.*
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.junit.jupiter.api.Test
import spock.lang.*

import javax.sql.DataSource
import java.lang.reflect.Method

@Title("测试  绑定参数绑定，结果绑定")
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

        and: " rollback "
        session.rollback();

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


    def "should Insert Author With Select Key And DynamicParams"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: "insertAuthorDynamic "
        int rows = mapper.insertAuthorDynamic(author);

        then: " rows should be 1"
        rows == 1

        and: " author are "
        author.getId() != -1

        when: " select author by id"
        Author author2 = mapper.selectAuthor(author.getId())

        then: " author2 should equals to  author"
        null != author2
        author2.getEmail() == author.getEmail()

    }

    def "should Select Random"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: "select Random "
        Integer x = mapper.selectRandom()

        then: " random should not be null "
        null != x
    }

    def "should Execute Bound SelectList Of Blogs Statement"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: "select blogs "
        List<Blog> blogs = mapper.selectBlogs()

        then: " blogs size should be 2 "
        blogs.size() == 2

    }

    def "should Execute Bound Select Map Of BlogsById"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: "select Blogs As Map ById "
        Map<Integer, Blog> blogs = mapper.selectBlogsAsMapById();

        then: " blogs size should be 2 "
        blogs.size() == 2
        blogs.get(1).getTitle() == 'Jim Business'
        blogs.get(2).getTitle() == 'Bally Slog'

    }

    def "should Execute Multiple Bound Select Of Blogs ById In WithProvided Result Handler Between Sessions"() {

        given: " a DefaultResultHandler  "
        DefaultResultHandler handler = new DefaultResultHandler();

        when: " open a session  "
        SqlSession sessionOne = sqlSessionFactory.openSession()

        and: " select  "
        sessionOne.select("selectBlogsAsMapById", handler);

        then: " result list size should be 2"
        handler.getResultList().size() == 2


        when: " open another session "
        SqlSession sessionTwo = sqlSessionFactory.openSession()

        and: " select with another handler "
        DefaultResultHandler moreHandler = new DefaultResultHandler();
        sessionTwo.select("selectBlogsAsMapById", moreHandler);

        then: " result list size should be 2"
        moreHandler.getResultList().size() == 2

    }


    def "should Execute Multiple Bound Select Of Blogs ById In With Provided ResultHandler In Same Session"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        when: " select with  DefaultResultHandler "
        DefaultResultHandler handler = new DefaultResultHandler();
        //MappedStatement  也是通过session进行查询
        session.select("selectBlogsAsMapById", handler);

        and: "select with another  DefaultResultHandler  "
        DefaultResultHandler moreHandler = new DefaultResultHandler();
        session.select("selectBlogsAsMapById", moreHandler);

        then: " result list size should be 2"
        handler.getResultList().size() == 2
        moreHandler.getResultList().size() == 2
    }

    def "should Select List Of Blogs Using XML Config"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: " selectBlogsFromXML"
        List<Blog> blogs = mapper.selectBlogsFromXML();

        then: " blogs should exists"
        blogs.size() == 2
    }


    def "should Execute Bound Select List Of Blog sStatement UsingProvider"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: " selectBlogsFromXML"
        List<Blog> blogs = mapper.selectBlogsUsingProvider();

        then: " blogs should exists"
        blogs.size() == 2

    }


    def "should Select List Of Posts Like"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: "selectPostsLike"
        List<Post> posts = mapper.selectPostsLike(new RowBounds(1, 1), "%a%");

        then: " posts should exists"
        posts.size() == 1


    }

    def "should Select List Of Posts Like Two Parameters"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: "selectPostsLikeSubjectAndBody"
        List<Post> posts = mapper.selectPostsLikeSubjectAndBody(new RowBounds(1, 1), "%a%", "%a%");

        then: " posts should exists"
        posts.size() == 1

    }

    def "should Execute Bound Select One Blog Statement"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: " selectBlog"
        Blog blog = mapper.selectBlog(1);

        then: " blog should be "
        blog.getId() == 1
        blog.getTitle() == "Jim Business"

    }

    def "should Execute Bound Select One Blog Statement With Constructor"() {

        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: "selectBlogUsingConstructor"
        Blog blog = mapper.selectBlogUsingConstructor(1);

        then: " blog should be "
        blog.getId() == 1
        null != blog.getAuthor()
        blog.getTitle() == "Jim Business"
        null != blog.getPosts()
        !blog.getPosts().isEmpty()
    }

    def "should Execute Bound Select Blog Using Constructor With ResultMap"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class);

        when: " selectBlogUsingConstructorWithResultMap"
        Blog blog = mapper.selectBlogUsingConstructorWithResultMap(1);

        then: " blog should be "
        blog.getId() == 1
        null != blog.getAuthor()
        blog.getTitle() == "Jim Business"
        null != blog.getPosts()
        !blog.getPosts().isEmpty()

    }

    def "should Execute Bound Select Blog Using Constructor With ResultMap And Properties"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectBlogUsingConstructorWithResultMapAndProperties "
        Blog blog = mapper.selectBlogUsingConstructorWithResultMapAndProperties(1);

        then: " blog should be "
        blog.getId() == 1
        null != blog.getAuthor()
        blog.getTitle() == "Jim Business"
        null != blog.getPosts()
        !blog.getPosts().isEmpty()

        and: " author are "
        blog.getAuthor().getId() == 101
        blog.getAuthor().getEmail() == 'jim@ibatis.apache.org'
        blog.getAuthor().getUsername() == 'jim'
        blog.getAuthor().getFavouriteSection() == Section.NEWS

    }

    def "should Execute Bound Select One Blog Statement With Constructor Using XML Config"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: " selectBlogByIdUsingConstructor"
        Blog blog = mapper.selectBlogByIdUsingConstructor(1);

        then: " blog should be "
        blog.getId() == 1
        null != blog.getAuthor()
        blog.getTitle() == "Jim Business"
        null != blog.getPosts()
        !blog.getPosts().isEmpty()

    }

    def "should Select One Blog As Map"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        and: "map param "
        Map<String, Object> param = ['id': 1]
        when: " selectBlogAsMap"
        Map<String, Object> blog = mapper.selectBlogAsMap(param)

        then: " blog should be "
        blog.get('ID') == 1
        blog.get('TITLE') == 'Jim Business'
    }


    @Test
    void shouldSelectOneAuthor() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        when: "selectAuthor "
        Author author = mapper.selectAuthor(101);

        then: "  author should be "
        author.getId() == 101
        author.getUsername() == "jim"
        author.getPassword() == "********"
        author.getEmail() == "jim@ibatis.apache.org"
        author.getBio() == ""
    }

    def "should Select One Author From Cache"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        when: " select author  "
        BoundAuthorMapper mapper1 = session.getMapper(BoundAuthorMapper.class);
        Author author1 = mapper1.selectAuthor(101)

        and: " select another author"
        BoundAuthorMapper mapper2 = session.getMapper(BoundAuthorMapper.class);
        Author author2 = mapper2.selectAuthor(101)

        then: "author1 and author2 should be same "
        author1 == author2

    }


    def "should Select One Author By Constructor"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        when: "selectAuthorConstructor"
        Author author = mapper.selectAuthorConstructor(101);

        then: " author should be"
        author.getId() == 101
        author.getUsername() == "jim"
        author.getPassword() == "********"
        author.getEmail() == "jim@ibatis.apache.org"
        author.getBio() == ""
    }


    def "should Select Draft Typed Posts"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectPosts"
        List<Post> posts = mapper.selectPosts();

        then: " post should be "
        posts.size() == 5
        posts[0] instanceof DraftPost
        !(posts[1] instanceof DraftPost)
        posts[2] instanceof DraftPost
        !(posts[3] instanceof DraftPost)
        !(posts[4] instanceof DraftPost)
    }


    def "should Select Draft Type dPosts With ResultMap"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectPostsWithResultMap"
        List<Post> posts = mapper.selectPostsWithResultMap();

        then: "post should be "
        posts.size() == 5
        posts[0] instanceof DraftPost
        !(posts[1] instanceof DraftPost)
        posts[2] instanceof DraftPost
        !(posts[3] instanceof DraftPost)
        !(posts[4] instanceof DraftPost)

    }


    def "should Return A Not Null To String"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        when: "get  a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        then: " mapper toString should not null"
        null != mapper.toString()
    }


    def "should Return A Not Null HashCode"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        when: " get  a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        then: " mapper hash code should not be null"
        null != mapper.hashCode()
    }


    def "should Compare Two Mappers"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        when: "get  mappers "
        //mapper 使用动态代理生成，所以获取到的mapper会不一样
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)
        BoundBlogMapper mapper2 = session.getMapper(BoundBlogMapper.class);

        then: " mappers should not be same"
        mapper != mapper2
    }


    def "should Fail When Selecting One Blog With Non ExistentP aram"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: " selectBlogByNonExistentParam"
        mapper.selectBlogByNonExistentParam(1)

        then: " should thrown exception"
        thrown(Exception.class)
    }


    def "should Fail When Selecting One Blog With Null Param"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: " selectBlogByNullParam"
        mapper.selectBlogByNullParam(null)

        then: " should thrown exception"
        thrown(Exception.class)
    }


    def "should Fail When Selecting One Blog With Non Existent NestedParam"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectBlogByNonExistentNestedParam "
        Blog blog = mapper.selectBlogByNonExistentNestedParam(1, Collections.<String, Object> emptyMap());

        then: "blog should be null"
        null == blog
    }


    def "should Select Blog With Default 30 Param Names"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectBlogByDefault30ParamNames "
        Blog blog = mapper.selectBlogByDefault30ParamNames(1, "Jim Business");

        then: " blog should not be null "
        null != blog
    }


    def "should Select Blog With Default 31 ParamNames"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectBlogByDefault31ParamNames"
        Blog blog = mapper.selectBlogByDefault31ParamNames(1, "Jim Business");

        then: " blog should not be null "
        null != blog
    }


    def "should Select Blog With A Param Named Value"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectBlogWithAParamNamedValue"
        Blog blog = mapper.selectBlogWithAParamNamedValue("id", 1, "Jim Business");

        then: " blog should not be null "
        null != blog
    }


    def "shouldCacheMapperMethod"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        when: " Create another mapper instance with a method cache we can test against"
        final MapperProxyFactory<BoundBlogMapper> mapperProxyFactory = new MapperProxyFactory<BoundBlogMapper>(BoundBlogMapper.class);

        then: "mapperProxyFactory.getMapperInterface() should be   "
        mapperProxyFactory.getMapperInterface() == BoundBlogMapper.class

        when: " create instance from mapperProxyFactory "
        BoundBlogMapper mapper = mapperProxyFactory.newInstance(session)

        then: " mapper should not same "
        mapper != mapperProxyFactory.newInstance(session)
        mapperProxyFactory.getMethodCache().isEmpty()

        when: " get selectBlog Mapper methods we will call later "
        Method selectBlog = BoundBlogMapper.class.getMethod("selectBlog", Integer.TYPE);
        Method selectBlogByIdUsingConstructor = BoundBlogMapper.class.getMethod("selectBlogByIdUsingConstructor", Integer.TYPE);
        // Call mapper method and verify it is cached:
        mapper.selectBlog(1);

        then: " method is cached "
        mapperProxyFactory.getMethodCache().size() == 1
        mapperProxyFactory.getMethodCache().containsKey(selectBlog)

        when: " get cached method "
        MapperProxy.MapperMethodInvoker cachedSelectBlog = mapperProxyFactory.getMethodCache().get(selectBlog);

        and: " clear cache"
        session.clearCache();

        and: "Call mapper method again"
        mapper.selectBlog(1);

        then: "cache is unchanged"
        mapperProxyFactory.getMethodCache().size() == 1
        cachedSelectBlog == mapperProxyFactory.getMethodCache().get(selectBlog)

        when: "clear cache "
        session.clearCache();
        mapper.selectBlogByIdUsingConstructor(1);

        then: " it shows up in the cache"
        2 == mapperProxyFactory.getMethodCache().size()
        cachedSelectBlog == mapperProxyFactory.getMethodCache().get(selectBlog)
        mapperProxyFactory.getMethodCache().containsKey(selectBlogByIdUsingConstructor)
    }

    def "should Get Blogs With Authors And Posts"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: " selectBlogsWithAutorAndPosts "
        List<Blog> blogs = mapper.selectBlogsWithAutorAndPosts();

        then: " blogs should be"
        blogs.size() == 2
        !(blogs[0] instanceof Factory)
        blogs[0].getAuthor().getId() == 101
        blogs[0].getPosts().size() == 1
        blogs[0].getPosts().get(0).getId() == 1
        !(blogs[1] instanceof Factory)
        blogs[1].getAuthor().getId() == 102
        blogs[1].getPosts().size() == 1
        blogs[1].getPosts().get(0).getId() == 2

    }


    def "should Get Blogs With Authors And Posts Eagerly"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectBlogsWithAutorAndPostsEagerly"
        List<Blog> blogs = mapper.selectBlogsWithAutorAndPostsEagerly();

        then: " blogs should be"
        blogs.size() == 2
        !(blogs[0] instanceof Factory)
        blogs[0].getAuthor().getId() == 101
        blogs[0].getPosts().size() == 1
        blogs[0].getPosts().get(0).getId() == 1
        !(blogs[1] instanceof Factory)
        blogs[1].getAuthor().getId() == 102
        blogs[1].getPosts().size() == 1
        blogs[1].getPosts().get(0).getId() == 2
    }


    def "execute With ResultHandler And RowBounds"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        and: " a DefaultResultHandler "
        DefaultResultHandler handler = new DefaultResultHandler();

        when: "collectRangeBlogs"
        mapper.collectRangeBlogs(handler, new RowBounds(1, 1));
        Blog blog = (Blog) handler.getResultList().get(0);

        then: " result should be"
        handler.getResultList().size() == 1

        and: " result one should be blog "
        blog.getId() == 2
    }


    def "execute With MapKey And RowBounds"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "selectRangeBlogsAsMapById"
        Map<Integer, Blog> blogs = mapper.selectRangeBlogsAsMapById(new RowBounds(1, 1));
        Blog blog = blogs.get(2);

        then: " blogs should be "
        blogs.size() == 1
        blog.getId() == 2
    }


    def "execute With Cursor And RowBounds"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundBlogMapper mapper = session.getMapper(BoundBlogMapper.class)

        when: "openRangeBlogs"
        Cursor<Blog> blogs = mapper.openRangeBlogs(new RowBounds(1, 1))
        Iterator<Blog> blogIterator = blogs.iterator();
        Blog blog = blogIterator.next();

        then: " blog should be "
        blog.getId() == 2
        !blogIterator.hasNext()
    }


    def "registeredMappers"() {
        when: "get mappers"
        Collection<Class<?>> mapperClasses = sqlSessionFactory.getConfiguration().getMapperRegistry().getMappers();

        then: " mappers are "
        mapperClasses.size() == 2
        mapperClasses.contains(BoundBlogMapper.class)
        mapperClasses.contains(BoundAuthorMapper.class)
    }

    def "should Map Properties Using Repeatable Annotation"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: " a mapper "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a  author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: "insertAuthor "
        mapper.insertAuthor(author);

        and: "  selectAuthorMapToPropertiesUsingRepeatable "
        Author author2 = mapper.selectAuthorMapToPropertiesUsingRepeatable(author.getId());

        and: "rollback "
        session.rollback()

        then: " author2 should same as author "
        null != author2
        author.getId() == author2.getId()
        author.getUsername() == author2.getUsername()
        author.getPassword() == author2.getPassword()
        author.getBio() == author2.getBio()
        author.getEmail() == author2.getEmail()
    }


    def "should Map Constructor Using Repeatable Annotation"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: "BoundAuthorMapper  "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a  author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: " insertAuthor "
        mapper.insertAuthor(author);

        and: "selectAuthorMapToConstructorUsingRepeatable "
        Author author2 = mapper.selectAuthorMapToConstructorUsingRepeatable(author.getId());

        and: "rollback"
        session.rollback()

        then: " author2 should same as author "
        null != author2

        author.getId() == author2.getId()
        author.getUsername() == author2.getUsername()
        author.getPassword() == author2.getPassword()
        author.getBio() == author2.getBio()
        author.getEmail() == author2.getEmail()

    }

    def "should Map Using Single Repeatable Annotation"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: "BoundAuthorMapper  "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a  author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: " insertAuthor"
        mapper.insertAuthor(author);

        and: "selectAuthorUsingSingleRepeatable"
        Author author2 = mapper.selectAuthorUsingSingleRepeatable(author.getId());

        and: " rollback"
        session.rollback()

        then: " author2 should same as author "
        null != author2
        author.getId() == author2.getId()
        author.getUsername() == author2.getUsername()
        null == author2.getPassword()
        null == author2.getBio()
        null == author2.getEmail()
    }

    def shouldMapWhenSpecifyBothArgAndConstructorArgs() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: "BoundAuthorMapper  "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a  author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: " insertAuthor"
        mapper.insertAuthor(author);

        and: "selectAuthorUsingBothArgAndConstructorArgs"
        Author author2 = mapper.selectAuthorUsingBothArgAndConstructorArgs(author.getId());

        and: " rollback"
        session.rollback()

        then: " author2 should same as author "
        null != author2
        author.getId() == author2.getId()
        author.getUsername() == author2.getUsername()
        author.getPassword() == author2.getPassword()
        author.getBio() == author2.getBio()
        author.getEmail() == author2.getEmail()

    }

    def "should Map When Specify Both Result And Results"() {
        given: " SqlSession  from SqlSessionFactory  "
        SqlSession session = sqlSessionFactory.openSession()

        and: "BoundAuthorMapper  "
        BoundAuthorMapper mapper = session.getMapper(BoundAuthorMapper.class);

        and: " a  author "
        Author author = new Author(-1, "cbegin", "******", "cbegin@nowhere.com", "N/A", Section.NEWS);

        when: " insertAuthor"
        mapper.insertAuthor(author);

        and: "selectAuthorUsingBothResultAndResults"
        Author author2 = mapper.selectAuthorUsingBothResultAndResults(author.getId());

        and: "rollback"
        session.rollback()

        then: " author2 should same as author "
        null != author2
        author.getId() == author2.getId()
        author.getUsername() == author2.getUsername()
        null == author2.getPassword()
        null == author2.getBio()
        null == author2.getEmail()
    }


}