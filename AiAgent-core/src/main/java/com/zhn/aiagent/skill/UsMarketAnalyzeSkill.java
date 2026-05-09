package com.zhn.aiagent.skill;

import org.springframework.stereotype.Component;

@Component
public class UsMarketAnalyzeSkill implements BaseSkill {

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
        return """
                你是专业海外市场分析师。请先通过 MCP 股票工具获取美股三大指数：道琼斯、纳斯达克、标普500 的原始行情数据。

                【原始串格式】工具返回多为 v_usXXX="..."，段内以 ~ 分隔；前几段为代码与名称，其后为最新点位、量能与时间等。美股字段排列可能与 A 股略有不同，无法确定的字段请标明“按盘面常识推断”，勿虚构字段定义。

                然后基于工具返回的数据完成以下分析：
                1. 分析美股当前整体趋势、强弱结构
                2. 解读关键点位、技术形态、资金情绪
                3. 预判短期和中期走势方向
                4. 提示市场风险与配置参考

                用户问题：%s
                """.formatted(userQuery);
    }
}
