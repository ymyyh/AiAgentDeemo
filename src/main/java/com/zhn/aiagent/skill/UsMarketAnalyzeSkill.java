package com.zhn.aiagent.skill;

import com.zhn.aiagent.mcp.tool.FreeStockMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsMarketAnalyzeSkill implements BaseSkill {

    private final FreeStockMcpTool freeStockMcpTool;

    @Override
    public String getSkillName() {
        return "查询近期美股走势并进行技术分析和趋势预测";
    }

    @Override
    public String getDescription() {
        return "获取道琼斯、纳斯达克、标普500近期行情，分析美股整体趋势、技术形态、市场风险，并做后续走势预测";
    }

    @Override
    public String execute(String userQuery) {
        String marketData = freeStockMcpTool.getUsMarketIndex();
        String prompt = """
                以下是美股三大指数原始行情数据，请你作为专业海外市场分析师：
                1. 分析美股当前整体趋势、强弱结构
                2. 解读关键点位、技术形态、资金情绪
                3. 预判短期和中期走势方向
                4. 提示市场风险与配置参考
                行情数据：
                %s
                """.formatted(marketData);
        return prompt;
    }
}