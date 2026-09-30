package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceLogistics;

public interface ITraceLogisticsService
{
    TraceLogistics selectLogisticsById(Long logisticsId);

    List<TraceLogistics> selectLogisticsList(TraceLogistics logistics);

    boolean checkOutBillUnique(TraceLogistics logistics);

    int insertLogistics(TraceLogistics logistics);

    int updateLogistics(TraceLogistics logistics);

    int deleteLogisticsByIds(Long[] logisticsIds);
}