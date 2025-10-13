package com.dzk.wx.api.growthrecord;

import com.dzk.common.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("growth-record")
@Tag(name = "生长记录管理", description = "儿童生长记录相关接口")
public class GrowthRecordController {

    @Autowired
    private GrowthRecordService growthRecordService;

    @Autowired
    private GrowthRecordConverter growthRecordConverter;

    @PostMapping
    @Operation(summary = "添加生长记录", description = "为指定儿童添加新的生长记录")
    public Result<GrowthRecordDto.Detail> addRecord(@RequestBody GrowthRecordDto.Input input) {
        GrowthRecordDto.Detail detail = growthRecordService.addRecord(input);
        return Result.success(detail);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取生长记录详情", description = "根据ID获取生长记录详情")
    public Result<GrowthRecordDto.Detail> getRecord(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        GrowthRecordDto.Detail detail = growthRecordService.getRecordById(id);
        return Result.success(detail);
    }

    @GetMapping("/child/{childId}")
    @Operation(summary = "获取儿童生长记录列表", description = "根据儿童ID获取所有生长记录")
    public Result<List<GrowthRecordDto.Detail>> getRecordsByChildId(
            @Parameter(description = "儿童ID") @PathVariable Long childId) {
        List<GrowthRecordDto.Detail> details = growthRecordService.getRecordsByChildId(childId);
        return Result.success(details);
    }

    @GetMapping("/child/{childId}/latest")
    @Operation(summary = "获取儿童最新记录", description = "获取儿童最新的几条生长记录")
    public Result<List<GrowthRecordDto.Detail>> getLatestRecords(
            @Parameter(description = "儿童ID") @PathVariable Long childId,
            @Parameter(description = "记录数量") @RequestParam(defaultValue = "5") Integer limit) {
        List<GrowthRecordDto.Detail> details = growthRecordService.getLatestRecordsByChildId(childId, limit);
        return Result.success(details);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新生长记录", description = "更新指定ID的生长记录")
    public Result<GrowthRecordDto.Detail> updateRecord(
            @Parameter(description = "记录ID") @PathVariable Long id,
            @RequestBody GrowthRecordDto.Input input) {
        GrowthRecordDto.Detail detail = growthRecordService.updateRecord(id, input);
        return Result.success(detail);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除生长记录", description = "删除指定ID的生长记录")
    public Result<String> deleteRecord(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        growthRecordService.deleteRecord(id);
        return Result.success("删除成功");
    }

    @DeleteMapping("/child/{childId}")
    @Operation(summary = "删除儿童所有记录", description = "删除指定儿童的所有生长记录")
    public Result<String> deleteRecordsByChildId(
            @Parameter(description = "儿童ID") @PathVariable Long childId) {
        growthRecordService.deleteRecordsByChildId(childId);
        return Result.success("删除成功");
    }
}
