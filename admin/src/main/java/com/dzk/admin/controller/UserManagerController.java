package com.dzk.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dzk.common.common.Result;
import com.dzk.wx.api.user.RoleEnum;
import com.dzk.wx.api.user.UserDto;
import com.dzk.wx.api.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
@Tag(name = "用户管理", description = "管理员用户管理相关接口")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "创建用户", description = "管理员创建新用户")
    public Result<UserDto> createUser(@RequestBody UserDto.Input input) {
        UserDto userDto = userService.createUser(input);
        return Result.success(userDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详情")
    public Result<UserDto> getUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return Result.success(userDto);
    }

    @GetMapping
    @Operation(summary = "获取用户（患者）列表", description = "分页获取用户列表，支持按用户名、电话、角色筛选")
    public Result<IPage<UserDto>> getUserList(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名（模糊查询）") @RequestParam(required = false) String username,
            @Parameter(description = "电话（模糊查询）") @RequestParam(required = false) String phone,
            @Parameter(description = "角色") @RequestParam(required = false) RoleEnum role) {
        IPage<UserDto> userPage = userService.getUserList(current, size, username, phone, role);
        return Result.success(userPage);
    }

    @GetMapping("/patients")
    @Operation(summary = "获取患者列表", description = "分页获取患者列表（儿童+家长信息）")
    public Result<IPage<UserDto.ManagerDto>> getPatientList(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "儿童姓名（模糊查询）") @RequestParam(required = false) String name,
            @Parameter(description = "家长电话（模糊查询）") @RequestParam(required = false) String phone) {
        IPage<UserDto.ManagerDto> patientPage = userService.getPatientList(current, size, name, phone);
        return Result.success(patientPage);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有用户列表", description = "获取所有用户列表（不分页）")
    public Result<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return Result.success(users);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息", description = "更新指定ID的用户信息")
    public Result<UserDto> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody UserDto.Input input) {
        UserDto userDto = userService.updateUser(id, input);
        return Result.success(userDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除指定ID的用户")
    public Result<String> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }
}

