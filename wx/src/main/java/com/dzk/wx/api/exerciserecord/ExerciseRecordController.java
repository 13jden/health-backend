package com.dzk.wx.api.exerciserecord;

import com.dzk.common.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("exercise-record")
@Tag(name = "运动记录管理", description = "儿童运动记录相关接口")
public class ExerciseRecordController {

    @Autowired
    private ExerciseRecordService exerciseRecordService;

    @PostMapping
    @Operation(summary = "添加运动记录", description = "为儿童添加新的运动记录")
    public Result<ExerciseRecordDto.Detail> addRecord(@RequestBody ExerciseRecordDto.Input input) {
        ExerciseRecordDto.Detail detail = exerciseRecordService.addRecord(input);
        return Result.success(detail);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取运动记录详情", description = "根据ID获取运动记录详情")
    public Result<ExerciseRecordDto.Detail> getRecord(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        ExerciseRecordDto.Detail detail = exerciseRecordService.getRecordById(id);
        return Result.success(detail);
    }

    @GetMapping("/child/{childId}")
    @Operation(summary = "获取儿童运动记录列表", description = "根据儿童ID获取所有运动记录")
    public Result<List<ExerciseRecordDto.Detail>> getRecordsByChildId(
            @Parameter(description = "儿童ID") @PathVariable Long childId) {
        List<ExerciseRecordDto.Detail> details = exerciseRecordService.getRecordsByChildId(childId);
        return Result.success(details);
    }

    @GetMapping("/child/{childId}/date/{recordDate}")
    @Operation(summary = "获取儿童指定日期的运动记录", description = "根据儿童ID和日期获取运动记录")
    public Result<List<ExerciseRecordDto.Detail>> getRecordsByChildIdAndDate(
            @Parameter(description = "儿童ID") @PathVariable Long childId,
            @Parameter(description = "记录日期") @PathVariable LocalDate recordDate) {
        List<ExerciseRecordDto.Detail> details = exerciseRecordService.getRecordsByChildIdAndDate(childId, recordDate);
        return Result.success(details);
    }

    @GetMapping("/child/{childId}/latest")
    @Operation(summary = "获取儿童最新运动记录", description = "获取儿童最新的几条运动记录")
    public Result<List<ExerciseRecordDto.Detail>> getLatestRecords(
            @Parameter(description = "儿童ID") @PathVariable Long childId,
            @Parameter(description = "记录数量") @RequestParam(defaultValue = "5") Integer limit) {
        List<ExerciseRecordDto.Detail> details = exerciseRecordService.getLatestRecordsByChildId(childId, limit);
        return Result.success(details);
    }

    @GetMapping("/child/{childId}/type/{exerciseType}")
    @Operation(summary = "获取儿童指定类型的运动记录", description = "根据儿童ID和运动类型获取运动记录")
    public Result<List<ExerciseRecordDto.Detail>> getRecordsByChildIdAndExerciseType(
            @Parameter(description = "儿童ID") @PathVariable Long childId,
            @Parameter(description = "运动类型") @PathVariable String exerciseType) {
        List<ExerciseRecordDto.Detail> details = exerciseRecordService.getRecordsByChildIdAndExerciseType(childId, exerciseType);
        return Result.success(details);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新运动记录", description = "更新指定ID的运动记录")
    public Result<ExerciseRecordDto.Detail> updateRecord(
            @Parameter(description = "记录ID") @PathVariable Long id,
            @RequestBody ExerciseRecordDto.Input input) {
        ExerciseRecordDto.Detail detail = exerciseRecordService.updateRecord(id, input);
        return Result.success(detail);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除运动记录", description = "删除指定ID的运动记录")
    public Result<String > deleteRecord(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        exerciseRecordService.deleteRecord(id);
        return Result.success("删除成功");
    }

    @DeleteMapping("/child/{childId}")
    @Operation(summary = "删除儿童所有运动记录", description = "删除指定儿童的所有运动记录")
    public Result<String> deleteRecordsByChildId(
            @Parameter(description = "儿童ID") @PathVariable Long childId) {
        exerciseRecordService.deleteRecordsByChildId(childId);
        return Result.success("删除成功");
    }
}

