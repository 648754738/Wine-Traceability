package com.wine.web.controller.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wine.common.annotation.Anonymous;
import com.wine.common.core.controller.BaseController;
import com.wine.common.core.domain.AjaxResult;
import com.wine.system.service.ITraceTraceabilityQueryService;

@RestController
@RequestMapping("/trace/query")
public class TraceTraceabilityQueryController extends BaseController
{
    @Autowired
    private ITraceTraceabilityQueryService traceabilityQueryService;

    @Anonymous
    @GetMapping("/code/{traceCode}")
    public AjaxResult publicQuery(@PathVariable String traceCode)
    {
        return success(traceabilityQueryService.queryByTraceCode(traceCode, "PUBLIC"));
    }

    @PreAuthorize("@ss.hasPermi('trace:query:query')")
    @GetMapping("/admin/{traceCode}")
    public AjaxResult adminQuery(@PathVariable String traceCode)
    {
        return success(traceabilityQueryService.queryByTraceCode(traceCode, "ADMIN"));
    }
}