package com.wine.system.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.system.mapper.TraceStatsMapper;
import com.wine.system.service.ITraceStatsService;

@Service
public class TraceStatsServiceImpl implements ITraceStatsService
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    @Autowired
    private TraceStatsMapper statsMapper;

    @Override
    public Map<String, Object> getDashboardData()
    {
        Map<String, Object> data = new LinkedHashMap<>();
        Map<String, Object> overview = new LinkedHashMap<>();
        Integer totalQueries = defaultZero(statsMapper.selectTotalQueryCount());
        Integer todayQueries = defaultZero(statsMapper.selectTodayQueryCount());
        Integer totalFeedback = defaultZero(statsMapper.selectTotalFeedbackCount());
        Integer suspiciousFeedback = defaultZero(statsMapper.selectSuspiciousFeedbackCount());
        Double averageRating = statsMapper.selectAverageRating();
        Integer totalBatch = defaultZero(statsMapper.selectTotalBatchCount());
        Integer processCovered = defaultZero(statsMapper.selectProcessCoveredBatchCount());
        Integer qualityCovered = defaultZero(statsMapper.selectQualityCoveredBatchCount());
        Integer bottlingCovered = defaultZero(statsMapper.selectBottlingCoveredBatchCount());
        Integer warehouseCovered = defaultZero(statsMapper.selectWarehouseCoveredBatchCount());
        overview.put("totalQueries", totalQueries);
        overview.put("todayQueries", todayQueries);
        overview.put("totalFeedback", totalFeedback);
        overview.put("suspiciousFeedback", suspiciousFeedback);
        overview.put("averageRating", averageRating == null ? 0D : averageRating);
        overview.put("batchCount", totalBatch);
        data.put("overview", overview);
        data.put("queryTrend", buildTrend(statsMapper.selectRecentQueryTrend()));
        data.put("topProducts", statsMapper.selectTopProducts());
        data.put("feedbackRatings", statsMapper.selectFeedbackRatingStats());
        data.put("coverage", buildCoverage(totalBatch, processCovered, qualityCovered, bottlingCovered, warehouseCovered));
        data.put("recentSuspicious", statsMapper.selectRecentSuspiciousFeedback());
        return data;
    }

    private List<Map<String, Object>> buildTrend(List<Map<String, Object>> dbTrend)
    {
        Map<String, Integer> trendMap = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--)
        {
            String label = LocalDate.now().minusDays(i).format(FORMATTER);
            trendMap.put(label, 0);
        }
        if (dbTrend != null)
        {
            for (Map<String, Object> item : dbTrend)
            {
                Object day = item.get("dayLabel");
                Object value = item.get("queryCount");
                if (day != null)
                {
                    trendMap.put(String.valueOf(day), value == null ? 0 : Integer.parseInt(String.valueOf(value)));
                }
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        trendMap.forEach((key, value) -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("label", key);
            row.put("value", value);
            result.add(row);
        });
        return result;
    }

    private List<Map<String, Object>> buildCoverage(Integer totalBatch, Integer processCovered, Integer qualityCovered,
        Integer bottlingCovered, Integer warehouseCovered)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(coverageItem("工序记录", totalBatch, processCovered));
        result.add(coverageItem("质检记录", totalBatch, qualityCovered));
        result.add(coverageItem("灌装记录", totalBatch, bottlingCovered));
        result.add(coverageItem("仓储记录", totalBatch, warehouseCovered));
        return result;
    }

    private Map<String, Object> coverageItem(String name, Integer total, Integer covered)
    {
        Map<String, Object> row = new LinkedHashMap<>();
        int totalVal = defaultZero(total);
        int coveredVal = defaultZero(covered);
        double rate = totalVal == 0 ? 0D : coveredVal * 100D / totalVal;
        row.put("name", name);
        row.put("covered", coveredVal);
        row.put("total", totalVal);
        row.put("rate", Math.round(rate * 100D) / 100D);
        return row;
    }

    private Integer defaultZero(Integer value)
    {
        return value == null ? 0 : value;
    }
}