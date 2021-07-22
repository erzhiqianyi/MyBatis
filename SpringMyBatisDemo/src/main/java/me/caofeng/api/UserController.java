package me.caofeng.api;

import me.caofeng.domain.User;
import me.caofeng.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/user")
    public List<User> listUser(User param) {
        return userMapper.selectAllUser(param);
    }

    @GetMapping("user/{id}")
    public User getUser(@PathVariable("id") Integer id){
        return userMapper.selectUser(id);
   }
}
