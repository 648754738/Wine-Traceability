package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceContainerUsage;

public interface ITraceContainerUsageService
{
    TraceContainerUsage selectContainerUsageById(Long usageId);

    List<TraceContainerUsage> selectContainerUsageList(TraceContainerUsage usage);

    int insertContainerUsage(TraceContainerUsage usage);

    int updateContainerUsage(TraceContainerUsage usage);

    int deleteContainerUsageByIds(Long[] usageIds);
}
