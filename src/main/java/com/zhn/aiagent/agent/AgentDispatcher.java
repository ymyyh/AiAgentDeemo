package com.zhn.aiagent.agent;

import com.zhn.aiagent.skill.BaseSkill;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentDispatcher {

    private final SkillRegistry skillRegistry;
    private final ChatClient chatClient;

    // 大模型全自动意图匹配 + 技能调度（无任何硬编码关键词）
    public String dispatch(String userQuery) {
        // 1. 获取所有已注册的业务技能信息
        List<BaseSkill> skillList = skillRegistry.getSkillList();
        StringBuilder skillDescPrompt = new StringBuilder();
        for (BaseSkill skill : skillList) {
            skillDescPrompt.append("技能名称：").append(skill.getSkillName())
                    .append("，技能能力描述：").append(skill.getDescription())
                    .append("\n");
        }

        // 2. 构造AI智能匹配Prompt，指令大模型选择对应技能
        String promptString = """
                你是AI Agent调度器，负责根据用户提问，精准匹配唯一合适的业务技能。
                规则：
                1. 仅返回【技能名称】，不要返回多余解释、标点、多余文字
                2. 若无匹配技能，直接返回：NONE
                3. 严格根据技能描述匹配用户需求，优先精准匹配
                
                可用技能列表：
                %s
                
                用户提问：%s
                """.formatted(skillDescPrompt, userQuery);

        Prompt prompt = new Prompt(promptString);
        // 3. 调用大模型自动识别意图、匹配技能
        String matchSkillName = chatClient.prompt(prompt).call().content().trim();

        // 4. 无匹配技能
        if ("NONE".equals(matchSkillName)) {
            return "暂时无法理解您的需求，暂无对应处理能力";
        }

        // 5. 执行匹配到的目标技能
        for (BaseSkill skill : skillList) {
            if (skill.getSkillName().equals(matchSkillName)) {
                return skill.execute(userQuery);
            }
        }

        return "技能匹配失败，请重试";
    }
}
