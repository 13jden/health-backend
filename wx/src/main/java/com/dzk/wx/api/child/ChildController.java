package com.dzk.wx.api.child;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("child")
public class ChildController {

    @Autowired
    private ChildService childService;

    @Autowired
    private ChildConverter childConverter;

} 