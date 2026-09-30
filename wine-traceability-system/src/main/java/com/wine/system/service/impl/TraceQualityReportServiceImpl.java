package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.system.domain.TraceQualityReport;
import com.wine.system.mapper.TraceQualityReportMapper;
import com.wine.system.service.ITraceQualityReportService;

@Service
public class TraceQualityReportServiceImpl implements ITraceQualityReportService
{
    @Autowired
    private TraceQualityReportMapper qualityReportMapper;

    @Override
    public TraceQualityReport selectQualityReportById(Long reportId)
    {
        return qualityReportMapper.selectQualityReportById(reportId);
    }

    @Override
    public List<TraceQualityReport> selectQualityReportList(TraceQualityReport qualityReport)
    {
        return qualityReportMapper.selectQualityReportList(qualityReport);
    }

    @Override
    public int insertQualityReport(TraceQualityReport qualityReport)
    {
        return qualityReportMapper.insertQualityReport(qualityReport);
    }

    @Override
    public int updateQualityReport(TraceQualityReport qualityReport)
    {
        return qualityReportMapper.updateQualityReport(qualityReport);
    }

    @Override
    public int deleteQualityReportByIds(Long[] reportIds)
    {
        return qualityReportMapper.deleteQualityReportByIds(reportIds);
    }
}