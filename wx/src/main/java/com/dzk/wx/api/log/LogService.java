package com.dzk.wx.api.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService extends ServiceImpl<LogMapper, Log> {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private LogConverter logConverter;

    public List<Log> getLogsByUserId(Long userId) {
        return logMapper.getLogsByUserId(userId);
    }

    public Log getLogById(Long id) {
        return logMapper.getLogById(id);
    }

    public Log saveLog(Log log) {
        int result = logMapper.insert(log);
        return logMapper.getLogById(log.getId());
    }

    public boolean updateLog(Log log) {
        return logMapper.updateById(log) > 0;
    }

    public boolean deleteLog(Long id) {
        return logMapper.deleteById(id) > 0;
    }
} 