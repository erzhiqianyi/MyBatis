package me.caofeng.infrastructure.mapper;

import me.caofeng.domain.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    List<User> selectAllUser(User param);

    @Select(" SELECT id , name , email , age,sex , schoolName  FROM `user`  where id = #{id}" )
    User selectUser(Integer id);
}
