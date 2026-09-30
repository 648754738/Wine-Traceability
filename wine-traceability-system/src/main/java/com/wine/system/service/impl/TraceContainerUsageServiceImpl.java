package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.system.domain.TraceContainerUsage;
import com.wine.system.mapper.TraceContainerUsageMapper;
import com.wine.system.service.ITraceContainerUsageService;

@Service
public class TraceContainerUsageServiceImpl implements ITraceContainerUsageService
{
    @Autowired
    private TraceContainerUsageMapper containerUsageMapper;

    @Override
    public TraceContainerUsage selectContainerUsageById(Long usageId)
    {
        return containerUsageMapper.selectContainerUsageById(usageId);
    }

    @Override
    public List<TraceContainerUsage> selectContainerUsageList(TraceContainerUsage usage)
    {
        return containerUsageMapper.selectContainerUsageList(usage);
    }

    @Override
    public int insertContainerUsage(TraceContainerUsage usage)
    {
        return containerUsageMapper.insertContainerUsage(usage);
    }

    @Override
    public int updateContainerUsage(TraceContainerUsage usage)
    {
        return containerUsageMapper.updateContainerUsage(usage);
    }

    @Override
    public int deleteContainerUsageByIds(Long[] usageIds)
    {
        return containerUsageMapper.deleteContainerUsageByIds(usageIds);
    }
}
