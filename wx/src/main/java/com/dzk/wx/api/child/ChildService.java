package com.dzk.wx.api.child;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildService extends ServiceImpl<ChildMapper, Child> {

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private ChildConverter childConverter;

    /**
     * 添加儿童信息
     */
    @Transactional
    public ChildDto.Detail addChild(ChildDto.Input input) {
        Child child = childConverter.toEntity(input);
        int result = childMapper.insert(child);
        if (result > 0) {
            Child savedChild = childMapper.getChildById(child.getId());
            return childConverter.toDetail(savedChild);
        }
        throw new BusinessException("添加儿童信息失败");
    }

    /**
     * 根据ID获取儿童详情
     */
    public ChildDto.Detail getChildById(Long id) {
        Child child = childMapper.getChildById(id);
        if (child == null) {
            throw new BusinessException("儿童信息不存在");
        }
        return childConverter.toDetail(child);
    }

    /**
     * 根据家长ID获取所有儿童
     */
    public List<ChildDto.Detail> getChildrenByParentId(Long parentId) {
        List<Child> children = childMapper.getChildrenByParentId(parentId);
        return children.stream()
                .map(childConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 更新儿童信息
     */
    @Transactional
    public ChildDto.Detail updateChild(Long id, ChildDto.Input input) {
        Child existingChild = childMapper.getChildById(id);
        if (existingChild == null) {
            throw new BusinessException("儿童信息不存在");
        }

        // 更新字段
        existingChild.setParentId(input.getParentId());
        existingChild.setName(input.getName());
        existingChild.setGender(input.getGender());
        existingChild.setBirthdate(input.getBirthdate());
        existingChild.setHeight(input.getHeight());
        existingChild.setWeight(input.getWeight());
        existingChild.setBmi(input.getBmi());
        existingChild.setBoneAge(input.getBoneAge());
        existingChild.setTestDate(input.getTestDate());

        int result = childMapper.updateById(existingChild);
        if (result > 0) {
            Child updatedChild = childMapper.getChildById(id);
            return childConverter.toDetail(updatedChild);
        }
        throw new BusinessException("更新儿童信息失败");
    }

    /**
     * 删除儿童信息
     */
    @Transactional
    public void deleteChild(Long id) {
        Child existingChild = childMapper.getChildById(id);
        if (existingChild == null) {
            throw new BusinessException("儿童信息不存在");
        }
        
        int result = childMapper.deleteById(id);
        if (result <= 0) {
            throw new BusinessException("删除儿童信息失败");
        }
    }
} 