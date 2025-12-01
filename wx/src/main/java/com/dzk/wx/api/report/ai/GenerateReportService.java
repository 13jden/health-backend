package com.dzk.wx.api.report.ai;

import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationOutput;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dzk.wx.api.child.Child;
import com.dzk.wx.api.child.ChildConverter;
import com.dzk.wx.api.child.ChildDto;
import com.dzk.wx.api.child.ChildMapper;
import com.dzk.wx.api.dietrecord.DietRecord;
import com.dzk.wx.api.dietrecord.DietRecordConverter;
import com.dzk.wx.api.dietrecord.DietRecordDto;
import com.dzk.wx.api.dietrecord.DietRecordMapper;
import com.dzk.wx.api.exerciserecord.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class GenerateReportService {

    @Value("${id.report.app-id:${spring.ai.dashscope.agent.app-id}}")
    private String appId;

    @Value("${id.report.api-key:${spring.ai.dashscope.api-key}}")
    private String apiKey;

    @Autowired
    private Gson gson;

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private DietRecordMapper dietRecordMapper;

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @Autowired
    private ChildConverter childConverter;

    @Autowired
    private ExerciseRecordConverter exerciseRecordConverter;

    @Autowired
    private DietRecordConverter dietRecordConverter;

    /**
     * 生成儿童健康报告
     *
     * @param bizParams 附带的业务参数，最终会作为 bizParams 发送给应用
     * @return 模型返回的报告文本
     */
    public String generateReport(Map<String, ?> bizParams) {
        try {
            JsonObject bizParamJson = buildBizParams(bizParams);

            ApplicationParam param = ApplicationParam.builder()
                    .apiKey(apiKey)
                    .appId(appId)
                    .prompt("根据用户数据和规定格式生成报告")
                    .bizParams(bizParamJson)
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

            ApplicationResult finalResult = resultFlowable.blockingLast();
            String report = extractResult(finalResult);
            if (report != null && !report.isEmpty()) {
                return report;
            }
            throw new RuntimeException("AI返回结果为空");
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("生成报告失败: " + e.getMessage(), e);
        }
    }

    /**
     * 准备报告
     */
    public String prepareReport(Long childId) {
        if (childId == null) {
            return null;
        }
        Child child = childMapper.getChildById(childId);
        ChildDto childDto = childConverter.toDto(child);

        LocalDate fifteenDaysAgo = LocalDate.now().minusDays(15);
        LambdaQueryWrapper<DietRecord> dietRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dietRecordLambdaQueryWrapper
                .eq(DietRecord::getChildId, childId)
                .ge(DietRecord::getRecordTime, fifteenDaysAgo)
                .orderByDesc(DietRecord::getRecordTime);
        List<DietRecordDto> dietRecordDtoList = dietRecordConverter.toDtoList(dietRecordMapper.selectList(dietRecordLambdaQueryWrapper)) ;
        LambdaQueryWrapper<ExerciseRecord> exerciseRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        exerciseRecordLambdaQueryWrapper
                .eq(ExerciseRecord::getChildId,childId)
                .ge(ExerciseRecord::getStartTime,fifteenDaysAgo)
                .orderByDesc(ExerciseRecord::getStartTime);
        List<ExerciseRecordDto> exerciseRecordDtoList = exerciseRecordConverter.toDtoList(exerciseRecordMapper.selectList(exerciseRecordLambdaQueryWrapper));
        Map<String, Object> params = new HashMap<>();
        params.put("child", JSONUtil.toJsonStr( child ) );
        params.put("dietRecords", JSONUtil.toJsonStr(dietRecordDtoList));
        params.put("exerciseRecords", JSONUtil.toJsonStr(exerciseRecordDtoList));
        return generateReport(params);
    }

    private JsonObject buildBizParams(Map<String, ?> rawParams) {
        JsonObject bizParams = new JsonObject();
        if (rawParams == null || rawParams.isEmpty()) {
            return bizParams;
        }
        rawParams.forEach((key, value) -> {
            if (key == null) {
                return;
            }
            if (value == null) {
                bizParams.addProperty(key, "");
            } else if (value instanceof Number) {
                bizParams.addProperty(key, (Number) value);
            } else if (value instanceof Boolean) {
                bizParams.addProperty(key, (Boolean) value);
            } else if (value instanceof Character) {
                bizParams.addProperty(key, value.toString());
            } else if (value instanceof String) {
                bizParams.addProperty(key, (String) value);
            } else if (value instanceof JsonElement) {
                bizParams.add(key, (JsonElement) value);
            } else {
                bizParams.add(key, gson.toJsonTree(value));
            }
        });
        return bizParams;
    }

    private String extractResult(ApplicationResult finalResult) {
        if (finalResult == null || finalResult.getOutput() == null) {
            return null;
        }
        ApplicationOutput output = finalResult.getOutput();
        if (output.getThoughts() != null) {
            for (ApplicationOutput.Thought thought : output.getThoughts()) {
                String resultText = extractResultFromThought(thought);
                if (resultText != null && !resultText.isEmpty()) {
                    return resultText;
                }
            }
        }
        if (output.getText() != null && !output.getText().isEmpty()) {
            return output.getText();
        }
        return null;
    }

    private String extractResultFromThought(ApplicationOutput.Thought thought) {
        if (thought == null || thought.getResponse() == null) {
            return null;
        }
        try {
            JsonObject responseJson = gson.fromJson(thought.getResponse(), JsonObject.class);
            if (responseJson == null) {
                return null;
            }
            JsonElement nodeResultElement = responseJson.get("nodeResult");
            if (nodeResultElement == null || nodeResultElement.isJsonNull()) {
                return null;
            }
            JsonObject nodeResultJson;
            if (nodeResultElement.isJsonObject()) {
                nodeResultJson = nodeResultElement.getAsJsonObject();
            } else {
                nodeResultJson = gson.fromJson(nodeResultElement.getAsString(), JsonObject.class);
            }
            if (nodeResultJson != null && nodeResultJson.has("result")) {
                JsonElement resultElement = nodeResultJson.get("result");
                if (resultElement != null && !resultElement.isJsonNull()) {
                    return resultElement.getAsString();
                }
            }
        } catch (Exception ignored) {
            // 忽略单条解析错误
        }
        return null;
    }


}
