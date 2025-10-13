package com.dzk.wx.api.child;

import com.dzk.common.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("child")
@Tag(name = "儿童管理", description = "儿童信息相关接口")
public class ChildController {

    @Autowired
    private ChildService childService;

    @Autowired
    private ChildConverter childConverter;

    @PostMapping
    @Operation(summary = "添加儿童信息", description = "为家长添加新的儿童信息")
    public Result<ChildDto.Detail> addChild(@RequestBody ChildDto.Input input) {
        ChildDto.Detail detail = childService.addChild(input);
        return Result.success(detail);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取儿童详情", description = "根据ID获取儿童详情")
    public Result<ChildDto.Detail> getChild(
            @Parameter(description = "儿童ID") @PathVariable Long id) {
        ChildDto.Detail detail = childService.getChildById(id);
        return Result.success(detail);
    }

    @GetMapping("/parent/{parentId}")
    @Operation(summary = "获取家长的所有儿童", description = "根据家长ID获取所有儿童信息")
    public Result<List<ChildDto.Detail>> getChildrenByParentId(
            @Parameter(description = "家长ID") @PathVariable Long parentId) {
        List<ChildDto.Detail> details = childService.getChildrenByParentId(parentId);
        return Result.success(details);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新儿童信息", description = "更新指定ID的儿童信息")
    public Result<ChildDto.Detail> updateChild(
            @Parameter(description = "儿童ID") @PathVariable Long id,
            @RequestBody ChildDto.Input input) {
        ChildDto.Detail detail = childService.updateChild(id, input);
        return Result.success(detail);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除儿童信息", description = "删除指定ID的儿童信息")
    public Result<String> deleteChild(
            @Parameter(description = "儿童ID") @PathVariable Long id) {
        childService.deleteChild(id);
        return Result.success("删除成功");
    }
} 