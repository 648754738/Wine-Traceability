package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceGrapeVariety;
import com.wine.system.mapper.TraceGrapeVarietyMapper;
import com.wine.system.service.ITraceGrapeVarietyService;

@Service
public class TraceGrapeVarietyServiceImpl implements ITraceGrapeVarietyService
{
    @Autowired
    private TraceGrapeVarietyMapper varietyMapper;

    @Override
    public TraceGrapeVariety selectVarietyById(Long varietyId)
    {
        return varietyMapper.selectVarietyById(varietyId);
    }

    @Override
    public List<TraceGrapeVariety> selectVarietyList(TraceGrapeVariety variety)
    {
        return varietyMapper.selectVarietyList(variety);
    }

    @Override
    public List<TraceGrapeVariety> selectVarietyOptions()
    {
        return varietyMapper.selectVarietyOptions();
    }

    @Override
    public boolean checkVarietyNameUnique(TraceGrapeVariety variety)
    {
        Long varietyId = StringUtils.isNull(variety.getVarietyId()) ? -1L : variety.getVarietyId();
        TraceGrapeVariety info = varietyMapper.checkVarietyNameUnique(variety.getVarietyName());
        if (StringUtils.isNotNull(info) && info.getVarietyId().longValue() != varietyId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkVarietyUsed(Long varietyId)
    {
        return varietyMapper.checkVarietyUsed(varietyId) > 0;
    }

    @Override
    public int insertVariety(TraceGrapeVariety variety)
    {
        return varietyMapper.insertVariety(variety);
    }

    @Override
    public int updateVariety(TraceGrapeVariety variety)
    {
        return varietyMapper.updateVariety(variety);
    }

    @Override
    public int deleteVarietyByIds(Long[] varietyIds)
    {
        return varietyMapper.deleteVarietyByIds(varietyIds);
    }
}
