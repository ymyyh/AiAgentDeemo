package com.ai.skill;

import com.zhn.aiagent.mcp.tool.FeishuMcpTool;
import com.zhn.aiagent.skill.BaseSkill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeishuMsgSkill implements BaseSkill {

    private final FeishuMcpTool feishuMcpTool;

    @Override
    public String getSkillName() {
        return "飞书消息推送技能";
    }

    @Override
    public String getDescription() {
        return "用户需要发送飞书消息、工作提醒、推送通知时使用";
    }

    @Override
    public String execute(String userQuery) {
        return feishuMcpTool.sendFeishuMsg("ou_123456", userQuery);
    }
}
