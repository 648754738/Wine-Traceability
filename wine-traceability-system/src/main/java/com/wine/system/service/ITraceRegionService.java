package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceRegion;

public interface ITraceRegionService
{
    List<TraceRegion> selectRegionList(TraceRegion region);

    List<TraceRegion> buildRegionTree(List<TraceRegion> regions);

    TraceRegion selectRegionById(Long regionId);

    boolean checkRegionNameUnique(TraceRegion region);

    boolean hasChildByRegionId(Long regionId);

    boolean checkRegionExistEnterprise(Long regionId);

    int selectNormalChildrenRegionById(Long regionId);

    int insertRegion(TraceRegion region);

    int updateRegion(TraceRegion region);

    int deleteRegionById(Long regionId);
}
