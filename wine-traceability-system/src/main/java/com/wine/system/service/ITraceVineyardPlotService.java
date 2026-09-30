package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceVineyardPlot;

public interface ITraceVineyardPlotService
{
    TraceVineyardPlot selectPlotById(Long plotId);

    List<TraceVineyardPlot> selectPlotList(TraceVineyardPlot plot);

    List<TraceVineyardPlot> selectPlotOptions(Long enterpriseId);

    boolean checkPlotCodeUnique(TraceVineyardPlot plot);

    boolean hasBatchByPlotId(Long plotId);

    int insertPlot(TraceVineyardPlot plot);

    int updatePlot(TraceVineyardPlot plot);

    int deletePlotByIds(Long[] plotIds);
}
