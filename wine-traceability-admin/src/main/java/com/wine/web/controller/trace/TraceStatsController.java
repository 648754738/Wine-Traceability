package com.wine.web.controller.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wine.common.core.controller.BaseController;
import com.wine.common.core.domain.AjaxResult;
import com.wine.system.service.ITraceStatsService;

@RestController
@RequestMapping("/trace/stats")
public class TraceStatsController extends BaseController
{
    @Autowired
    private ITraceStatsService statsService;

    @PreAuthorize("@ss.hasPermi('trace:stats:view')")
    @GetMapping("/dashboard")
    public AjaxResult dashboard()
    {
        return success(statsService.getDashboardData());
    }
}