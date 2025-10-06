package com.dzk.wx.api.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import com.dzk.wx.redis.RedisComponent;
import com.dzk.wx.utils.SecurityUtil;
import com.dzk.wx.utils.WxLoginTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService  extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    
    @Autowired
    private UserConverter userConverter;

    @Autowired
    private WxLoginTool wxLoginTool;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisComponent redisComponent;

    @Value("default.Password")
    private String defaultPassword;

    
    public User saveUser(User user) {
        int result = userMapper.insert(user);
        return userMapper.getUserByOpenId(user.getOpenId());
    }

    public User getUserByUserName(String username){
        return userMapper.getUserByUsername(username);
    }


    public User getUserByOpenId(String openId){
        User user = userMapper.getUserByOpenId(openId);
        if(user==null){
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    public String register(UserDto.Input input){
        User user = userConverter.toEntity(input);
        String openId = wxLoginTool.wxLogin(input.getCode());
        user.setRole(RoleEnum.USER);
        user.setOpenId(openId);
        user.setPassword(passwordEncoder.encode(defaultPassword));
        userMapper.insert(user);
        return "注册成功！";
    }
}
