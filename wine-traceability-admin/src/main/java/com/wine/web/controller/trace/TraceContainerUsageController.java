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
import com.wine.system.domain.TraceContainerUsage;
import com.wine.system.service.ITraceContainerUsageService;

@RestController
@RequestMapping("/trace/usage")
public class TraceContainerUsageController extends BaseController
{
    @Autowired
    private ITraceContainerUsageService containerUsageService;

    @PreAuthorize("@ss.hasPermi('trace:usage:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceContainerUsage usage)
    {
        startPage();
        List<TraceContainerUsage> list = containerUsageService.selectContainerUsageList(usage);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:usage:query')")
    @GetMapping("/{usageId}")
    public AjaxResult getInfo(@PathVariable Long usageId)
    {
        return success(containerUsageService.selectContainerUsageById(usageId));
    }

    @PreAuthorize("@ss.hasPermi('trace:usage:add')")
    @Log(title = "容器流转", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceContainerUsage usage)
    {
        usage.setCreateBy(getUsername());
        return toAjax(containerUsageService.insertContainerUsage(usage));
    }

    @PreAuthorize("@ss.hasPermi('trace:usage:edit')")
    @Log(title = "容器流转", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceContainerUsage usage)
    {
        usage.setUpdateBy(getUsername());
        return toAjax(containerUsageService.updateContainerUsage(usage));
    }

    @PreAuthorize("@ss.hasPermi('trace:usage:remove')")
    @Log(title = "容器流转", businessType = BusinessType.DELETE)
    @DeleteMapping("/{usageIds}")
    public AjaxResult remove(@PathVariable Long[] usageIds)
    {
        return toAjax(containerUsageService.deleteContainerUsageByIds(usageIds));
    }
}
