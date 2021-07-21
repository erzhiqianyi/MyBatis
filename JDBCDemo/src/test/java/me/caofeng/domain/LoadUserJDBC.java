package me.caofeng.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class LoadUserJDBC {


    @BeforeAll
    public void setUp(){

    }

    public void runScript(){

    }
    @Test
    public void loadUserByJdbc() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/mybatis?serverTimezone=UTC";
        String userName = "root";
        String password = "peak";

        User userParam = new User();
        userParam.setSchoolName("Sunny School");

        //第一步: 加载驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");

        //第二步: 获得数据库连接
        Connection conn = DriverManager.getConnection(url, userName, password);

        //第三步: 创建语句并执行
        Statement statement = conn.createStatement();
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

        //第五步: 关闭连接
        statement.close();
        for (User user : userList) {
            System.out.println("name : " + user.getName() + "; email : " + user.getEmail());
        }

    }

}