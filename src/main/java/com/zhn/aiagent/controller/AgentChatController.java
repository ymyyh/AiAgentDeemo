package com.zhn.aiagent.controller;

import com.zhn.aiagent.agent.AgentDispatcher;
import com.zhn.aiagent.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentChatController {

    private final AgentDispatcher agentDispatcher;

    @GetMapping("/chat")
    public Result<String> chat(@RequestParam String query) {
        String res = agentDispatcher.dispatch(query);
        return Result.success(res);
    }
}