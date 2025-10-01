package com.dzk.wx.api.predictionresult;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionResultService extends ServiceImpl<PredictionResultMapper, PredictionResult> {

    @Autowired
    private PredictionResultMapper predictionResultMapper;

    @Autowired
    private PredictionResultConverter predictionResultConverter;

    public List<PredictionResult> getResultsByChildId(Long childId) {
        return predictionResultMapper.getResultsByChildId(childId);
    }

    public PredictionResult getResultById(Long id) {
        return predictionResultMapper.getResultById(id);
    }

    public PredictionResult saveResult(PredictionResult predictionResult) {
        int result = predictionResultMapper.insert(predictionResult);
        return predictionResultMapper.getResultById(predictionResult.getId());
    }

    public boolean updateResult(PredictionResult predictionResult) {
        return predictionResultMapper.updateById(predictionResult) > 0;
    }

    public boolean deleteResult(Long id) {
        return predictionResultMapper.deleteById(id) > 0;
    }
} 