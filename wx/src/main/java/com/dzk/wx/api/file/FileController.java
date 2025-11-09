package com.dzk.wx.api.file;

import com.dzk.common.common.Result;
import com.dzk.wx.util.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("file")
@Tag(name = "文件管理", description = "文件上传和下载相关接口")
public class FileController {

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到服务器，返回文件URL")
    public Result<String> uploadFile(
            @Parameter(description = "上传的文件") @RequestParam(value = "file", required = true) MultipartFile file) {
        try {
            String fileUrl = fileUploadUtil.uploadFile(file);
            return Result.success(fileUrl);
        } catch (IOException e) {
            return Result.error("上传文件失败: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/download")
    @Operation(summary = "下载文件", description = "根据文件路径或URL下载文件")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "文件路径或URL") @RequestParam(value = "path", required = true) String filePath) {
        try {
            java.io.File file = fileUploadUtil.getFile(filePath);
            Resource resource = new FileSystemResource(file);

            // 获取文件名
            String filename = file.getName();
            // 尝试从原始文件名获取，如果URL中包含文件名信息
            if (filePath.contains("/")) {
                String[] parts = filePath.split("/");
                if (parts.length > 0) {
                    String lastPart = parts[parts.length - 1];
                    // 移除可能的查询参数
                    if (lastPart.contains("?")) {
                        lastPart = lastPart.substring(0, lastPart.indexOf("?"));
                    }
                    if (!lastPart.isEmpty()) {
                        filename = lastPart;
                    }
                }
            }

            // URL编码文件名，支持中文
            String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + encodedFilename)
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
