package com.dzk.wx.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dzk.wx.api.user.User;
import com.dzk.wx.api.user.UserMapper;
import com.dzk.wx.utils.WxLoginTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticeTask {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WxLoginTool wxLoginTool;

    /**
     * 每天早上8点发送早餐饮食打卡提醒
     * cron表达式: 0 0 8 * * ? (每天8点0分0秒执行)
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendBreakfastReminder() {
        System.out.println("开始发送早餐饮食打卡提醒...");
        sendReminderToAllUsers("早餐饮食打卡", "08:00~10:00");
    }

    /**
     * 每天中午12点发送午餐饮食打卡提醒
     * cron表达式: 0 0 12 * * ? (每天12点0分0秒执行)
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void sendLunchReminder() {
        System.out.println("开始发送午餐饮食打卡提醒...");
        sendReminderToAllUsers("午餐饮食打卡", "12:00~14:00");
    }

    /**
     * 每天晚上6点05分发送晚餐饮食打卡提醒
     * cron表达式: 0 10 18 * * ? (每天18点05分0秒执行)
     */
    @Scheduled(cron = "0 22 20 * * ?")
    public void sendDinnerReminder() {
        System.out.println("开始发送晚餐饮食打卡提醒...");
        sendReminderToAllUsers("晚餐饮食打卡", "18:00~20:00");
    }

    /**
     * 发送提醒消息给所有用户
     *
     * @param activityName 活动名称
     * @param timeRange 时间段
     */
    private void sendReminderToAllUsers(String activityName, String timeRange) {
        try {
            // 查询所有用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNotNull("open_id"); // 只查询有openId的用户
            List<User> users = userMapper.selectList(queryWrapper);

            if (users.isEmpty()) {
                System.out.println("没有找到用户，跳过发送提醒");
                return;
            }

            int successCount = 0;
            int failCount = 0;

            for (User user : users) {
                if (user.getOpenId() != null && !user.getOpenId().trim().isEmpty()) {
                    boolean success = wxLoginTool.sendSubscribeMessage(
                            user.getOpenId(), 
                            activityName, 
                            timeRange
                    );
                    
                    if (success) {
                        successCount++;
                        System.out.println("成功发送提醒给用户: " + user.getUsername() + " (openId: " + user.getOpenId() + ")");
                    } else {
                        failCount++;
                        System.out.println("发送提醒失败给用户: " + user.getUsername() + " (openId: " + user.getOpenId() + ")");
                    }
                    
                    // 添加短暂延迟，避免请求过于频繁
                    try {
                        Thread.sleep(100); // 100毫秒延迟
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    System.out.println("用户 " + user.getUsername() + " 没有有效的openId，跳过发送");
                }
            }

            System.out.println(activityName + " 提醒发送完成 - 成功: " + successCount + ", 失败: " + failCount);
        } catch (Exception e) {
            System.err.println("发送 " + activityName + " 提醒时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
