package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.TraceVineyardPlot;

public interface TraceVineyardPlotMapper
{
    TraceVineyardPlot selectPlotById(Long plotId);

    List<TraceVineyardPlot> selectPlotList(TraceVineyardPlot plot);

    List<TraceVineyardPlot> selectPlotOptions(@Param("enterpriseId") Long enterpriseId);

    TraceVineyardPlot checkPlotCodeUnique(@Param("plotCode") String plotCode);

    int hasBatchByPlotId(Long plotId);

    int insertPlot(TraceVineyardPlot plot);

    int updatePlot(TraceVineyardPlot plot);

    int deletePlotByIds(Long[] plotIds);
}
