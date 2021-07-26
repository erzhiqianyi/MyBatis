package me.caofeng;

import me.caofeng.domain.User;
import me.caofeng.mapper.UserMapper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.Jdk;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class SpringMyBatisDebugApplication {

    public static void main(String[] args) {
        initMyBatis();
    }

    private static void initMyBatis() {
        //MyBatis 初始化，通过xml文件构建 SqlSessionFactory,建立数据库连接
        String resource = "mybatis-config.xml";
        SqlSessionFactory sqlSessionFactory = null;
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //进行操作
        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            User param = new User();
            param.setSchoolName("Sunny School");
            List<User> userList = userMapper.selectAllUser(param);
            for (User user : userList){
                System.out.println("name: " + user.getName() + ";email:" + user.getEmail());
            }
        }

    }

}
