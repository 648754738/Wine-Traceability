package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.TraceWorkshop;

public interface TraceWorkshopMapper
{
    TraceWorkshop selectWorkshopById(Long workshopId);

    List<TraceWorkshop> selectWorkshopList(TraceWorkshop workshop);

    List<TraceWorkshop> selectWorkshopOptions(@Param("enterpriseId") Long enterpriseId);

    TraceWorkshop checkWorkshopNameUnique(@Param("enterpriseId") Long enterpriseId, @Param("workshopName") String workshopName);

    int hasContainerByWorkshopId(Long workshopId);

    int hasBatchByWorkshopId(Long workshopId);

    int hasWorkshopByEnterpriseId(Long enterpriseId);

    int insertWorkshop(TraceWorkshop workshop);

    int updateWorkshop(TraceWorkshop workshop);

    int deleteWorkshopByIds(Long[] workshopIds);
}
