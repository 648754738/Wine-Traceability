package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceEnterprise;

public interface ITraceEnterpriseService
{
    TraceEnterprise selectEnterpriseById(Long enterpriseId);

    List<TraceEnterprise> selectEnterpriseList(TraceEnterprise enterprise);

    List<TraceEnterprise> selectEnterpriseOptions();

    boolean checkEnterpriseNameUnique(TraceEnterprise enterprise);

    boolean checkLicenseNoUnique(TraceEnterprise enterprise);

    boolean hasPlotByEnterpriseId(Long enterpriseId);

    boolean hasProductByEnterpriseId(Long enterpriseId);

    boolean hasWorkshopByEnterpriseId(Long enterpriseId);

    boolean hasContainerByEnterpriseId(Long enterpriseId);

    int insertEnterprise(TraceEnterprise enterprise);

    int updateEnterprise(TraceEnterprise enterprise);

    int deleteEnterpriseByIds(Long[] enterpriseIds);
}
