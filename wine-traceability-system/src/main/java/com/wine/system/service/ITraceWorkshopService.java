package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceWorkshop;

public interface ITraceWorkshopService
{
    TraceWorkshop selectWorkshopById(Long workshopId);

    List<TraceWorkshop> selectWorkshopList(TraceWorkshop workshop);

    List<TraceWorkshop> selectWorkshopOptions(Long enterpriseId);

    boolean checkWorkshopNameUnique(TraceWorkshop workshop);

    boolean hasContainerByWorkshopId(Long workshopId);

    boolean hasBatchByWorkshopId(Long workshopId);

    int insertWorkshop(TraceWorkshop workshop);

    int updateWorkshop(TraceWorkshop workshop);

    int deleteWorkshopByIds(Long[] workshopIds);
}
