package me.caofeng.domain;

import me.caofeng.infrastructure.mapper.UserMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadUserFromConfigSqlSessionFactory {

    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        DataSource dataSource = BaseDataTest.createUserDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
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
