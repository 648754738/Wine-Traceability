package com.wine.web.controller.trace;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wine.common.annotation.Log;
import com.wine.common.constant.UserConstants;
import com.wine.common.core.controller.BaseController;
import com.wine.common.core.domain.AjaxResult;
import com.wine.common.core.page.TableDataInfo;
import com.wine.common.enums.BusinessType;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceVineyardPlot;
import com.wine.system.service.ITraceVineyardPlotService;

@RestController
@RequestMapping("/trace/plot")
public class TraceVineyardPlotController extends BaseController
{
    @Autowired
    private ITraceVineyardPlotService plotService;

    @PreAuthorize("@ss.hasPermi('trace:plot:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceVineyardPlot plot)
    {
        startPage();
        List<TraceVineyardPlot> list = plotService.selectPlotList(plot);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:plot:list')")
    @GetMapping("/options")
    public AjaxResult options(@RequestParam(required = false) Long enterpriseId)
    {
        return success(plotService.selectPlotOptions(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('trace:plot:query')")
    @GetMapping("/{plotId}")
    public AjaxResult getInfo(@PathVariable Long plotId)
    {
        return success(plotService.selectPlotById(plotId));
    }

    @PreAuthorize("@ss.hasPermi('trace:plot:add')")
    @Log(title = "地块管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceVineyardPlot plot)
    {
        if (!plotService.checkPlotCodeUnique(plot))
        {
            return error("新增地块'" + plot.getPlotName() + "'失败，地块编码已存在");
        }
        plot.setCreateBy(getUsername());
        if (StringUtils.isEmpty(plot.getStatus()))
        {
            plot.setStatus(UserConstants.NORMAL);
        }
        return toAjax(plotService.insertPlot(plot));
    }

    @PreAuthorize("@ss.hasPermi('trace:plot:edit')")
    @Log(title = "地块管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceVineyardPlot plot)
    {
        if (!plotService.checkPlotCodeUnique(plot))
        {
            return error("修改地块'" + plot.getPlotName() + "'失败，地块编码已存在");
        }
        plot.setUpdateBy(getUsername());
        return toAjax(plotService.updatePlot(plot));
    }

    @PreAuthorize("@ss.hasPermi('trace:plot:remove')")
    @Log(title = "地块管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{plotIds}")
    public AjaxResult remove(@PathVariable Long[] plotIds)
    {
        for (Long plotId : plotIds)
        {
            if (plotService.hasBatchByPlotId(plotId))
            {
                return warn("该地块已关联生产批次，不允许删除");
            }
        }
        return toAjax(plotService.deletePlotByIds(plotIds));
    }
}
