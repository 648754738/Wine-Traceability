package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceGrapeVariety;

public interface ITraceGrapeVarietyService
{
    TraceGrapeVariety selectVarietyById(Long varietyId);

    List<TraceGrapeVariety> selectVarietyList(TraceGrapeVariety variety);

    List<TraceGrapeVariety> selectVarietyOptions();

    boolean checkVarietyNameUnique(TraceGrapeVariety variety);

    boolean checkVarietyUsed(Long varietyId);

    int insertVariety(TraceGrapeVariety variety);

    int updateVariety(TraceGrapeVariety variety);

    int deleteVarietyByIds(Long[] varietyIds);
}
