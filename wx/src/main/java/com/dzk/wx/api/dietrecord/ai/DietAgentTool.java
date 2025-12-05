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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    /**
     * 通过 DashScope Agent（应用）识别饮食记录图片（URL 列表），返回 JSON 字符串
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
            // 业务参数：只保留 imageList，名称固定为 imageList，支持多张图片
            JsonObject bizParams = new JsonObject();
            JsonArray imageArray = new JsonArray();
            for (String url : imageList) {
                imageArray.add(url);
            }
            bizParams.add("imageList", imageArray);

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
                resultFlowable = application.streamCall(param);
            } catch (NoApiKeyException | InputRequiredException e) {
                throw new RuntimeException("饮食 AI 调用异常: " + e.getMessage(), e);
            }

            // 收集最终结果
            ApplicationResult finalResult = resultFlowable.blockingLast();
            if (finalResult != null && finalResult.getOutput() != null) {
                ApplicationOutput output = finalResult.getOutput();

                // 优先从 thoughts.nodeResult.result 中取
                if (output.getThoughts() != null) {
                    for (ApplicationOutput.Thought thought : output.getThoughts()) {
                        if (thought.getResponse() != null) {
                            try {
                                JsonObject responseJson = gson.fromJson(thought.getResponse(), JsonObject.class);
                                String nodeResult = responseJson.get("nodeResult") != null
                                        ? responseJson.get("nodeResult").getAsString()
                                        : "";
                                if (!nodeResult.isEmpty()) {
                                    JsonObject nodeResultJson = gson.fromJson(nodeResult, JsonObject.class);
                                    String resultText = nodeResultJson.get("result") != null
                                            ? nodeResultJson.get("result").getAsString()
                                            : "";
                                    if (!resultText.isEmpty()) {
                                        return resultText;
                                    }
                                }
                            } catch (Exception e) {
                                // 继续尝试其他 thought
                            }
                        }
                    }
                }

                // 兜底：从 output.text 中取
                if (output.getText() != null && !output.getText().isEmpty()) {
                    return output.getText();
                }
            }

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
