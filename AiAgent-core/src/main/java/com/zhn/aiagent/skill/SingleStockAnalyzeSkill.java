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
        return "支持输入股票名称或代码，先通过MCP匹配腾讯标准股票代码，再通过MCP拉取行情数据，做技术面分析、支撑压力、量价研判及趋势预测";
    }

    @Override
    public String execute(String userQuery) {
        return """
                你是专业投顾。请严格按下面顺序通过 MCP 股票工具完成个股行情查询，不要跳过任何一步：

                第一步：调用 MCP 工具 getStockCodeByName，根据用户输入查询并确认腾讯标准股票代码。
                - 用户输入可能是股票名称，也可能是股票代码，都必须先执行这一步。
                - 如果返回“未匹配到该股票”或“名称匹配异常”，请停止后续调用，并明确提示用户更换名称或代码重试。

                第二步：只使用第一步返回的腾讯标准股票代码，调用 MCP 工具 getStockDetailByCode 查询该股票行情。
                - 不要直接使用用户原始输入查询行情。
                - 如果第二步行情查询失败，请提示用户稍后重试或更换代码。

                【腾讯 qt.gtimg.cn 原始串格式说明】工具返回多为单行脚本变量，例如：
                v_sh600519="1~贵州茅台~600519~1372.99~1371.05~1371.66~33369~…"
                引号内以 ~ 从左切分为若干字段（字段含义随品类略有差异）：
                - 通常从左数：字段 1 为内部序号；字段 2 为证券简称；字段 3 为六位代码；
                - 字段 4 起多为价格与量能（常见顺序含现价、昨收、今开、成交量/额、买卖档位量与价等）；
                - 中部可出现 yyyyMMddHHmmss 形式时间；末尾可见币种（如 CNY）、类型标记（如 GP-A 表示 A 股）等。
                解读时请用相邻数值互相校验；无法确认的字段请写明“含义不确定，按数值推断”，勿编造字段名。

                拿到原始行情数据后，请完成：
                1. 解读个股近期整体走势
                2. 技术面分析：支撑位、压力位、量价关系
                3. 短期趋势预判、中期走势逻辑
                4. 给出理性投资参考和风险提示

                用户问题：%s
                """.formatted(userQuery);
    }
}
