package com.zhn.aiagent.skill;

import com.zhn.aiagent.mcp.tool.HrMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HrPostSkill implements BaseSkill {

    private final HrMcpTool hrMcpTool;

    @Override
    public String getSkillName() {
        return "HR岗位信息查询技能";
    }

    @Override
    public String getDescription() {
        return "用户询问HR岗位、角色、岗位配置信息时使用";
    }

    @Override
    public String execute(String userQuery) {
        // 后续我可以帮你升级：大模型自动提取参数，此处彻底零硬编码
        List<String> demoIds = List.of("N01710HR_Analyst","N01710HRVP");
        return hrMcpTool.queryHrPost(demoIds);
    }
}
