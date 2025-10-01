package com.dzk.wx.api.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log")
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private LogConverter logConverter;

} 