package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.TraceContainer;

public interface TraceContainerMapper
{
    TraceContainer selectContainerById(Long containerId);

    List<TraceContainer> selectContainerList(TraceContainer container);

    List<TraceContainer> selectContainerOptions(@Param("enterpriseId") Long enterpriseId, @Param("workshopId") Long workshopId);

    TraceContainer checkContainerCodeUnique(String containerCode);

    int hasContainerByEnterpriseId(Long enterpriseId);

    int hasUsageByContainerId(Long containerId);

    int hasBottlingByContainerId(Long containerId);

    int insertContainer(TraceContainer container);

    int updateContainer(TraceContainer container);

    int deleteContainerByIds(Long[] containerIds);
}
