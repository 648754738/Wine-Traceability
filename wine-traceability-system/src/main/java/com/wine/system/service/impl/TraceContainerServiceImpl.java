package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceContainer;
import com.wine.system.mapper.TraceContainerMapper;
import com.wine.system.service.ITraceContainerService;

@Service
public class TraceContainerServiceImpl implements ITraceContainerService
{
    @Autowired
    private TraceContainerMapper containerMapper;

    @Override
    public TraceContainer selectContainerById(Long containerId)
    {
        return containerMapper.selectContainerById(containerId);
    }

    @Override
    public List<TraceContainer> selectContainerList(TraceContainer container)
    {
        return containerMapper.selectContainerList(container);
    }

    @Override
    public List<TraceContainer> selectContainerOptions(Long enterpriseId, Long workshopId)
    {
        return containerMapper.selectContainerOptions(enterpriseId, workshopId);
    }

    @Override
    public boolean checkContainerCodeUnique(TraceContainer container)
    {
        Long containerId = StringUtils.isNull(container.getContainerId()) ? -1L : container.getContainerId();
        TraceContainer info = containerMapper.checkContainerCodeUnique(container.getContainerCode());
        if (StringUtils.isNotNull(info) && info.getContainerId().longValue() != containerId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasUsageByContainerId(Long containerId)
    {
        return containerMapper.hasUsageByContainerId(containerId) > 0;
    }

    @Override
    public boolean hasBottlingByContainerId(Long containerId)
    {
        return containerMapper.hasBottlingByContainerId(containerId) > 0;
    }

    @Override
    public int insertContainer(TraceContainer container)
    {
        return containerMapper.insertContainer(container);
    }

    @Override
    public int updateContainer(TraceContainer container)
    {
        return containerMapper.updateContainer(container);
    }

    @Override
    public int deleteContainerByIds(Long[] containerIds)
    {
        return containerMapper.deleteContainerByIds(containerIds);
    }
}
