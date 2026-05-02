package com.zhn.aiagent.skill;

import org.springframework.stereotype.Component;

@Component
public class ChinaMarketAnalyzeSkill implements BaseSkill {

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
        return """
                你是专业金融分析师。请先通过 MCP 股票工具获取A股三大指数：上证指数、深证成指、创业板指的原始行情数据，
                然后基于工具返回的数据完成以下分析：
                1. 解读当前大盘整体走势、市场情绪
                2. 分析技术面强弱、关键支撑和压力位
                3. 结合量能和形态给出短期、中期趋势预测
                4. 给出普通投资者简单操作参考建议

                用户问题：%s
                """.formatted(userQuery);
    }
}
