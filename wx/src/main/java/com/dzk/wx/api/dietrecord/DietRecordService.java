package com.dzk.wx.api.dietrecord;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.wx.api.dietrecord.ai.DietAgentTool;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
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
        
        // 如果是对象，先标准化数值字段（AI 可能返回逗号分隔的多个数值）
        try {
            JsonElement element = JsonParser.parseString(cleanedJson);
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                normalizeNumberField(obj, "quantity");
                normalizeNumberField(obj, "calories");
                normalizeNumberField(obj, "protein");
                normalizeNumberField(obj, "carbohydrate");
                normalizeNumberField(obj, "fat");
                normalizeNumberField(obj, "fiber");
                cleanedJson = gson.toJson(obj);
            }
        } catch (Exception e) {
            logger.warn("数值标准化时解析 JSON 失败，跳过标准化: {}", e.getMessage());
        }

        // 解析为 JsonElement，按类型处理
        try {
            JsonElement element = JsonParser.parseString(cleanedJson);
            if (element.isJsonObject()) {
                return gson.fromJson(element, DietRecordDto.Input.class);
            }
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                String unescapedJson = element.getAsString();
                logger.debug("解析到字符串形式的 JSON，继续反序列化: {}", unescapedJson);
                return gson.fromJson(unescapedJson, DietRecordDto.Input.class);
            }
            logger.error("无法识别的 JSON 结构: {}", cleanedJson);
            throw new RuntimeException("AI返回的JSON结构不正确: " + cleanedJson);
        } catch (Exception e) {
            logger.error("解析JSON失败，原始内容: {}", jsonResult, e);
            throw new RuntimeException("AI返回的JSON格式不正确: " + jsonResult + ", 错误: " + e.getMessage(), e);
        }
    }

    /**
     * 将逗号分隔或包含多个数字的字符串，取第一个数字并写回对象
     */
    private void normalizeNumberField(JsonObject obj, String field) {
        if (!obj.has(field) || obj.get(field).isJsonNull()) {
            return;
        }
        try {
            String raw = obj.get(field).getAsString();
            if (raw == null || raw.isEmpty()) {
                return;
            }
            // 按逗号分割，取第一段，并截取首个数字串
            String firstPart = raw.split(",")[0].trim();
            String number = firstPart.replaceAll("[^0-9.\\-]", "");
            if (number.isEmpty()) {
                return;
            }
            obj.addProperty(field, number);
        } catch (Exception e) {
            // 保持原值，避免影响其它字段
            logger.warn("数值字段标准化失败: {}, error: {}", field, e.getMessage());
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
    public DietRecordDto.Detail addQuickRecordByUrls(Long childId, List<String> imageList, String mealType, LocalDate recordDate, LocalTime recordTime,String prompt) {
        if (imageList == null || imageList.isEmpty()) {
            throw new RuntimeException("图片列表不能为空");
        }

        // 调用AI识别
        String jsonResult = dietAgentTool.recognizeDietByUrls(imageList, mealType, recordDate,prompt);
        
        // 解析JSON为Input对象
        DietRecordDto.Input recordInput = parseJsonResult(jsonResult);

        recordInput.setRecordTime(recordTime);
        // 设置childId和recordDate（如果未设置）
        if (recordInput.getChildId() == null) {
            recordInput.setChildId(childId);
        }
        if (recordInput.getRecordDate() == null && recordDate != null) {
            recordInput.setRecordDate(recordDate);
        }
        if(recordInput.getRecordTime()==null && recordTime!=null){
            recordInput.setRecordTime(recordTime);
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

