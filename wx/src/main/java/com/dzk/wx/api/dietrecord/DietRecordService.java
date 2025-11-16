package com.dzk.wx.api.dietrecord;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.wx.api.dietrecord.ai.DietAgentTool;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietRecordService extends ServiceImpl<DietRecordMapper, DietRecord> {

    private static final Logger logger = LoggerFactory.getLogger(DietRecordService.class);

    @Autowired
    private DietRecordMapper dietRecordMapper;

    @Autowired
    private DietRecordConverter dietRecordConverter;

    @Autowired
    private DietAgentTool dietAgentTool;

    @Autowired
    private Gson gson;


    
    /**
     * 添加饮食记录
     */
    @Transactional
    public DietRecordDto.Detail addRecord(DietRecordDto.Input input) {
        DietRecord record = dietRecordConverter.toEntity(input);
        int result = dietRecordMapper.insert(record);
        if (result > 0) {
            DietRecord savedRecord = dietRecordMapper.getRecordById(record.getId());
            return dietRecordConverter.toDetail(savedRecord);
        }
        throw new RuntimeException("添加饮食记录失败");
    }

    /**
     * 根据ID获取饮食记录详情
     */
    public DietRecordDto.Detail getRecordById(Long id) {
        DietRecord record = dietRecordMapper.getRecordById(id);
        if (record == null) {
            throw new RuntimeException("饮食记录不存在");
        }
        return dietRecordConverter.toDetail(record);
    }

    /**
     * 根据儿童ID获取饮食记录列表
     */
    public List<DietRecordDto.Detail> getRecordsByChildId(Long childId) {
        List<DietRecord> records = dietRecordMapper.getRecordsByChildId(childId);
        return records.stream()
                .map(dietRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 根据儿童ID和日期获取饮食记录
     */
    public List<DietRecordDto.Detail> getRecordsByChildIdAndDate(Long childId, LocalDate recordDate) {
        List<DietRecord> records = dietRecordMapper.getRecordsByChildIdAndDate(childId, recordDate);
        return records.stream()
                .map(dietRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 获取儿童最新的几条饮食记录
     */
    public List<DietRecordDto.Detail> getLatestRecordsByChildId(Long childId, Integer limit) {
        List<DietRecord> records = dietRecordMapper.getLatestRecordsByChildId(childId, limit);
        return records.stream()
                .map(dietRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 根据儿童ID、餐次类型和日期获取饮食记录
     */
    public List<DietRecordDto.Detail> getRecordsByChildIdAndMealTypeAndDate(Long childId, String mealType, LocalDate recordDate) {
        List<DietRecord> records = dietRecordMapper.getRecordsByChildIdAndMealTypeAndDate(childId, mealType, recordDate);
        return records.stream()
                .map(dietRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 更新饮食记录
     */
    @Transactional
    public DietRecordDto.Detail updateRecord(Long id, DietRecordDto.Input input) {
        DietRecord existingRecord = dietRecordMapper.getRecordById(id);
        if (existingRecord == null) {
            throw new RuntimeException("饮食记录不存在");
        }

        // 更新字段
        existingRecord.setChildId(input.getChildId());
        existingRecord.setMealType(input.getMealType());
        existingRecord.setFoodName(input.getFoodName());
        existingRecord.setFoodCategory(input.getFoodCategory());
        existingRecord.setQuantity(input.getQuantity());
        existingRecord.setUnit(input.getUnit());
        existingRecord.setCalories(input.getCalories());
        existingRecord.setProtein(input.getProtein());
        existingRecord.setCarbohydrate(input.getCarbohydrate());
        existingRecord.setFat(input.getFat());
        existingRecord.setFiber(input.getFiber());
        existingRecord.setRecordDate(input.getRecordDate());
        existingRecord.setRecordTime(input.getRecordTime());
        existingRecord.setNotes(input.getNotes());
        existingRecord.setImageList(input.getImageList());

        int result = dietRecordMapper.updateById(existingRecord);
        if (result > 0) {
            DietRecord updatedRecord = dietRecordMapper.getRecordById(id);
            return dietRecordConverter.toDetail(updatedRecord);
        }
        throw new RuntimeException("更新饮食记录失败");
    }

    /**
     * 删除饮食记录
     */
    @Transactional
    public void deleteRecord(Long id) {
        DietRecord existingRecord = dietRecordMapper.getRecordById(id);
        if (existingRecord == null) {
            throw new RuntimeException("饮食记录不存在");
        }
        
        int result = dietRecordMapper.deleteById(id);
        if (result <= 0) {
            throw new RuntimeException("删除饮食记录失败");
        }
    }

    /**
     * 根据儿童ID删除所有饮食记录
     */
    @Transactional
    public void deleteRecordsByChildId(Long childId) {
        int result = dietRecordMapper.delete(
                new QueryWrapper<DietRecord>().eq("child_id", childId)
        );
        if (result < 0) {
            throw new RuntimeException("删除饮食记录失败");
        }
    }

    /**
     * 解析AI返回的JSON字符串为Input对象
     * 处理字符串化的JSON（转义的JSON字符串）和Markdown代码块
     */
    private DietRecordDto.Input parseJsonResult(String jsonResult) {
        if (jsonResult == null || jsonResult.trim().isEmpty()) {
            throw new RuntimeException("AI返回的JSON结果为空");
        }
        
        logger.debug("解析AI返回的JSON: {}", jsonResult);
        
        // 清理Markdown代码块标记（```json 和 ```）
        String cleanedJson = jsonResult.trim();
        if (cleanedJson.startsWith("```")) {
            // 移除开头的 ```json 或 ```
            int startIndex = cleanedJson.indexOf("\n");
            if (startIndex > 0) {
                cleanedJson = cleanedJson.substring(startIndex + 1);
            } else {
                cleanedJson = cleanedJson.replaceFirst("```[a-zA-Z]*", "");
            }
        }
        if (cleanedJson.endsWith("```")) {
            // 移除结尾的 ```
            cleanedJson = cleanedJson.substring(0, cleanedJson.lastIndexOf("```")).trim();
        }
        cleanedJson = cleanedJson.trim();
        
        logger.debug("清理后的JSON: {}", cleanedJson);
        
        // 先尝试直接解析为对象
        try {
            return gson.fromJson(cleanedJson, DietRecordDto.Input.class);
        } catch (JsonSyntaxException e) {
            logger.debug("直接解析失败，尝试解析为字符串化的JSON: {}", e.getMessage());
            
            // 如果直接解析失败，可能是字符串化的JSON（JSON字符串被转义在另一个字符串中）
            try {
                // 先解析为字符串（去除转义）
                String unescapedJson = gson.fromJson(cleanedJson, String.class);
                logger.debug("解析后的JSON字符串: {}", unescapedJson);
                // 再解析为对象
                return gson.fromJson(unescapedJson, DietRecordDto.Input.class);
            } catch (Exception e2) {
                logger.error("解析JSON失败，原始内容: {}", jsonResult, e2);
                throw new RuntimeException("AI返回的JSON格式不正确: " + jsonResult + ", 错误: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 快速AI识别图片添加饮食记录
     */
    @Transactional
    public DietRecordDto.Detail addQuickRecord(Long childId, MultipartFile image, String mealType, LocalDate recordDate) {
        if (image == null || image.isEmpty()) {
            throw new RuntimeException("图片不能为空");
        }

        // 调用AI识别
        String jsonResult = dietAgentTool.recognizeDiet(image, mealType, recordDate);
        
        // 解析JSON为Input对象
        DietRecordDto.Input recordInput = parseJsonResult(jsonResult);
        
        // 设置childId和recordDate（如果未设置）
        if (recordInput.getChildId() == null) {
            recordInput.setChildId(childId);
        }
        if (recordInput.getRecordDate() == null && recordDate != null) {
            recordInput.setRecordDate(recordDate);
        }
        if (recordInput.getMealType() == null && mealType != null) {
            recordInput.setMealType(mealType);
        }
        
        // 调用addRecord插入记录
        return addRecord(recordInput);
    }

    /**
     * 通过图片URL列表快速AI识别添加饮食记录
     */
    @Transactional
    public DietRecordDto.Detail addQuickRecordByUrls(Long childId, List<String> imageList, String mealType, LocalDate recordDate) {
        if (imageList == null || imageList.isEmpty()) {
            throw new RuntimeException("图片列表不能为空");
        }

        // 调用AI识别
        String jsonResult = dietAgentTool.recognizeDietByUrls(imageList, mealType, recordDate);
        
        // 解析JSON为Input对象
        DietRecordDto.Input recordInput = parseJsonResult(jsonResult);
        
        // 设置childId和recordDate（如果未设置）
        if (recordInput.getChildId() == null) {
            recordInput.setChildId(childId);
        }
        if (recordInput.getRecordDate() == null && recordDate != null) {
            recordInput.setRecordDate(recordDate);
        }
        if (recordInput.getMealType() == null && mealType != null) {
            recordInput.setMealType(mealType);
        }
        // 设置图片列表
        recordInput.setImageList(imageList);
        
        // 调用addRecord插入记录
        return addRecord(recordInput);
    }
}

