package com.zhn.aiagent.mcp.tool;

import com.zhn.aiagent.AiAgentMcpApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 直接调用 {@link FreeStockMcpTool#getStockCodeByName(String)}，测量 smartbox 接口耗时。
 * <p>
 * 运行：{@code mvn -pl AiAgent-mcp test -Dtest=FreeStockMcpToolGetStockCodeByNameTimingTest}
 */
@SpringBootTest(classes = AiAgentMcpApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class McpToolTest {

    @Autowired
    private FreeStockMcpTool freeStockMcpTool;

    @Test
    void getStockCodeByName_reportsElapsedMillis_singleQuery() {
        String keyword = "贵州茅台";
        long t0 = System.nanoTime();
        String result = freeStockMcpTool.getStockCodeByName(keyword);
        long elapsedMs = (System.nanoTime() - t0) / 1_000_000L;

        System.out.printf("%n[getStockCodeByName] keyword=%s 耗时=%d ms 结果=%s%n%n", keyword, elapsedMs, result);
        assertThat(result).isNotNull();
    }

    @Test
    void getChinaMarketIndexTest() {
        String result = freeStockMcpTool.getChinaMarketIndex();
        System.out.println(result);
    }

    @Test
    void getStockDetailByCodeTest() {
        String result = freeStockMcpTool.getStockDetailByCode("sh600519");
        System.out.println(result);
    }

    @Test
    void getStockCodeByNameTest() {
        String keyword = "贵州茅台";
        freeStockMcpTool.getStockCodeByName(keyword);
    }
}
