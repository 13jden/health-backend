package com.dzk.wx.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${file.diet.path}")
    private String dietPath;

    @Value("${host.url}")
    private String hostUrl;

    /**
     * 上传饮食图片到服务器
     * @param file 上传的文件
     * @return 图片的访问URL
     */
    public String uploadDietImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 确保目录存在
        Path uploadPath = Paths.get(dietPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        // 按日期创建子目录
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path datePath = uploadPath.resolve(dateDir);
        if (!Files.exists(datePath)) {
            Files.createDirectories(datePath);
        }

        // 保存文件
        Path filePath = datePath.resolve(fileName);
        file.transferTo(filePath.toFile());

        // 返回访问URL
        // 计算相对于diet目录的路径
        String relativePath = dateDir + "/" + fileName;
        // URL格式: hostUrl/files/diet/yyyy/MM/dd/filename
        return hostUrl + "/files/diet/" + relativePath;
    }
}

