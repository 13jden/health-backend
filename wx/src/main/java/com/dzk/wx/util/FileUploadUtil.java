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

    @Value("${upload.commonPath}")
    private String commonPath;

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

    /**
     * 上传通用文件到服务器
     * @param file 上传的文件
     * @return 文件的访问URL
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 确保目录存在
        Path uploadPath = Paths.get(commonPath);
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
        String relativePath = dateDir + "/" + fileName;
        // URL格式: hostUrl/files/yyyy/MM/dd/filename
        return hostUrl + "/files/" + relativePath;
    }

    /**
     * 根据文件路径获取文件
     * @param filePath 文件路径（相对于commonPath的路径，或完整URL）
     * @return 文件对象
     */
    public File getFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        Path path;
        String basePath;
        String relativePath;

        // 如果是URL格式，提取路径部分
        if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
            // 从URL中提取路径
            String urlPath = filePath;
            int filesIndex = urlPath.indexOf("/files/");
            if (filesIndex == -1) {
                throw new IllegalArgumentException("无效的文件URL格式");
            }
            String pathAfterFiles = urlPath.substring(filesIndex + "/files/".length());
            
            // 检查是否是饮食图片路径 /files/diet/
            if (pathAfterFiles.startsWith("diet/")) {
                // 饮食图片路径
                relativePath = pathAfterFiles.substring("diet/".length());
                basePath = dietPath;
            } else {
                // 通用文件路径
                relativePath = pathAfterFiles;
                basePath = commonPath;
            }
            path = Paths.get(basePath, relativePath);
        } else {
            // 直接使用相对路径，检查是否以 diet/ 开头
            if (filePath.startsWith("diet/")) {
                relativePath = filePath.substring("diet/".length());
                basePath = dietPath;
            } else {
                relativePath = filePath;
                basePath = commonPath;
            }
            path = Paths.get(basePath, relativePath);
        }

        File file = path.toFile();
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("文件不存在: " + filePath);
        }
        return file;
    }
}

