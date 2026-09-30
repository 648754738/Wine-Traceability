package com.wine.system.mapper;

import java.util.List;
import com.wine.system.domain.TraceGrapeVariety;

public interface TraceGrapeVarietyMapper
{
    TraceGrapeVariety selectVarietyById(Long varietyId);

    List<TraceGrapeVariety> selectVarietyList(TraceGrapeVariety variety);

    List<TraceGrapeVariety> selectVarietyOptions();

    TraceGrapeVariety checkVarietyNameUnique(String varietyName);

    int checkVarietyUsed(Long varietyId);

    int insertVariety(TraceGrapeVariety variety);

    int updateVariety(TraceGrapeVariety variety);

    int deleteVarietyByIds(Long[] varietyIds);
}
