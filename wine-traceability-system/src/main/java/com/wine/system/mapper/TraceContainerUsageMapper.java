package com.wine.system.mapper;

import java.util.List;
import com.wine.system.domain.TraceContainerUsage;

public interface TraceContainerUsageMapper
{
    TraceContainerUsage selectContainerUsageById(Long usageId);

    List<TraceContainerUsage> selectContainerUsageList(TraceContainerUsage usage);

    int hasUsageByBatchId(Long batchId);

    int hasUsageByContainerId(Long containerId);

    int insertContainerUsage(TraceContainerUsage usage);

    int updateContainerUsage(TraceContainerUsage usage);

    int deleteContainerUsageByIds(Long[] usageIds);
}
