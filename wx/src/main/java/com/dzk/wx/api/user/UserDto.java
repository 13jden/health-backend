package com.dzk.wx.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "用户数据传输对象")
public class UserDto {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "用户名（家长姓名）")
    private String username;
    
    @Schema(description = "微信openId")
    private String openId;
    
    @Schema(description = "用户头像")
    private String avatar;
    
    @Schema(description = "用户角色")
    private RoleEnum role;
    
    @Schema(description = "JWT认证令牌")
    private String token;
    
    @Schema(description = "用户权限信息")
    private String authorities;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "电话号码")
    private String phone;


    @Data
    @Schema(description = "管理端患者dto")
    public static class ManagerDto extends UserDto{

        @Schema(description = "性别")
        private String gender;

        @Schema(description = "出生日期")
        private LocalDate birthdate;

        @Schema(description = "身高(cm)")
        private BigDecimal height;

        @Schema(description = "体重(kg)")
        private BigDecimal weight;

        @Schema(description = "BMI指数")
        private BigDecimal bmi;

        @Schema(description = "儿童姓名")
        private String name;

        @Schema(description = "儿童ID")
        private Long childId;

    }

    @Data
    @Schema(description = "用户输入数据传输对象")
    public static class Input {
        @Schema(description = "用户名")
        private String username;

        @Schema(description = "密码")
        private String password;

        @Schema(description = "电话号码")
        private String phone;
        
        @Schema(description = "用户头像")
        private String avatar;

        @Schema(description = "部门")
        private String department;

        @Schema(description = "邮箱")
        private String email;

        @Schema(description = "用户角色")
        private RoleEnum role;

        @Schema(description = "微信获取openId的code")
        private String code;
    }
}
