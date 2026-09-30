package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceVineyardPlot;
import com.wine.system.mapper.TraceVineyardPlotMapper;
import com.wine.system.service.ITraceVineyardPlotService;

@Service
public class TraceVineyardPlotServiceImpl implements ITraceVineyardPlotService
{
    @Autowired
    private TraceVineyardPlotMapper plotMapper;

    @Override
    public TraceVineyardPlot selectPlotById(Long plotId)
    {
        return plotMapper.selectPlotById(plotId);
    }

    @Override
    public List<TraceVineyardPlot> selectPlotList(TraceVineyardPlot plot)
    {
        return plotMapper.selectPlotList(plot);
    }

    @Override
    public List<TraceVineyardPlot> selectPlotOptions(Long enterpriseId)
    {
        return plotMapper.selectPlotOptions(enterpriseId);
    }

    @Override
    public boolean checkPlotCodeUnique(TraceVineyardPlot plot)
    {
        Long plotId = StringUtils.isNull(plot.getPlotId()) ? -1L : plot.getPlotId();
        TraceVineyardPlot info = plotMapper.checkPlotCodeUnique(plot.getPlotCode());
        if (StringUtils.isNotNull(info) && info.getPlotId().longValue() != plotId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasBatchByPlotId(Long plotId)
    {
        return plotMapper.hasBatchByPlotId(plotId) > 0;
    }

    @Override
    public int insertPlot(TraceVineyardPlot plot)
    {
        return plotMapper.insertPlot(plot);
    }

    @Override
    public int updatePlot(TraceVineyardPlot plot)
    {
        return plotMapper.updatePlot(plot);
    }

    @Override
    public int deletePlotByIds(Long[] plotIds)
    {
        return plotMapper.deletePlotByIds(plotIds);
    }
}
