package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceContainer;

public interface ITraceContainerService
{
    TraceContainer selectContainerById(Long containerId);

    List<TraceContainer> selectContainerList(TraceContainer container);

    List<TraceContainer> selectContainerOptions(Long enterpriseId, Long workshopId);

    boolean checkContainerCodeUnique(TraceContainer container);

    boolean hasUsageByContainerId(Long containerId);

    boolean hasBottlingByContainerId(Long containerId);

    int insertContainer(TraceContainer container);

    int updateContainer(TraceContainer container);

    int deleteContainerByIds(Long[] containerIds);
}
