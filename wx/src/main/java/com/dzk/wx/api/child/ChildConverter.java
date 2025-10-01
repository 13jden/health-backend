package com.dzk.wx.api.child;

import org.springframework.stereotype.Component;

@Component
public class ChildConverter {
    
    /**
     * 将Child实体转换为ChildDto
     */
    public ChildDto toDto(Child child) {
        if (child == null) {
            return null;
        }
        
        ChildDto childDto = new ChildDto();
        childDto.setId(child.getId());
        childDto.setParentId(child.getParentId());
        childDto.setName(child.getName());
        childDto.setGender(child.getGender());
        childDto.setBirthdate(child.getBirthdate());
        childDto.setHeight(child.getHeight());
        childDto.setWeight(child.getWeight());
        childDto.setBmi(child.getBmi());
        childDto.setBoneAge(child.getBoneAge());
        childDto.setTestDate(child.getTestDate());
        return childDto;
    }
    
    /**
     * 将ChildDto.Input转换为Child实体
     */
    public Child toEntity(ChildDto.Input input) {
        if (input == null) {
            return null;
        }
        
        Child child = new Child();
        child.setParentId(input.getParentId());
        child.setName(input.getName());
        child.setGender(input.getGender());
        child.setBirthdate(input.getBirthdate());
        child.setHeight(input.getHeight());
        child.setWeight(input.getWeight());
        child.setBmi(input.getBmi());
        child.setBoneAge(input.getBoneAge());
        child.setTestDate(input.getTestDate());
        return child;
    }

    /**
     * 将Child实体转换为ChildDto.Detail
     */
    public ChildDto.Detail toDetail(Child child) {
        if (child == null) {
            return null;
        }
        
        ChildDto.Detail detail = new ChildDto.Detail();
        detail.setId(child.getId());
        detail.setParentId(child.getParentId());
        detail.setName(child.getName());
        detail.setGender(child.getGender());
        detail.setBirthdate(child.getBirthdate());
        detail.setHeight(child.getHeight());
        detail.setWeight(child.getWeight());
        detail.setBmi(child.getBmi());
        detail.setBoneAge(child.getBoneAge());
        detail.setTestDate(child.getTestDate());
        detail.setCreateTime(child.getCreateTime() != null ? child.getCreateTime().toString() : null);
        detail.setUpdateTime(child.getUpdateTime() != null ? child.getUpdateTime().toString() : null);
        return detail;
    }
} 