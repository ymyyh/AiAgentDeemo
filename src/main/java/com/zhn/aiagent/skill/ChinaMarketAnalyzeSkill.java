package com.zhn.aiagent.skill;

import com.zhn.aiagent.mcp.tool.FreeStockMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChinaMarketAnalyzeSkill implements BaseSkill {

    private final FreeStockMcpTool freeStockMcpTool;

    @Override
    public String getSkillName() {
        return "查询近期A股走势并进行技术分析和趋势预测";
    }

    @Override
    public String getDescription() {
        return "获取上证指数、深证成指、创业板指实时及近期行情，进行技术面形态、量价、支撑压力分析，并给出短期中期趋势预测";
    }

    @Override
    public String execute(String userQuery) {
        String marketData = freeStockMcpTool.getChinaMarketIndex();
        String prompt = """
                以下是A股三大指数原始行情数据，请你作为专业金融分析师：
                1. 解读当前大盘整体走势、市场情绪
                2. 分析技术面强弱、关键支撑和压力位
                3. 结合量能和形态给出短期、中期趋势预测
                4. 给出普通投资者简单操作参考建议
                行情数据：
                %s
                """.formatted(marketData);
        return prompt;
    }
}