package com.dzk.wx.api.predictionresult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prediction-result")
public class PredictionResultController {

    @Autowired
    private PredictionResultService predictionResultService;

    @Autowired
    private PredictionResultConverter predictionResultConverter;

} 