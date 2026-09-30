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
import com.wine.common.annotation.Anonymous;
import com.wine.common.annotation.Log;
import com.wine.common.core.controller.BaseController;
import com.wine.common.core.domain.AjaxResult;
import com.wine.common.core.page.TableDataInfo;
import com.wine.common.enums.BusinessType;
import com.wine.system.domain.TraceFeedback;
import com.wine.system.service.ITraceFeedbackService;

@RestController
@RequestMapping("/trace/feedback")
public class TraceFeedbackController extends BaseController
{
    @Autowired
    private ITraceFeedbackService feedbackService;

    @Anonymous
    @PostMapping("/public")
    public AjaxResult submitPublic(@RequestBody TraceFeedback feedback)
    {
        return toAjax(feedbackService.insertPublicFeedback(feedback));
    }

    @PreAuthorize("@ss.hasPermi('trace:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceFeedback feedback)
    {
        startPage();
        List<TraceFeedback> list = feedbackService.selectFeedbackList(feedback);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:feedback:query')")
    @GetMapping("/{feedbackId}")
    public AjaxResult getInfo(@PathVariable Long feedbackId)
    {
        return success(feedbackService.selectFeedbackById(feedbackId));
    }

    @PreAuthorize("@ss.hasPermi('trace:feedback:edit')")
    @Log(title = "Trace Feedback", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceFeedback feedback)
    {
        feedback.setUpdateBy(getUsername());
        return toAjax(feedbackService.updateFeedback(feedback));
    }

    @PreAuthorize("@ss.hasPermi('trace:feedback:remove')")
    @Log(title = "Trace Feedback", businessType = BusinessType.DELETE)
    @DeleteMapping("/{feedbackIds}")
    public AjaxResult remove(@PathVariable Long[] feedbackIds)
    {
        return toAjax(feedbackService.deleteFeedbackByIds(feedbackIds));
    }
}