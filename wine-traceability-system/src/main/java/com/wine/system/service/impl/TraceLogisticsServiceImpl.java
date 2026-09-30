package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceLogistics;
import com.wine.system.mapper.TraceLogisticsMapper;
import com.wine.system.service.ITraceLogisticsService;

@Service
public class TraceLogisticsServiceImpl implements ITraceLogisticsService
{
    @Autowired
    private TraceLogisticsMapper logisticsMapper;

    @Override
    public TraceLogistics selectLogisticsById(Long logisticsId)
    {
        return logisticsMapper.selectLogisticsById(logisticsId);
    }

    @Override
    public List<TraceLogistics> selectLogisticsList(TraceLogistics logistics)
    {
        return logisticsMapper.selectLogisticsList(logistics);
    }

    @Override
    public boolean checkOutBillUnique(TraceLogistics logistics)
    {
        Long logisticsId = StringUtils.isNull(logistics.getLogisticsId()) ? -1L : logistics.getLogisticsId();
        TraceLogistics info = logisticsMapper.checkOutBillUnique(logistics.getOutBillId());
        if (StringUtils.isNotNull(info) && info.getLogisticsId().longValue() != logisticsId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int insertLogistics(TraceLogistics logistics)
    {
        return logisticsMapper.insertLogistics(logistics);
    }

    @Override
    public int updateLogistics(TraceLogistics logistics)
    {
        return logisticsMapper.updateLogistics(logistics);
    }

    @Override
    public int deleteLogisticsByIds(Long[] logisticsIds)
    {
        return logisticsMapper.deleteLogisticsByIds(logisticsIds);
    }
}