package com.wine.system.mapper;

import java.util.List;
import java.util.Map;

public interface TraceStatsMapper
{
    Integer selectTotalQueryCount();

    Integer selectTodayQueryCount();

    Integer selectTotalFeedbackCount();

    Integer selectSuspiciousFeedbackCount();

    Double selectAverageRating();

    Integer selectTotalBatchCount();

    Integer selectProcessCoveredBatchCount();

    Integer selectQualityCoveredBatchCount();

    Integer selectBottlingCoveredBatchCount();

    Integer selectWarehouseCoveredBatchCount();

    List<Map<String, Object>> selectRecentQueryTrend();

    List<Map<String, Object>> selectTopProducts();

    List<Map<String, Object>> selectFeedbackRatingStats();

    List<Map<String, Object>> selectRecentSuspiciousFeedback();
}