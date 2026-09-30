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
import org.springframework.web.bind.annotation.RestController;
import com.wine.common.annotation.Log;
import com.wine.common.core.controller.BaseController;
import com.wine.common.core.domain.AjaxResult;
import com.wine.common.core.page.TableDataInfo;
import com.wine.common.enums.BusinessType;
import com.wine.system.domain.TraceLogistics;
import com.wine.system.service.ITraceLogisticsService;

@RestController
@RequestMapping("/trace/logistics")
public class TraceLogisticsController extends BaseController
{
    @Autowired
    private ITraceLogisticsService logisticsService;

    @PreAuthorize("@ss.hasPermi('trace:logistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceLogistics logistics)
    {
        startPage();
        List<TraceLogistics> list = logisticsService.selectLogisticsList(logistics);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:logistics:query')")
    @GetMapping("/{logisticsId}")
    public AjaxResult getInfo(@PathVariable Long logisticsId)
    {
        return success(logisticsService.selectLogisticsById(logisticsId));
    }

    @PreAuthorize("@ss.hasPermi('trace:logistics:add')")
    @Log(title = "物流信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceLogistics logistics)
    {
        if (!logisticsService.checkOutBillUnique(logistics))
        {
            return error("新增物流失败，该出库单已存在物流信息");
        }
        logistics.setCreateBy(getUsername());
        return toAjax(logisticsService.insertLogistics(logistics));
    }

    @PreAuthorize("@ss.hasPermi('trace:logistics:edit')")
    @Log(title = "物流信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceLogistics logistics)
    {
        if (!logisticsService.checkOutBillUnique(logistics))
        {
            return error("修改物流失败，该出库单已存在物流信息");
        }
        logistics.setUpdateBy(getUsername());
        return toAjax(logisticsService.updateLogistics(logistics));
    }

    @PreAuthorize("@ss.hasPermi('trace:logistics:remove')")
    @Log(title = "物流信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logisticsIds}")
    public AjaxResult remove(@PathVariable Long[] logisticsIds)
    {
        return toAjax(logisticsService.deleteLogisticsByIds(logisticsIds));
    }
}