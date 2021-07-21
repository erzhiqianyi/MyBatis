package me.caofeng.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class LoadUserJDBC {

    private Statement statement;

    @BeforeEach
    public void setUp() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/mybatis?serverTimezone=UTC";
        String userName = "root";
        String password = "peak";

        //第一步: 加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");

        //第二步: 获得数据库连接
        Connection conn = DriverManager.getConnection(url, userName, password);

        //第三步: 创建语句并执行
        statement = conn.createStatement();
    }


    private void runScript(String file) {
        try (Stream<String> lines = Files.lines(Paths.get(file))) {
            String content = lines.collect(Collectors.joining(System.lineSeparator()));
            statement.execute(content);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void loadUserByJdbc() throws ClassNotFoundException, SQLException {
        User userParam = new User();
        userParam.setSchoolName("Sunny School");

        ResultSet resultSet = statement.executeQuery(" select *  from `user` where schoolName = \'" + userParam.getSchoolName() + "\';");

        //第四步: 处理数据库操作结果
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setEmail(resultSet.getString("email"));
            user.setAge(resultSet.getInt("age"));
            user.setSex(resultSet.getInt("sex"));
            user.setSchoolName(resultSet.getString("schoolName"));
            userList.add(user);
        }

        for (User user : userList) {
            System.out.println("name : " + user.getName() + "; email : " + user.getEmail());
        }

    }

    @AfterEach
    public void close() throws SQLException {
        //第五步: 关闭连接
        statement.close();
    }

}