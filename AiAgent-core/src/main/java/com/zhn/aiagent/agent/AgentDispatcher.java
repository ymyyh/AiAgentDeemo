package com.zhn.aiagent.agent;

import com.zhn.aiagent.skill.BaseSkill;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentDispatcher {

    private final SkillRegistry skillRegistry;
    private final ChatClient chatClient;
    @Qualifier("toolCallbacks")
    private final ToolCallbackProvider mcpToolCallbackProvider;

    public String dispatch(String userQuery) {
        List<BaseSkill> skillList = skillRegistry.getSkillList();
        StringBuilder sb = new StringBuilder();
        for (BaseSkill skill : skillList) {
            sb.append("技能名称：").append(skill.getSkillName())
                    .append("，功能描述：").append(skill.getDescription()).append("\n");
        }

        String routePrompt = """
                你是股票AI调度器，只输出匹配到的【技能名称】，无匹配输出NONE。
                可用技能：
                %s
                用户问题：%s
                """.formatted(sb, userQuery);

        String skillName = chatClient.prompt(routePrompt).call().content().trim();
        if ("NONE".equalsIgnoreCase(skillName)) {
            return "暂时无法解析该需求，请换种提问方式。";
        }
        for (BaseSkill skill : skillList) {
            if (skill.getSkillName().equals(skillName)) {
                return chatClient.prompt(skill.execute(userQuery))
                        .tools(mcpToolCallbackProvider.getToolCallbacks())
                        .call()
                        .content();
            }
        }
        return "技能匹配失败，请重试。";
    }
}
