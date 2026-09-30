package com.wine.web.controller.trace;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.wine.system.domain.TraceQualityReport;
import com.wine.system.service.ITraceQualityReportService;

@RestController
@RequestMapping("/trace/quality")
public class TraceQualityReportController extends BaseController
{
    @Autowired
    private ITraceQualityReportService qualityReportService;

    @PreAuthorize("@ss.hasPermi('trace:quality:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceQualityReport qualityReport)
    {
        startPage();
        List<TraceQualityReport> list = qualityReportService.selectQualityReportList(qualityReport);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:quality:query')")
    @GetMapping("/{reportId}")
    public AjaxResult getInfo(@PathVariable Long reportId)
    {
        return success(qualityReportService.selectQualityReportById(reportId));
    }

    @PreAuthorize("@ss.hasPermi('trace:quality:add')")
    @Log(title = "质检报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TraceQualityReport qualityReport)
    {
        qualityReport.setCreateBy(getUsername());
        return toAjax(qualityReportService.insertQualityReport(qualityReport));
    }

    @PreAuthorize("@ss.hasPermi('trace:quality:edit')")
    @Log(title = "质检报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TraceQualityReport qualityReport)
    {
        qualityReport.setUpdateBy(getUsername());
        return toAjax(qualityReportService.updateQualityReport(qualityReport));
    }

    @PreAuthorize("@ss.hasPermi('trace:quality:remove')")
    @Log(title = "质检报告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reportIds}")
    public AjaxResult remove(@PathVariable Long[] reportIds)
    {
        return toAjax(qualityReportService.deleteQualityReportByIds(reportIds));
    }
}