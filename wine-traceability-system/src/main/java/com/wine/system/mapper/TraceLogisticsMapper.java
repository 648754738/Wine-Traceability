package com.wine.system.mapper;

import java.util.List;
import com.wine.system.domain.TraceLogistics;

public interface TraceLogisticsMapper
{
    TraceLogistics selectLogisticsById(Long logisticsId);

    List<TraceLogistics> selectLogisticsList(TraceLogistics logistics);

    TraceLogistics checkOutBillUnique(Long outBillId);

    int insertLogistics(TraceLogistics logistics);

    int updateLogistics(TraceLogistics logistics);

    int deleteLogisticsByIds(Long[] logisticsIds);
}