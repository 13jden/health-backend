package com.dzk.wx.api.child;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChildService extends ServiceImpl<ChildMapper, Child> {

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private ChildConverter childConverter;

    public List<Child> getChildrenByParentId(Long parentId) {
        return childMapper.getChildrenByParentId(parentId);
    }

    public Child getChildById(Long id) {
        return childMapper.getChildById(id);
    }

    public Child saveChild(Child child) {
        int result = childMapper.insert(child);
        return childMapper.getChildById(child.getId());
    }

    public boolean updateChild(Child child) {
        return childMapper.updateById(child) > 0;
    }

    public boolean deleteChild(Long id) {
        return childMapper.deleteById(id) > 0;
    }
} 