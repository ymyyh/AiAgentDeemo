package com.zhn.aiagent.mcp.tool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class FreeStockMcpTool {

    /**
     * 例如：{@code v_hint="sh~600519~...~..."}，无匹配时为 {@code v_hint="N"}。
     */
    private static final Pattern V_HINT_PATTERN = Pattern.compile("v_hint\\s*=\\s*\"([^\"]*)\"");

    private final RestTemplate restTemplate;

    @Tool(description = "获取A股三大指数：上证指数、深证成指、创业板指行情数据，用于大盘技术分析和趋势预测")
    public String getChinaMarketIndex() {
        List<String> indexCodes = Arrays.asList("sh000001", "sz399001", "sz399006");
        StringBuilder result = new StringBuilder();
        for (String code : indexCodes) {
            String url = "https://qt.gtimg.cn/q=" + code;
            try {
                String data = restTemplate.getForObject(url, String.class);
                result.append("【").append(code).append("】\n").append(data).append("\n\n");
            } catch (Exception e) {
                result.append("【").append(code).append("】数据获取失败\n");
            }
        }
        return result.toString();
    }

    @Tool(description = "获取美股三大指数：道琼斯、纳斯达克、标普500行情数据，用于美股技术分析和趋势预测")
    public String getUsMarketIndex() {
        List<String> indexCodes = Arrays.asList("us^DJI", "us^IXIC", "us^GSPC");
        StringBuilder result = new StringBuilder();
        for (String code : indexCodes) {
            String url = "https://qt.gtimg.cn/q=" + code;
            try {
                String data = restTemplate.getForObject(url, String.class);
                result.append("【").append(code).append("】\n").append(data).append("\n\n");
            } catch (Exception e) {
                result.append("【").append(code).append("】数据获取失败\n");
            }
        }
        return result.toString();
    }

    @Tool(description = "根据股票中文名称自动匹配市场前缀+完整代码，例如：贵州茅台 -> sh600519")
    public String getStockCodeByName(
            @ToolParam(description = "股票中文名称，如：贵州茅台、特斯拉") String stockName
    ) {
        try {
            // 路径带末尾 /、仅用 UriComponentsBuilder 编码 q，避免双重编码触发服务端 301/HTML
            String url = UriComponentsBuilder.fromHttpUrl("https://smartbox.gtimg.cn/s3/")
                    .queryParam("q", stockName.trim())
                    .queryParam("t", "all")
                    .build()
                    .toUriString();

            String resp = restTemplate.getForObject(url, String.class);
            if (resp == null || resp.isBlank()) {
                return "名称匹配接口无返回";
            }

            String tencentCode = parseTencentCodeFromVHint(resp);
            if (tencentCode == null) {
                log.warn("smartbox 响应中未解析到 v_hint，前 160 字符: {}",
                        resp.substring(0, Math.min(160, resp.length())));
                return "名称匹配接口返回格式异常";
            }
            if (tencentCode.isEmpty()) {
                return "未匹配到该股票";
            }
            return tencentCode;
        } catch (Exception e) {
            log.error("股票名称匹配失败", e);
            return "名称匹配异常";
        }
    }

    /**
     * @return 解析到的腾讯代码（如 sh600519）；{@code v_hint="N"} 返回空串；未匹配到 v_hint 返回 {@code null}
     */
    static String parseTencentCodeFromVHint(String responseBody) {
        Matcher m = V_HINT_PATTERN.matcher(responseBody);
        if (!m.find()) {
            return null;
        }
        String inner = m.group(1);
        if ("N".equals(inner) || inner.isEmpty()) {
            return "";
        }
        String[] seg = inner.split("~", -1);
        if (seg.length < 2 || seg[0].isBlank() || seg[1].isBlank()) {
            return null;
        }
        return seg[0] + seg[1];
    }

    @Tool(description = "传入腾讯标准股票代码（sh600519/sz000001/usAAPL），获取个股完整行情数据用于技术分析")
    public String getStockDetailByCode(
            @ToolParam(description = "腾讯标准完整代码：sh/sz/us开头") String fullCode
    ) {
        String url = "https://qt.gtimg.cn/q=" + fullCode;
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "个股行情数据获取失败";
        }
    }
}
