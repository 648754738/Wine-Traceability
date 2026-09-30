package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceBottlingInfo;
import com.wine.system.mapper.TraceBottlingInfoMapper;
import com.wine.system.service.ITraceBottlingInfoService;

@Service
public class TraceBottlingInfoServiceImpl implements ITraceBottlingInfoService
{
    @Autowired
    private TraceBottlingInfoMapper bottlingInfoMapper;

    @Override
    public TraceBottlingInfo selectBottlingInfoById(Long bottlingId)
    {
        return bottlingInfoMapper.selectBottlingInfoById(bottlingId);
    }

    @Override
    public List<TraceBottlingInfo> selectBottlingInfoList(TraceBottlingInfo bottlingInfo)
    {
        return bottlingInfoMapper.selectBottlingInfoList(bottlingInfo);
    }

    @Override
    public List<TraceBottlingInfo> selectBottlingOptions(Long batchId)
    {
        return bottlingInfoMapper.selectBottlingOptions(batchId);
    }

    @Override
    public boolean checkTraceCodeRangeUnique(TraceBottlingInfo bottlingInfo)
    {
        Long bottlingId = StringUtils.isNull(bottlingInfo.getBottlingId()) ? -1L : bottlingInfo.getBottlingId();
        TraceBottlingInfo info = bottlingInfoMapper.checkTraceCodeRangeConflict(
            bottlingInfo.getTraceCodePrefix(), bottlingInfo.getTraceCodeStart(), bottlingInfo.getTraceCodeEnd());
        if (StringUtils.isNotNull(info) && info.getBottlingId().longValue() != bottlingId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasWarehouseDetailByBottlingId(Long bottlingId)
    {
        return bottlingInfoMapper.hasWarehouseDetailByBottlingId(bottlingId) > 0;
    }

    @Override
    public int insertBottlingInfo(TraceBottlingInfo bottlingInfo)
    {
        return bottlingInfoMapper.insertBottlingInfo(bottlingInfo);
    }

    @Override
    public int updateBottlingInfo(TraceBottlingInfo bottlingInfo)
    {
        return bottlingInfoMapper.updateBottlingInfo(bottlingInfo);
    }

    @Override
    public int deleteBottlingInfoByIds(Long[] bottlingIds)
    {
        return bottlingInfoMapper.deleteBottlingInfoByIds(bottlingIds);
    }
}