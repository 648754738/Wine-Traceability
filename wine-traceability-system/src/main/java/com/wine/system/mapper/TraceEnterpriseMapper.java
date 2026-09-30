package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.TraceEnterprise;

public interface TraceEnterpriseMapper
{
    TraceEnterprise selectEnterpriseById(Long enterpriseId);

    List<TraceEnterprise> selectEnterpriseList(TraceEnterprise enterprise);

    List<TraceEnterprise> selectEnterpriseOptions();

    TraceEnterprise checkEnterpriseNameUnique(String enterpriseName);

    TraceEnterprise checkLicenseNoUnique(@Param("licenseNo") String licenseNo);

    int hasPlotByEnterpriseId(Long enterpriseId);

    int hasProductByEnterpriseId(Long enterpriseId);

    int hasWorkshopByEnterpriseId(Long enterpriseId);

    int hasContainerByEnterpriseId(Long enterpriseId);

    int insertEnterprise(TraceEnterprise enterprise);

    int updateEnterprise(TraceEnterprise enterprise);

    int deleteEnterpriseByIds(Long[] enterpriseIds);
}
