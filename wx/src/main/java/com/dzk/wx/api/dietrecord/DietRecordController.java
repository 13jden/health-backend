package com.dzk.wx.api.dietrecord;

import com.dzk.common.common.Result;
import com.dzk.wx.util.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("diet-record")
@Tag(name = "饮食记录管理", description = "儿童饮食记录相关接口")
public class DietRecordController {

    @Autowired
    private DietRecordService dietRecordService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @PostMapping("/upload")
    @Operation(summary = "上传饮食图片", description = "上传饮食图片到服务器，返回图片URL")
    public Result<String> uploadImage(
            @RequestParam(value = "image", required = true) MultipartFile image) {
        try {
            String imageUrl = fileUploadUtil.uploadDietImage(image);
            return Result.success(imageUrl);
        } catch (IOException e) {
            return Result.error("上传图片失败: " + e.getMessage());
        }
    }


    @PostMapping("/{childId}")
    @Operation(summary = "快速（图片URL）添加饮食记录", description = "根据儿童ID和图片URL列表快速添加饮食记录")
    public Result<DietRecordDto.Detail> addQuickRecordByUrls(
            @PathVariable Long childId,
            @RequestBody DietRecordDto.QuickInput quickInput) {
        DietRecordDto.Detail detail = dietRecordService.addQuickRecordByUrls(childId, quickInput.getImageList(), quickInput.getMealType(), quickInput.getRecordDate(),quickInput.getRecordTime());
        return Result.success(detail);
    }
    @PostMapping
    @Operation(summary = "添加饮食记录", description = "为儿童添加新的饮食记录")
    public Result<DietRecordDto.Detail> addRecord(@RequestBody DietRecordDto.Input input) {
        DietRecordDto.Detail detail = dietRecordService.addRecord(input);
        return Result.success(detail);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取饮食记录详情", description = "根据ID获取饮食记录详情")
    public Result<DietRecordDto.Detail> getRecord(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        DietRecordDto.Detail detail = dietRecordService.getRecordById(id);
        return Result.success(detail);
    }

    @GetMapping("/child/{childId}")
    @Operation(summary = "获取儿童饮食记录列表", description = "根据儿童ID获取所有饮食记录")
    public Result<List<DietRecordDto.Detail>> getRecordsByChildId(
            @Parameter(description = "儿童ID") @PathVariable Long childId) {
        List<DietRecordDto.Detail> details = dietRecordService.getRecordsByChildId(childId);
        return Result.success(details);
    }

    @GetMapping("/child/{childId}/date/{recordDate}")
    @Operation(summary = "获取儿童指定日期的饮食记录", description = "根据儿童ID和日期获取饮食记录")
    public Result<List<DietRecordDto.Detail>> getRecordsByChildIdAndDate(
            @Parameter(description = "儿童ID") @PathVariable Long childId,
            @Parameter(description = "记录日期") @PathVariable LocalDate recordDate) {
        List<DietRecordDto.Detail> details = dietRecordService.getRecordsByChildIdAndDate(childId, recordDate);
        return Result.success(details);
    }

    @GetMapping("/child/{childId}/latest")
    @Operation(summary = "获取儿童最新饮食记录", description = "获取儿童最新的几条饮食记录")
    public Result<List<DietRecordDto.Detail>> getLatestRecords(
            @Parameter(description = "儿童ID") @PathVariable Long childId,
            @Parameter(description = "记录数量") @RequestParam(defaultValue = "5") Integer limit) {
        List<DietRecordDto.Detail> details = dietRecordService.getLatestRecordsByChildId(childId, limit);
        return Result.success(details);
    }

    @GetMapping("/child/{childId}/meal/{mealType}/date/{recordDate}")
    @Operation(summary = "获取儿童指定餐次的饮食记录", description = "根据儿童ID、餐次类型和日期获取饮食记录")
    public Result<List<DietRecordDto.Detail>> getRecordsByChildIdAndMealTypeAndDate(
            @Parameter(description = "儿童ID") @PathVariable Long childId,
            @Parameter(description = "餐次类型") @PathVariable String mealType,
            @Parameter(description = "记录日期") @PathVariable LocalDate recordDate) {
            List<DietRecordDto.Detail> details = dietRecordService.getRecordsByChildIdAndMealTypeAndDate(childId, mealType, recordDate);
            return Result.success(details);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新饮食记录", description = "更新指定ID的饮食记录")
    public Result<DietRecordDto.Detail> updateRecord(
            @Parameter(description = "记录ID") @PathVariable Long id,
            @RequestBody DietRecordDto.Input input) {
            DietRecordDto.Detail detail = dietRecordService.updateRecord(id, input);
            return Result.success(detail);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除饮食记录", description = "删除指定ID的饮食记录")
    public Result<String> deleteRecord(
            @Parameter(description = "记录ID") @PathVariable Long id) {
            dietRecordService.deleteRecord(id);
            return Result.success("删除成功");
    }

    @DeleteMapping("/child/{childId}")
    @Operation(summary = "删除儿童所有饮食记录", description = "删除指定儿童的所有饮食记录")
    public Result<String> deleteRecordsByChildId(
            @Parameter(description = "儿童ID") @PathVariable Long childId) {
            dietRecordService.deleteRecordsByChildId(childId);
            return Result.success("删除成功");
    }
}

