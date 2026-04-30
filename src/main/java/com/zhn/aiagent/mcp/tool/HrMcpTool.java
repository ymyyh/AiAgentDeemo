package com.zhn.aiagent.mcp.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class HrMcpTool {

    /**
     * 标准SpringAI MCP工具：批量查询HR岗位信息
     */
    @Tool(description = "根据岗位ID列表批量查询HR岗位基础信息、角色信息")
    public String queryHrPost(
            @ToolParam(description = "HR岗位ID列表") List<String> postIdList
    ) {
        // 可替换为 ES / 第三方接口 / DB 查询
        return "【HR岗位查询成功】已查询岗位ID：" + postIdList;
    }
}
