package com.zhn.aiagent.mcp.tool;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FreeStockMcpTool {

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
            String keyword = URLEncoder.encode(stockName, StandardCharsets.UTF_8);
            String url = UriComponentsBuilder.fromHttpUrl("https://smartbox.gtimg.cn/s3")
                    .queryParam("q", keyword)
                    .queryParam("t", "all")
                    .toUriString();

            String resp = restTemplate.getForObject(url, String.class);
            JSONObject json = JSON.parseObject(resp);
            JSONArray arr = json.getJSONArray("items");
            if (arr == null || arr.isEmpty()) {
                return "未匹配到该股票";
            }

            JSONObject first = arr.getJSONObject(0);
            String market = first.getString("market");
            String code = first.getString("code");
            String prefix = switch (market) {
                case "sh" -> "sh";
                case "sz" -> "sz";
                case "us" -> "us";
                default -> "";
            };
            return prefix + code;
        } catch (Exception e) {
            log.error("股票名称匹配失败", e);
            return "名称匹配异常";
        }
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
