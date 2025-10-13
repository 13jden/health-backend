package com.dzk.wx.api.Agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/stream/{type}")
    public Flux<String> stream(@PathVariable String type, @RequestBody ChatRequest request) {
        return chatService.streamChat(type, request);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
