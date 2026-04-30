package com.zhn.aiagent.skill;

import com.zhn.aiagent.mcp.tool.FreeStockMcpTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SingleStockAnalyzeSkill implements BaseSkill {

    private final FreeStockMcpTool freeStockMcpTool;

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
        String fullCode;
        // 判断是否已经是腾讯标准代码
        if (userQuery.startsWith("sh") || userQuery.startsWith("sz") || userQuery.startsWith("us")) {
            fullCode = userQuery;
        } else {
            // 名称转代码
            fullCode = freeStockMcpTool.getStockCodeByName(userQuery);
        }
        if ("未匹配到该股票".equals(fullCode) || "名称匹配异常".equals(fullCode)) {
            return "无法识别该股票，请更换名称或代码重试";
        }
        String stockData = freeStockMcpTool.getStockDetailByCode(fullCode);

        String prompt = """
                股票标识：%s
                原始行情数据：
                %s
                请你作为专业投顾完成：
                1. 解读个股近期整体走势
                2. 技术面分析：支撑位、压力位、量价关系
                3. 短期趋势预判、中期走势逻辑
                4. 给出理性投资参考和风险提示
                """.formatted(userQuery, stockData);
        return prompt;
    }
}