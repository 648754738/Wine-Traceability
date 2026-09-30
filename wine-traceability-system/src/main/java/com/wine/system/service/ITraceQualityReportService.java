package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceQualityReport;

public interface ITraceQualityReportService
{
    TraceQualityReport selectQualityReportById(Long reportId);

    List<TraceQualityReport> selectQualityReportList(TraceQualityReport qualityReport);

    int insertQualityReport(TraceQualityReport qualityReport);

    int updateQualityReport(TraceQualityReport qualityReport);

    int deleteQualityReportByIds(Long[] reportIds);
}