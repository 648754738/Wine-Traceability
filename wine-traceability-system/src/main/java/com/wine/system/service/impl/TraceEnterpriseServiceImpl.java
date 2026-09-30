package com.wine.system.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceEnterprise;
import com.wine.system.mapper.TraceEnterpriseMapper;
import com.wine.system.service.ITraceEnterpriseService;

@Service
public class TraceEnterpriseServiceImpl implements ITraceEnterpriseService
{
    @Autowired
    private TraceEnterpriseMapper enterpriseMapper;

    @Override
    public TraceEnterprise selectEnterpriseById(Long enterpriseId)
    {
        return enterpriseMapper.selectEnterpriseById(enterpriseId);
    }

    @Override
    public List<TraceEnterprise> selectEnterpriseList(TraceEnterprise enterprise)
    {
        return enterpriseMapper.selectEnterpriseList(enterprise);
    }

    @Override
    public List<TraceEnterprise> selectEnterpriseOptions()
    {
        return enterpriseMapper.selectEnterpriseOptions();
    }

    @Override
    public boolean checkEnterpriseNameUnique(TraceEnterprise enterprise)
    {
        Long enterpriseId = StringUtils.isNull(enterprise.getEnterpriseId()) ? -1L : enterprise.getEnterpriseId();
        TraceEnterprise info = enterpriseMapper.checkEnterpriseNameUnique(enterprise.getEnterpriseName());
        if (StringUtils.isNotNull(info) && info.getEnterpriseId().longValue() != enterpriseId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkLicenseNoUnique(TraceEnterprise enterprise)
    {
        if (StringUtils.isEmpty(enterprise.getLicenseNo()))
        {
            return UserConstants.UNIQUE;
        }
        Long enterpriseId = StringUtils.isNull(enterprise.getEnterpriseId()) ? -1L : enterprise.getEnterpriseId();
        TraceEnterprise info = enterpriseMapper.checkLicenseNoUnique(enterprise.getLicenseNo());
        if (StringUtils.isNotNull(info) && info.getEnterpriseId().longValue() != enterpriseId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasPlotByEnterpriseId(Long enterpriseId)
    {
        return enterpriseMapper.hasPlotByEnterpriseId(enterpriseId) > 0;
    }

    @Override
    public boolean hasProductByEnterpriseId(Long enterpriseId)
    {
        return enterpriseMapper.hasProductByEnterpriseId(enterpriseId) > 0;
    }

    @Override
    public boolean hasWorkshopByEnterpriseId(Long enterpriseId)
    {
        return enterpriseMapper.hasWorkshopByEnterpriseId(enterpriseId) > 0;
    }

    @Override
    public boolean hasContainerByEnterpriseId(Long enterpriseId)
    {
        return enterpriseMapper.hasContainerByEnterpriseId(enterpriseId) > 0;
    }

    @Override
    public int insertEnterprise(TraceEnterprise enterprise)
    {
        return enterpriseMapper.insertEnterprise(enterprise);
    }

    @Override
    public int updateEnterprise(TraceEnterprise enterprise)
    {
        return enterpriseMapper.updateEnterprise(enterprise);
    }

    @Override
    public int deleteEnterpriseByIds(Long[] enterpriseIds)
    {
        return enterpriseMapper.deleteEnterpriseByIds(enterpriseIds);
    }
}
