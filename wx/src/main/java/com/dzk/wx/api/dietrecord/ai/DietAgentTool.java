package com.dzk.wx.api.dietrecord.ai;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationOutput;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.dzk.wx.util.FileUploadUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Component
public class DietAgentTool {

    private static final Logger logger = LoggerFactory.getLogger(DietAgentTool.class);

    @Value("${spring.ai.dashscope.diet.app-id}")
    String appId;

    @Value("${spring.ai.dashscope.diet.api-key}")
    String apiKey;

    @Autowired
    private Gson gson;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 将图片 URL 转换为 base64 字符串
     *
     * @param imageUrl 图片 URL
     * @return base64 字符串（包含 data URI 前缀）
     */
    private String convertUrlToBase64(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("图片 URL 不能为空");
        }

        logger.debug("开始转换图片 URL 为 base64: {}", imageUrl);
        byte[] imageBytes;
        String mimeType = "image/jpeg"; // 默认 MIME 类型

        // 判断是本地文件 URL 还是外部 URL
        if (imageUrl.contains("/files/")) {
            // 本地文件，使用 FileUploadUtil 获取
            try {
                logger.debug("检测到本地文件 URL，尝试读取文件: {}", imageUrl);
                File file = fileUploadUtil.getFile(imageUrl);
                logger.debug("文件路径: {}, 文件存在: {}, 文件大小: {} bytes", 
                    file.getAbsolutePath(), file.exists(), file.length());
                
                if (!file.exists() || !file.isFile()) {
                    throw new IOException("文件不存在或不是有效文件: " + file.getAbsolutePath());
                }
                
                imageBytes = Files.readAllBytes(file.toPath());
                logger.debug("成功读取文件，大小: {} bytes", imageBytes.length);
                
                // 根据文件扩展名判断 MIME 类型
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(".png")) {
                    mimeType = "image/png";
                } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                    mimeType = "image/jpeg";
                } else if (fileName.endsWith(".gif")) {
                    mimeType = "image/gif";
                } else if (fileName.endsWith(".webp")) {
                    mimeType = "image/webp";
                }
                logger.debug("识别到 MIME 类型: {}", mimeType);
            } catch (Exception e) {
                logger.error("读取本地文件失败: {}, 错误信息: {}", imageUrl, e.getMessage(), e);
                throw new IOException("读取本地文件失败: " + e.getMessage(), e);
            }
        } else if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            // 外部 URL，需要下载
            logger.debug("检测到外部 URL，尝试下载: {}", imageUrl);
            try (InputStream inputStream = new URL(imageUrl).openStream()) {
                imageBytes = inputStream.readAllBytes();
                logger.debug("成功下载图片，大小: {} bytes", imageBytes.length);
                
                // 尝试从 URL 判断 MIME 类型
                String urlLower = imageUrl.toLowerCase();
                if (urlLower.contains(".png")) {
                    mimeType = "image/png";
                } else if (urlLower.contains(".jpg") || urlLower.contains(".jpeg")) {
                    mimeType = "image/jpeg";
                } else if (urlLower.contains(".gif")) {
                    mimeType = "image/gif";
                } else if (urlLower.contains(".webp")) {
                    mimeType = "image/webp";
                }
            } catch (Exception e) {
                logger.error("下载图片失败: {}, 错误信息: {}", imageUrl, e.getMessage(), e);
                throw new IOException("下载图片失败: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("不支持的图片 URL 格式: " + imageUrl);
        }

        // 转换为 base64
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        logger.debug("成功转换为 base64，长度: {} 字符", base64.length());
        return "data:" + mimeType + ";base64," + base64;
    }

    /**
     * 通过 DashScope Agent（应用）识别饮食记录图片（URL 列表），返回 JSON 字符串
     * 内部会将 URL 转换为 base64 后调用 API
     *
     * @param imageList  图片 URL 列表（参数名就叫 imageList）
     * @param mealType   餐次类型
     * @param recordDate 用餐日期
     * @param userPrompt 自定义提示词
     * @return JSON 字符串，包含解析后的饮食记录数据
     */
    public String recognizeDietByUrls(List<String> imageList, String mealType, LocalDate recordDate, String userPrompt) {
        if (imageList == null || imageList.isEmpty()) {
            throw new RuntimeException("图片列表不能为空");
        }

        try {
            // 业务参数：将 URL 列表转换为 base64 数组
            JsonObject bizParams = new JsonObject();
            JsonArray imageArray = new JsonArray();
            logger.info("开始处理 {} 张图片", imageList.size());
            for (int i = 0; i < imageList.size(); i++) {
                String imageUrl = imageList.get(i);
                try {
                    logger.info("处理第 {}/{} 张图片: {}", i + 1, imageList.size(), imageUrl);
                    // 将 URL 转换为 base64
                    String base64Data = convertUrlToBase64(imageUrl);
                    imageArray.add(base64Data);
                    logger.info("成功转换第 {}/{} 张图片为 base64", i + 1, imageList.size());
                } catch (Exception e) {
                    logger.error("转换第 {}/{} 张图片失败: {}, 错误: {}", i + 1, imageList.size(), imageUrl, e.getMessage(), e);
                    throw new RuntimeException("转换图片失败: " + imageUrl + ", 错误: " + e.getMessage(), e);
                }
            }
            bizParams.add("imageList", imageArray);
            // 业务参数里的 prompt 若需要传递，使用 addProperty 确保类型正确
            bizParams.addProperty("prompt", userPrompt != null ? userPrompt : "");
            logger.info("所有图片转换完成，准备调用 DashScope API");

            // 构建提示词
            StringBuilder promptBuilder = new StringBuilder();
            if (userPrompt != null) {
                promptBuilder.append(userPrompt);
            }
            if (mealType != null && !mealType.isEmpty()) {
                promptBuilder.append("餐次类型：").append(mealType).append("。");
            }
            if (recordDate != null) {
                promptBuilder.append("用餐日期：")
                        .append(recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        .append("。");
            }
            promptBuilder.append("只输出纯JSON格式，不要包含Markdown代码块标记（```json或```），不要其他文字说明。");

            String prompt = promptBuilder.toString();
            logger.debug("饮食识别提示词: {}", prompt);

            // 构建 Agent 调用参数
            ApplicationParam param = ApplicationParam.builder()
                    .apiKey(apiKey)
                    .appId(appId)
                    .prompt(prompt)
                    .bizParams(bizParams)
                    .incrementalOutput(true)
                    .hasThoughts(true)
                    .build();

            Application application = new Application();
            Flowable<ApplicationResult> resultFlowable;
            try {
                logger.info("开始调用 DashScope API，appId: {}", appId);
                resultFlowable = application.streamCall(param);
            } catch (NoApiKeyException | InputRequiredException e) {
                logger.error("DashScope API 调用异常: {}", e.getMessage(), e);
                throw new RuntimeException("饮食 AI 调用异常: " + e.getMessage(), e);
            }

            // 收集最终结果
            logger.info("等待 DashScope API 返回结果...");
            ApplicationResult finalResult = resultFlowable.blockingLast();
            logger.info("收到 DashScope API 返回结果");
            
            if (finalResult == null) {
                logger.error("DashScope API 返回结果为空");
                throw new RuntimeException("饮食 AI 返回结果为空");
            }
            
            if (finalResult.getOutput() == null) {
                logger.error("DashScope API 返回的 output 为空");
                throw new RuntimeException("饮食 AI 返回结果为空");
            }
            
            ApplicationOutput output = finalResult.getOutput();
            logger.debug("DashScope API 返回的 output.text: {}", output.getText());
            logger.debug("DashScope API 返回的 thoughts 数量: {}", 
                output.getThoughts() != null ? output.getThoughts().size() : 0);

            // 优先从 thoughts.nodeResult.result 中取
            if (output.getThoughts() != null && !output.getThoughts().isEmpty()) {
                logger.debug("尝试从 thoughts 中提取结果");
                for (int i = 0; i < output.getThoughts().size(); i++) {
                    ApplicationOutput.Thought thought = output.getThoughts().get(i);
                    if (thought.getResponse() != null) {
                        logger.debug("处理第 {} 个 thought，response: {}", i + 1, thought.getResponse());
                        try {
                            JsonObject responseJson = gson.fromJson(thought.getResponse(), JsonObject.class);
                            String nodeResult = responseJson.get("nodeResult") != null
                                    ? responseJson.get("nodeResult").getAsString()
                                    : "";
                            if (!nodeResult.isEmpty()) {
                                logger.debug("找到 nodeResult: {}", nodeResult);
                                JsonObject nodeResultJson = gson.fromJson(nodeResult, JsonObject.class);
                                String resultText = nodeResultJson.get("result") != null
                                        ? nodeResultJson.get("result").getAsString()
                                        : "";
                                if (!resultText.isEmpty()) {
                                    logger.info("成功从 thoughts 中提取结果，长度: {}", resultText.length());
                                    return resultText;
                                } else {
                                    logger.warn("nodeResult 中的 result 字段为空");
                                }
                            } else {
                                logger.debug("nodeResult 为空");
                            }
                        } catch (Exception e) {
                            logger.warn("解析第 {} 个 thought 失败: {}", i + 1, e.getMessage());
                            // 继续尝试其他 thought
                        }
                    }
                }
            }

            // 兜底：从 output.text 中取
            if (output.getText() != null && !output.getText().isEmpty()) {
                logger.info("从 output.text 中提取结果，长度: {}", output.getText().length());
                return output.getText();
            } else {
                logger.warn("output.text 为空");
            }

            logger.error("无法从 DashScope API 返回结果中提取有效数据");
            throw new RuntimeException("饮食 AI 返回结果为空");
        } catch (Exception e) {
            logger.error("识别饮食记录失败", e);
            throw new RuntimeException("识别饮食记录失败: " + e.getMessage(), e);
        }
    }

    /**
     * 若后续需要支持 MultipartFile 直接识别，可以在这里先上传为 URL，再复用 recognizeDietByUrls。
     * 目前前端流程是先上传拿到 URL，再调用 URL 识别接口，这里暂时保留空实现避免误用。
     */
    public String recognizeDiet(MultipartFile image, String mealType, LocalDate recordDate) {
        throw new UnsupportedOperationException("请先上传图片获取 URL 后，使用 imageList 调用识别接口");
    }
}
