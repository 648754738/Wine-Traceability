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
import com.wine.system.domain.TraceProcessRecord;
import com.wine.system.service.ITraceProcessRecordService;

@RestController
@RequestMapping("/trace/process")
public class TraceProcessRecordController extends BaseController
{
    @Autowired
    private ITraceProcessRecordService processRecordService;

    @PreAuthorize("@ss.hasPermi('trace:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceProcessRecord processRecord)
    {
        startPage();
        List<TraceProcessRecord> list = processRecordService.selectProcessRecordList(processRecord);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:process:query')")
    @GetMapping("/{processId}")
    public AjaxResult getInfo(@PathVariable Long processId)
    {
        return success(processRecordService.selectProcessRecordById(processId));
    }

    @PreAuthorize("@ss.hasPermi('trace:process:add')")
    @Log(title = "工序记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceProcessRecord processRecord)
    {
        processRecord.setCreateBy(getUsername());
        return toAjax(processRecordService.insertProcessRecord(processRecord));
    }

    @PreAuthorize("@ss.hasPermi('trace:process:edit')")
    @Log(title = "工序记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceProcessRecord processRecord)
    {
        processRecord.setUpdateBy(getUsername());
        return toAjax(processRecordService.updateProcessRecord(processRecord));
    }

    @PreAuthorize("@ss.hasPermi('trace:process:remove')")
    @Log(title = "工序记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{processIds}")
    public AjaxResult remove(@PathVariable Long[] processIds)
    {
        return toAjax(processRecordService.deleteProcessRecordByIds(processIds));
    }
}
