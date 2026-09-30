package com.wine.system.mapper;

import java.util.List;
import com.wine.system.domain.TraceQualityReport;

public interface TraceQualityReportMapper
{
    TraceQualityReport selectQualityReportById(Long reportId);

    List<TraceQualityReport> selectQualityReportList(TraceQualityReport qualityReport);

    int hasQualityByBatchId(Long batchId);

    int insertQualityReport(TraceQualityReport qualityReport);

    int updateQualityReport(TraceQualityReport qualityReport);

    int deleteQualityReportByIds(Long[] reportIds);
}