package com.dzk.wx.api.dietrecord.ai;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Component
public class DietAgentTool {

    private static final Logger logger = LoggerFactory.getLogger(DietAgentTool.class);
    
    private static final String API_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";
    private static final String MODEL_NAME = "qwen-vl-plus";

    @Value("${spring.ai.dashscope.diet.api-key}")
    String apiKey;

    @Autowired
    private Gson gson;
    
    @Autowired(required = false)
    private OkHttpClient httpClient;
    
    /**
     * 获取或创建 OkHttpClient
     */
    private OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .build();
        }
        return httpClient;
    }

    /**
     * 从URL下载图片并转换为base64
     * @param imageUrl 图片URL
     * @return base64编码的图片字符串
     */
    private String downloadImageAsBase64(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                byte[] imageBytes = inputStream.readAllBytes();
                return Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("下载图片失败: " + imageUrl + ", 错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检测图片类型（通过文件头）
     * @param imageBytes 图片字节数组
     * @return MIME类型，默认为 image/jpeg
     */
    private String detectImageType(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length < 4) {
            return "image/jpeg";
        }
        
        // 检查文件头
        // PNG: 89 50 4E 47
        if (imageBytes[0] == (byte)0x89 && imageBytes[1] == 0x50 && 
            imageBytes[2] == 0x4E && imageBytes[3] == 0x47) {
            return "image/png";
        }
        // JPEG: FF D8 FF
        if (imageBytes[0] == (byte)0xFF && imageBytes[1] == (byte)0xD8 && 
            imageBytes[2] == (byte)0xFF) {
            return "image/jpeg";
        }
        // GIF: 47 49 46 38
        if (imageBytes[0] == 0x47 && imageBytes[1] == 0x49 && 
            imageBytes[2] == 0x46 && imageBytes[3] == 0x38) {
            return "image/gif";
        }
        // WebP: 检查 RIFF...WEBP
        if (imageBytes.length >= 12 && 
            imageBytes[0] == 0x52 && imageBytes[1] == 0x49 && 
            imageBytes[2] == 0x46 && imageBytes[3] == 0x46) {
            return "image/webp";
        }
        
        // 默认返回 JPEG
        return "image/jpeg";
    }
    
    /**
     * 调用 OpenAI 兼容的 DashScope API 识别图片
     * @param imageBase64 base64编码的图片
     * @param prompt 提示词
     * @param imageMimeType 图片MIME类型（可选，如果为null则使用默认值）
     * @return AI返回的JSON字符串
     */
    private String callVisionAPI(String imageBase64, String prompt, String imageMimeType) {
        if (imageMimeType == null || imageMimeType.isEmpty()) {
            imageMimeType = "image/jpeg";
        }
        try {
            // 构建请求体
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", MODEL_NAME);
            
            // 构建消息数组
            JsonArray messages = new JsonArray();
            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            
            // 构建内容数组（包含图片和文本）
            JsonArray content = new JsonArray();
            
            // 添加图片
            JsonObject imageContent = new JsonObject();
            imageContent.addProperty("type", "image_url");
            JsonObject imageUrl = new JsonObject();
            imageUrl.addProperty("url", "data:" + imageMimeType + ";base64," + imageBase64);
            imageContent.add("image_url", imageUrl);
            content.add(imageContent);
            
            // 添加文本提示
            JsonObject textContent = new JsonObject();
            textContent.addProperty("type", "text");
            textContent.addProperty("text", prompt);
            content.add(textContent);
            
            message.add("content", content);
            messages.add(message);
            
            requestBody.add("messages", messages);
            
            String requestBodyJson = gson.toJson(requestBody);
            logger.debug("API请求体: {}", requestBodyJson);
            
            // 构建HTTP请求
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(requestBodyJson, mediaType);
            
            Request request = new Request.Builder()
                    .url(API_BASE_URL + "/chat/completions")
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            
            // 发送请求
            try (Response response = getHttpClient().newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "无错误详情";
                    logger.error("API请求失败: {} - {}", response.code(), errorBody);
                    throw new RuntimeException("AI调用失败: HTTP " + response.code() + " - " + errorBody);
                }
                
                String responseBody = response.body().string();
                logger.debug("API响应: {}", responseBody);
                
                // 解析响应
                JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
                
                // 提取choices中的content
                if (responseJson.has("choices") && responseJson.get("choices").isJsonArray()) {
                    JsonArray choices = responseJson.getAsJsonArray("choices");
                    if (choices.size() > 0) {
                        JsonObject firstChoice = choices.get(0).getAsJsonObject();
                        if (firstChoice.has("message")) {
                            JsonObject messageObj = firstChoice.getAsJsonObject("message");
                            if (messageObj.has("content")) {
                                String contentText = messageObj.get("content").getAsString();
                                logger.debug("提取的内容: {}", contentText);
                                return contentText;
                            }
                        }
                    }
                }
                
                throw new RuntimeException("AI返回结果格式不正确: " + responseBody);
            }
        } catch (IOException e) {
            logger.error("调用AI API失败", e);
            throw new RuntimeException("调用AI API失败: " + e.getMessage(), e);
        }
    }

    /**
     * 调用AI识别饮食记录图片（通过URL列表），返回JSON字符串
     * @param imageList 图片URL列表
     * @param mealType 餐次类型
     * @param recordDate 用餐日期
     * @return JSON字符串，包含解析后的饮食记录数据
     */
    public String recognizeDietByUrls(List<String> imageList, String mealType, LocalDate recordDate) {
        if (imageList == null || imageList.isEmpty()) {
            throw new RuntimeException("图片列表不能为空");
        }

        try {
            // 下载第一张图片（如果有多张，可以后续扩展为合并处理）
            String firstImageUrl = imageList.get(0);
            URL url = new URL(firstImageUrl);
            byte[] imageBytes;
            try (InputStream inputStream = url.openStream()) {
                imageBytes = inputStream.readAllBytes();
            }
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            String imageMimeType = detectImageType(imageBytes);

            // 构建提示词
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("请分析这张饮食图片，识别食物名称、分类、分量、营养成分等信息。");
            promptBuilder.append("请以JSON格式返回结果，包含以下字段：");
            promptBuilder.append("foodName（食物名称）、foodCategory（食物分类）、quantity（分量，数字）、");
            promptBuilder.append("unit（单位，如：克、毫升、个等）、calories（热量，卡路里）、");
            promptBuilder.append("protein（蛋白质，克）、carbohydrate（碳水化合物，克）、");
            promptBuilder.append("fat（脂肪，克）、fiber（膳食纤维，克）、notes（备注）。");
            
            if (mealType != null && !mealType.isEmpty()) {
                promptBuilder.append("餐次类型：").append(mealType).append("。");
            }
            if (recordDate != null) {
                promptBuilder.append("用餐日期：").append(recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("。");
            }
            promptBuilder.append("只输出纯JSON格式，不要包含Markdown代码块标记（```json或```），不要其他文字说明。");

            String prompt = promptBuilder.toString();
            logger.debug("提示词: {}", prompt);

            // 调用API
            String result = callVisionAPI(imageBase64, prompt, imageMimeType);
            
            if (result == null || result.trim().isEmpty()) {
                throw new RuntimeException("AI返回结果为空");
            }
            
            return result;
        } catch (Exception e) {
            logger.error("识别饮食记录失败", e);
            throw new RuntimeException("识别饮食记录失败: " + e.getMessage(), e);
        }
    }

    /**
     * 调用AI识别饮食记录图片，返回JSON字符串
     * @param image 用户上传的图片
     * @param mealType 餐次类型
     * @param recordDate 用餐日期
     * @return JSON字符串，包含解析后的饮食记录数据
     */
    public String recognizeDiet(MultipartFile image, String mealType, LocalDate recordDate) {
        try {
            if (image == null || image.isEmpty()) {
                throw new RuntimeException("图片不能为空");
            }
            
            // 将图片转换为 base64
            byte[] imageBytes = image.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            
            // 检测图片类型
            String imageMimeType = image.getContentType();
            if (imageMimeType == null || imageMimeType.isEmpty() || !imageMimeType.startsWith("image/")) {
                // 如果无法从MultipartFile获取，则通过文件头检测
                imageMimeType = detectImageType(imageBytes);
            }

            // 构建提示词
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("请分析这张饮食图片，识别食物名称、分类、分量、营养成分等信息。");
            promptBuilder.append("请以JSON格式返回结果，包含以下字段：");
            promptBuilder.append("foodName（食物名称）、foodCategory（食物分类）、quantity（分量，数字）、");
            promptBuilder.append("unit（单位，如：克、毫升、个等）、calories（热量，卡路里）、");
            promptBuilder.append("protein（蛋白质，克）、carbohydrate（碳水化合物，克）、");
            promptBuilder.append("fat（脂肪，克）、fiber（膳食纤维，克）、notes（备注）。");
            
            if (mealType != null && !mealType.isEmpty()) {
                promptBuilder.append("餐次类型：").append(mealType).append("。");
            }
            if (recordDate != null) {
                promptBuilder.append("用餐日期：").append(recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("。");
            }
            promptBuilder.append("只输出纯JSON格式，不要包含Markdown代码块标记（```json或```），不要其他文字说明。");

            String prompt = promptBuilder.toString();
            logger.debug("提示词: {}", prompt);

            // 调用API
            String result = callVisionAPI(imageBase64, prompt, imageMimeType);
            
            if (result == null || result.trim().isEmpty()) {
                throw new RuntimeException("AI返回结果为空");
            }
            
            return result;
        } catch (Exception e) {
            logger.error("识别饮食记录失败", e);
            throw new RuntimeException("识别饮食记录失败: " + e.getMessage(), e);
        }
    }
}
