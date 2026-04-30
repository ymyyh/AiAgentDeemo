package com.zhn.aiagent.mcp.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class FeishuMcpTool {

    @Tool(description = "向指定飞书用户推送工作消息、通知提醒")
    public String sendFeishuMsg(
            @ToolParam(description = "飞书用户ID") String receiveId,
            @ToolParam(description = "需要发送的消息内容") String content
    ) {
        return "【飞书消息发送成功】接收人:" + receiveId + " 内容:" + content;
    }
}
