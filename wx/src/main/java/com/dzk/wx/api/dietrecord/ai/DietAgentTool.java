package com.dzk.wx.api.dietrecord.ai;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationOutput;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DietAgentTool {

    @Value("${spring.ai.dashscope.diet.app-id}")
    String appId;

    @Value("${spring.ai.dashscope.diet.api-key}")
    String apiKey;

    @Autowired
    private Gson gson;

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
            String imageBase64 = downloadImageAsBase64(firstImageUrl);

            // 构建业务参数
            Map<String, String> bizParamMap = new HashMap<>();
            bizParamMap.put("image", imageBase64);
            bizParamMap.put("mealType", mealType != null ? mealType : "");
            if (recordDate != null) {
                bizParamMap.put("recordDate", recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }

            // 转换为JsonObject
            JsonObject bizParams = new JsonObject();
            for (Map.Entry<String, String> entry : bizParamMap.entrySet()) {
                bizParams.addProperty(entry.getKey(), entry.getValue());
            }

            // 构建提示词
            String prompt = "请分析这张饮食图片，识别食物名称、分类、分量、营养成分等信息。";
            if (mealType != null && !mealType.isEmpty()) {
                prompt += "餐次类型：" + mealType + "。";
            }
            if (recordDate != null) {
                prompt += "用餐日期：" + recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "。";
            }

            // 构建请求参数
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
                resultFlowable = application.streamCall(param);
            } catch (NoApiKeyException | InputRequiredException e) {
                throw new RuntimeException("AI调用异常: " + e.getMessage(), e);
            }

            // 收集所有结果，等待完成
            ApplicationResult finalResult = resultFlowable.blockingLast();
            
            // 从结果中提取JSON
            if (finalResult != null && finalResult.getOutput() != null) {
                ApplicationOutput output = finalResult.getOutput();
                
                // 从thoughts中提取响应
                if (output.getThoughts() != null) {
                    for (ApplicationOutput.Thought thought : output.getThoughts()) {
                        if (thought.getResponse() != null) {
                            try {
                                JsonObject responseJson = gson.fromJson(thought.getResponse(), JsonObject.class);
                                String nodeResult = responseJson.get("nodeResult") != null ? 
                                    responseJson.get("nodeResult").getAsString() : "";
                                if (!nodeResult.isEmpty()) {
                                    // 解析nodeResult中的result字段
                                    JsonObject nodeResultJson = gson.fromJson(nodeResult, JsonObject.class);
                                    String resultText = nodeResultJson.get("result") != null ? 
                                        nodeResultJson.get("result").getAsString() : "";
                                    if (!resultText.isEmpty()) {
                                        return resultText;
                                    }
                                }
                            } catch (Exception e) {
                                // 继续尝试其他thought
                            }
                        }
                    }
                }
                
                // 如果thoughts中没有找到，尝试从output的text中获取
                if (output.getText() != null && !output.getText().isEmpty()) {
                    return output.getText();
                }
            }
            
            throw new RuntimeException("AI返回结果为空");
        } catch (Exception e) {
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
            // 将图片转换为 base64
            String imageBase64 = null;
            if (image != null && !image.isEmpty()) {
                byte[] imageBytes = image.getBytes();
                imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            }

            // 构建业务参数
            Map<String, String> bizParamMap = new HashMap<>();
            if (imageBase64 != null) {
                bizParamMap.put("image", imageBase64);
            }
            bizParamMap.put("mealType", mealType != null ? mealType : "");
            if (recordDate != null) {
                bizParamMap.put("recordDate", recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }

            // 转换为JsonObject
            JsonObject bizParams = new JsonObject();
            for (Map.Entry<String, String> entry : bizParamMap.entrySet()) {
                bizParams.addProperty(entry.getKey(), entry.getValue());
            }

            // 构建提示词
            String prompt = "请分析这张饮食图片，识别食物名称、分类、分量、营养成分等信息。";
            if (mealType != null && !mealType.isEmpty()) {
                prompt += "餐次类型：" + mealType + "。";
            }
            if (recordDate != null) {
                prompt += "用餐日期：" + recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "。";
            }

            // 构建请求参数
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
                resultFlowable = application.streamCall(param);
            } catch (NoApiKeyException | InputRequiredException e) {
                throw new RuntimeException("AI调用异常: " + e.getMessage(), e);
            }

            // 收集所有结果，等待完成
            ApplicationResult finalResult = resultFlowable.blockingLast();
            
            // 从结果中提取JSON
            if (finalResult != null && finalResult.getOutput() != null) {
                ApplicationOutput output = finalResult.getOutput();
                
                // 从thoughts中提取响应
                if (output.getThoughts() != null) {
                    for (ApplicationOutput.Thought thought : output.getThoughts()) {
                        if (thought.getResponse() != null) {
                            try {
                                JsonObject responseJson = gson.fromJson(thought.getResponse(), JsonObject.class);
                                String nodeResult = responseJson.get("nodeResult") != null ? 
                                    responseJson.get("nodeResult").getAsString() : "";
                                if (!nodeResult.isEmpty()) {
                                    // 解析nodeResult中的result字段
                                    JsonObject nodeResultJson = gson.fromJson(nodeResult, JsonObject.class);
                                    String resultText = nodeResultJson.get("result") != null ? 
                                        nodeResultJson.get("result").getAsString() : "";
                                    if (!resultText.isEmpty()) {
                                        return resultText;
                                    }
                                }
                            } catch (Exception e) {
                                // 继续尝试其他thought
                            }
                        }
                    }
                }
                
                // 如果thoughts中没有找到，尝试从output的text中获取
                if (output.getText() != null && !output.getText().isEmpty()) {
                    return output.getText();
                }
            }
            
            throw new RuntimeException("AI返回结果为空");
        } catch (Exception e) {
            throw new RuntimeException("识别饮食记录失败: " + e.getMessage(), e);
        }
    }
}
