package me.caofeng.domain;

import me.caofeng.infrastructure.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadUserFromXmlSqlSessionFactory {

    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        DataSource dataSource = BaseDataTest.createUserDataSource();
        BaseDataTest.runScript(dataSource,BaseDataTest.USER_DDL);
    }

    @Test
    public void selectUser() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            User userParam = new User();
            userParam.setSchoolName("Sunny School");
            List<User> userList = userMapper.selectAllUser(userParam);
            assertFalse(userList.isEmpty());
            for (User user : userList) {
                assertEquals("Sunny School",user.getSchoolName());
            }
        }
    }

    @Test
    public void selectOne() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            Integer id = 1;
            User user = userMapper.selectUser(1);
            assertNotNull(user);
            assertEquals(1, user.getId());
            assertEquals("易哥",user.getName());

        }
    }

}