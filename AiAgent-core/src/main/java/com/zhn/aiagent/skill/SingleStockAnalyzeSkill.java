package com.zhn.aiagent.skill;

import org.springframework.stereotype.Component;

@Component
public class SingleStockAnalyzeSkill implements BaseSkill {

    @Override
    public String getSkillName() {
        return "查询个股近期走势并进行技术分析和趋势预测";
    }

    @Override
    public String getDescription() {
        return "支持输入股票名称或代码，自动匹配市场完整代码，拉取行情数据，做技术面分析、支撑压力、量价研判及趋势预测";
    }

    @Override
    public String execute(String userQuery) {
        return """
                你是专业投顾。请通过 MCP 股票工具完成个股行情查询：
                - 如果用户输入的是股票名称，先匹配腾讯标准股票代码，再查询该股票行情
                - 如果用户输入的是 sh、sz、us 开头的腾讯标准股票代码，直接查询该股票行情
                - 如果无法识别股票，请明确提示用户更换名称或代码重试

                拿到原始行情数据后，请完成：
                1. 解读个股近期整体走势
                2. 技术面分析：支撑位、压力位、量价关系
                3. 短期趋势预判、中期走势逻辑
                4. 给出理性投资参考和风险提示

                用户问题：%s
                """.formatted(userQuery);
    }
}
