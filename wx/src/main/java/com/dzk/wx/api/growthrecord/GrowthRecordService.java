package com.dzk.wx.api.growthrecord;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.wx.api.dietrecord.DietRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrowthRecordService extends ServiceImpl<GrowthRecordMapper, GrowthRecord> {

    @Autowired
    private GrowthRecordMapper growthRecordMapper;

    @Autowired
    private GrowthRecordConverter growthRecordConverter;

    /**
     * 添加生长记录
     */
    @Transactional
    public GrowthRecordDto.Detail addRecord(GrowthRecordDto.Input input) {
        GrowthRecord record = growthRecordConverter.toEntity(input);
        int result = growthRecordMapper.insert(record);
        if (result > 0) {
            GrowthRecord savedRecord = growthRecordMapper.getRecordById(record.getId());
            return growthRecordConverter.toDetail(savedRecord);
        }
        throw new RuntimeException("添加生长记录失败");
    }

    /**
     * 根据ID获取生长记录详情
     */
    public GrowthRecordDto.Detail getRecordById(Long id) {
        GrowthRecord record = growthRecordMapper.getRecordById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在");
        }
        return growthRecordConverter.toDetail(record);
    }

    /**
     * 根据儿童ID获取生长记录列表
     */
    public List<GrowthRecordDto.Detail> getRecordsByChildId(Long childId) {
        List<GrowthRecord> records = growthRecordMapper.getRecordsByChildId(childId);
        return records.stream()
                .map(growthRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 获取儿童最新的几条记录
     */
    public List<GrowthRecordDto.Detail> getLatestRecordsByChildId(Long childId, Integer limit) {
        List<GrowthRecord> records = growthRecordMapper.getLatestRecordsByChildId(childId, limit);
        return records.stream()
                .map(growthRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 更新生长记录
     */
    @Transactional
    public GrowthRecordDto.Detail updateRecord(Long id, GrowthRecordDto.Input input) {
        GrowthRecord existingRecord = growthRecordMapper.getRecordById(id);
        if (existingRecord == null) {
            throw new RuntimeException("记录不存在");
        }

        // 更新字段
        existingRecord.setChildId(input.getChildId());
        existingRecord.setHeight(input.getHeight());
        existingRecord.setWeight(input.getWeight());
        existingRecord.setBmi(input.getBmi());
        existingRecord.setBoneAge(input.getBoneAge());
        existingRecord.setTestDate(input.getTestDate());

        int result = growthRecordMapper.updateById(existingRecord);
        if (result > 0) {
            GrowthRecord updatedRecord = growthRecordMapper.getRecordById(id);
            return growthRecordConverter.toDetail(updatedRecord);
        }
        throw new RuntimeException("更新记录失败");
    }

    /**
     * 删除生长记录
     */
    @Transactional
    public void deleteRecord(Long id) {
        GrowthRecord existingRecord = growthRecordMapper.getRecordById(id);
        if (existingRecord == null) {
            throw new RuntimeException("记录不存在");
        }
        
        int result = growthRecordMapper.deleteById(id);
        if (result <= 0) {
            throw new RuntimeException("删除记录失败");
        }
    }

    /**
     * 根据儿童ID删除所有记录
     */
    @Transactional
    public void deleteRecordsByChildId(Long childId) {
        int result = growthRecordMapper.delete(
                new QueryWrapper<GrowthRecord>().eq("child_id", childId)
        );

        if (result < 0) {
            throw new RuntimeException("删除记录失败");
        }
    }
}
