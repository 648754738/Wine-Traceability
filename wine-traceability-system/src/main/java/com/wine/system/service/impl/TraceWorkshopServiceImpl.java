package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceWorkshop;
import com.wine.system.mapper.TraceWorkshopMapper;
import com.wine.system.service.ITraceWorkshopService;

@Service
public class TraceWorkshopServiceImpl implements ITraceWorkshopService
{
    @Autowired
    private TraceWorkshopMapper workshopMapper;

    @Override
    public TraceWorkshop selectWorkshopById(Long workshopId)
    {
        return workshopMapper.selectWorkshopById(workshopId);
    }

    @Override
    public List<TraceWorkshop> selectWorkshopList(TraceWorkshop workshop)
    {
        return workshopMapper.selectWorkshopList(workshop);
    }

    @Override
    public List<TraceWorkshop> selectWorkshopOptions(Long enterpriseId)
    {
        return workshopMapper.selectWorkshopOptions(enterpriseId);
    }

    @Override
    public boolean checkWorkshopNameUnique(TraceWorkshop workshop)
    {
        Long workshopId = StringUtils.isNull(workshop.getWorkshopId()) ? -1L : workshop.getWorkshopId();
        TraceWorkshop info = workshopMapper.checkWorkshopNameUnique(workshop.getEnterpriseId(), workshop.getWorkshopName());
        if (StringUtils.isNotNull(info) && info.getWorkshopId().longValue() != workshopId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasContainerByWorkshopId(Long workshopId)
    {
        return workshopMapper.hasContainerByWorkshopId(workshopId) > 0;
    }

    @Override
    public boolean hasBatchByWorkshopId(Long workshopId)
    {
        return workshopMapper.hasBatchByWorkshopId(workshopId) > 0;
    }

    @Override
    public int insertWorkshop(TraceWorkshop workshop)
    {
        return workshopMapper.insertWorkshop(workshop);
    }

    @Override
    public int updateWorkshop(TraceWorkshop workshop)
    {
        return workshopMapper.updateWorkshop(workshop);
    }

    @Override
    public int deleteWorkshopByIds(Long[] workshopIds)
    {
        return workshopMapper.deleteWorkshopByIds(workshopIds);
    }
}
