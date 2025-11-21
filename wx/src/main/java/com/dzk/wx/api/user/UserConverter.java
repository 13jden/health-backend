package com.dzk.wx.api.user;

import com.dzk.wx.api.child.Child;
import com.dzk.wx.api.growthrecord.GrowthRecord;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    
    /**
     * 将User实体转换为UserDto
     */
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setOpenId(user.getOpenId());
        // userDto.setAvatar(user.getAvatar());
        userDto.setRole(user.getRole());
        userDto.setPhone(user.getPhone());
        userDto.setDepartment(user.getDepartment());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
    
    /**
     * 将UserDto转换为User实体
     */
    public User toEntity(UserDto.Input input) {
        if (input == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(input.getUsername());
        // user.setAvatar(input.getAvatar());
        user.setPassword(input.getPassword());
        user.setPhone(input.getPhone());
        user.setDepartment(input.getDepartment());
        user.setEmail(input.getEmail());
        user.setRole(input.getRole());
        return user;
    }
    
    /**
     * 构造管理端患者视图
     */
    public UserDto.ManagerDto toManagerDto(User user, Child child, GrowthRecord growthRecord) {
        UserDto.ManagerDto managerDto = new UserDto.ManagerDto();
        if (user != null) {
            managerDto.setId(user.getId());
            managerDto.setUsername(user.getUsername());
            managerDto.setOpenId(user.getOpenId());
            managerDto.setRole(user.getRole());
            managerDto.setPhone(user.getPhone());
            managerDto.setDepartment(user.getDepartment());
            managerDto.setEmail(user.getEmail());
        }
        if (child != null) {
            managerDto.setChildId(child.getId());
            managerDto.setName(child.getName());
            managerDto.setGender(child.getGender());
            managerDto.setBirthdate(child.getBirthdate());
        }
        if (growthRecord != null) {
            managerDto.setHeight(growthRecord.getHeight());
            managerDto.setWeight(growthRecord.getWeight());
            managerDto.setBmi(growthRecord.getBmi());
        }
        return managerDto;
    }
}