package com.zhn.aiagent.config;

import com.zhn.aiagent.mcp.tool.FreeStockMcpTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolConfig {

    @Bean
    public ToolCallbackProvider stockToolCallbackProvider(FreeStockMcpTool freeStockMcpTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(freeStockMcpTool)
                .build();
    }
}
