package com.dzk.wx.api.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import com.dzk.wx.api.child.Child;
import com.dzk.wx.api.child.ChildMapper;
import com.dzk.wx.api.growthrecord.GrowthRecord;
import com.dzk.wx.api.growthrecord.GrowthRecordMapper;
import com.dzk.wx.redis.RedisComponent;
import com.dzk.wx.utils.WxLoginTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService  extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    
    @Autowired
    private UserConverter userConverter;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private GrowthRecordMapper growthRecordMapper;

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
        try {
            User user = userConverter.toEntity(input);
            String openId = wxLoginTool.wxLogin(input.getCode());
            if (openId == null) {
                throw new BusinessException("微信登录失败，请重试");
            }
            if(userMapper.getUserByOpenId(openId)!=null){
                throw new BusinessException( "账号已注册，请退出直接登录");
            }
            user.setRole(RoleEnum.USER);
            user.setOpenId(openId);
            user.setPassword(passwordEncoder.encode(defaultPassword));
            userMapper.insert(user);
            return "注册成功！";
        } catch (RuntimeException e) {
            throw new BusinessException("注册失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取用户详情
     */
    public UserDto getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return userConverter.toDto(user);
    }

    /**
     * 获取用户列表（分页）
     */
    public IPage<UserDto> getUserList(Integer current, Integer size, String username, String phone, RoleEnum role) {
        Page<User> page = new Page<>(current, size);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        if (username != null && !username.trim().isEmpty()) {
            queryWrapper.like("username", username);
        }
        if (phone != null && !phone.trim().isEmpty()) {
            queryWrapper.like("phone", phone);
        }
        if (role != null) {
            queryWrapper.eq("role", role);
        }
        
        queryWrapper.orderByDesc("create_time");
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);
        
        // 转换为DTO
        Page<UserDto> dtoPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserDto> dtoList = userPage.getRecords().stream()
                .map(userConverter::toDto)
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        
        return dtoPage;
    }

    /**
     * 获取所有用户列表
     */
    public List<UserDto> getAllUsers() {
        List<User> users = userMapper.selectList(new QueryWrapper<User>().orderByDesc("create_time"));
        return users.stream()
                .map(userConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 管理端患者列表（根据儿童信息聚合家长）
     */
    public IPage<UserDto.ManagerDto> getPatientList(Integer current, Integer size, String childName, String phone) {
        Page<Child> page = new Page<>(current, size);
        QueryWrapper<Child> childWrapper = new QueryWrapper<>();
        childWrapper.select("id", "parent_id", "name", "gender", "birthdate", "create_time", "update_time");
        if (StringUtils.hasText(childName)) {
            childWrapper.like("name", childName);
        }
        if (StringUtils.hasText(phone)) {
            childWrapper.apply("parent_id in (select id from user where phone like {0})", "%" + phone.trim() + "%");
        }
        childWrapper.orderByDesc("create_time");
        IPage<Child> childPage = childMapper.selectPage(page, childWrapper);

        List<Long> parentIds = childPage.getRecords().stream()
                .map(Child::getParentId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, User> parentMap = parentIds.isEmpty()
                ? Collections.emptyMap()
                : userMapper.selectBatchIds(parentIds).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<UserDto.ManagerDto> managerDtos = childPage.getRecords().stream()
                .map(child -> {
                    GrowthRecord latestGrowth = child.getId() == null ? null : growthRecordMapper.getRecordByChildId(child.getId());
                    return userConverter.toManagerDto(parentMap.get(child.getParentId()), child, latestGrowth);
                })
                .collect(Collectors.toList());

        Page<UserDto.ManagerDto> dtoPage = new Page<>(childPage.getCurrent(), childPage.getSize(), childPage.getTotal());
        dtoPage.setRecords(managerDtos);
        return dtoPage;
    }

    /**
     * 创建用户
     */
    @Transactional
    public UserDto createUser(UserDto.Input input) {
        // 检查用户名是否已存在
        if (input.getUsername() != null && !input.getUsername().trim().isEmpty()) {
            User existingUser = userMapper.getUserByUsername(input.getUsername());
            if (existingUser != null) {
                throw new BusinessException("用户名已存在");
            }
        }
        
        User user = userConverter.toEntity(input);
        if (user.getRole() == null) {
            user.setRole(RoleEnum.USER);
        }
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode(defaultPassword));
        }
        
        int result = userMapper.insert(user);
        if (result > 0) {
            User savedUser = userMapper.selectById(user.getId());
            return userConverter.toDto(savedUser);
        }
        throw new BusinessException("创建用户失败");
    }

    /**
     * 更新用户信息
     */
    @Transactional
    public UserDto updateUser(Long id, UserDto.Input input) {
        User existingUser = userMapper.selectById(id);
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 更新字段
        if (input.getUsername() != null) {
            // 检查用户名是否被其他用户使用
            User userWithSameName = userMapper.getUserByUsername(input.getUsername());
            if (userWithSameName != null && !userWithSameName.getId().equals(id)) {
                throw new BusinessException("用户名已被其他用户使用");
            }
            existingUser.setUsername(input.getUsername());
        }
        if (input.getPhone() != null) {
            existingUser.setPhone(input.getPhone());
        }
        if (input.getDepartment() != null) {
            existingUser.setDepartment(input.getDepartment());
        }
        if (input.getEmail() != null) {
            existingUser.setEmail(input.getEmail());
        }
        if (input.getRole() != null) {
            existingUser.setRole(input.getRole());
        }
        if (input.getPassword() != null && !input.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(input.getPassword()));
        }

        int result = userMapper.updateById(existingUser);
        if (result > 0) {
            User updatedUser = userMapper.selectById(id);
            return userConverter.toDto(updatedUser);
        }
        throw new BusinessException("更新用户信息失败");
    }

    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        User existingUser = userMapper.selectById(id);
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        int result = userMapper.deleteById(id);
        if (result <= 0) {
            throw new BusinessException("删除用户失败");
        }
    }
}
