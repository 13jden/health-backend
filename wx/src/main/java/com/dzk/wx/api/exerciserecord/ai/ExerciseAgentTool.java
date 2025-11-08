package com.dzk.wx.api.exerciserecord.ai;

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

import java.util.HashMap;
import java.util.Map;

@Component
public class ExerciseAgentTool {

    @Value("${spring.ai.dashscope.exercise.app-id}")
    String appId;

    @Value("${spring.ai.dashscope.exercise.api-key}")
    String apiKey;

    @Autowired
    private Gson gson;

    /**
     * 调用AI识别运动记录内容，返回JSON字符串
     * @param content 用户输入的内容
     * @param weight 儿童体重（公斤）
     * @return JSON字符串，包含解析后的运动记录数据
     */
    public String recognizeExercise(String content, Integer weight) {
        try {
            // 构建业务参数
            Map<String, String> bizParamMap = new HashMap<>();
            bizParamMap.put("content", content != null ? content : "");
            bizParamMap.put("weight", weight != null ? weight.toString() : "");

            // 转换为JsonObject
            JsonObject bizParams = new JsonObject();
            for (Map.Entry<String, String> entry : bizParamMap.entrySet()) {
                bizParams.addProperty(entry.getKey(), entry.getValue());
            }

            // 构建请求参数
            ApplicationParam param = ApplicationParam.builder()
                    .apiKey(apiKey)
                    .appId(appId)
                    .prompt(content != null ? content : "")
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
            throw new RuntimeException("识别运动记录失败: " + e.getMessage(), e);
        }
    }
}
