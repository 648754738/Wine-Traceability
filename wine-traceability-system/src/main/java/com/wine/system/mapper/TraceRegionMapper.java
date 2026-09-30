package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.TraceRegion;

public interface TraceRegionMapper
{
    List<TraceRegion> selectRegionList(TraceRegion region);

    TraceRegion selectRegionById(Long regionId);

    List<TraceRegion> selectRegionByIds(Long[] regionIds);

    List<TraceRegion> selectChildrenRegionById(Long regionId);

    int selectNormalChildrenRegionById(Long regionId);

    int hasChildByRegionId(Long regionId);

    int checkRegionExistEnterprise(Long regionId);

    TraceRegion checkRegionNameUnique(@Param("regionName") String regionName, @Param("parentId") Long parentId);

    int insertRegion(TraceRegion region);

    int updateRegion(TraceRegion region);

    int updateRegionChildren(@Param("regions") List<TraceRegion> regions);

    int updateRegionStatusNormal(Long[] regionIds);

    int deleteRegionById(Long regionId);
}