package com.dzk.wx.api.auth.captcha;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CaptchaDto {


    @Schema(description = "验证码ID")
    private String uuid;

    @Schema(description = "验证码图片")
    private String img;

    @Data
	@AllArgsConstructor
	@NoArgsConstructor
    public static class Input{
        @Schema(description = "验证码")
        private String code;

        @Schema(description = "验证码ID")
        private String uuid;
    }
}