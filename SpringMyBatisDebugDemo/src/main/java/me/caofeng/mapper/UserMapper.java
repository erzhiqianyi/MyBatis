package me.caofeng.mapper;

import me.caofeng.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select(" SELECT id , name , email , age,sex , schoolName  FROM `user`  where id = #{id}" )
    User selectUser(Integer id);

    List<User> selectAllUser(User param);
}
