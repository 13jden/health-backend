package com.dzk.wx.api.predictionresult;

import org.springframework.stereotype.Component;

@Component
public class PredictionResultConverter {
    
    public PredictionResultDto toDto(PredictionResult predictionResult) {
        if (predictionResult == null) {
            return null;
        }
        
        PredictionResultDto dto = new PredictionResultDto();
        dto.setId(predictionResult.getId());
        dto.setChildId(predictionResult.getChildId());
        dto.setRiskLevel(predictionResult.getRiskLevel());
        dto.setPredictionDate(predictionResult.getPredictionDate());
        dto.setModelVersion(predictionResult.getModelVersion());
        return dto;
    }
    
    public PredictionResult toEntity(PredictionResultDto.Input input) {
        if (input == null) {
            return null;
        }
        
        PredictionResult predictionResult = new PredictionResult();
        predictionResult.setChildId(input.getChildId());
        predictionResult.setRiskLevel(input.getRiskLevel());
        predictionResult.setPredictionDate(input.getPredictionDate());
        predictionResult.setModelVersion(input.getModelVersion());
        return predictionResult;
    }

    public PredictionResultDto.Detail toDetail(PredictionResult predictionResult) {
        if (predictionResult == null) {
            return null;
        }
        
        PredictionResultDto.Detail detail = new PredictionResultDto.Detail();
        detail.setId(predictionResult.getId());
        detail.setChildId(predictionResult.getChildId());
        detail.setRiskLevel(predictionResult.getRiskLevel());
        detail.setPredictionDate(predictionResult.getPredictionDate());
        detail.setModelVersion(predictionResult.getModelVersion());
        detail.setCreateTime(predictionResult.getCreateTime() != null ? predictionResult.getCreateTime().toString() : null);
        detail.setUpdateTime(predictionResult.getUpdateTime() != null ? predictionResult.getUpdateTime().toString() : null);
        return detail;
    }
} 