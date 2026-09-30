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
import com.wine.common.core.controller.BaseController;
import com.wine.common.core.domain.AjaxResult;
import com.wine.common.core.page.TableDataInfo;
import com.wine.common.enums.BusinessType;
import com.wine.system.domain.TraceBottlingInfo;
import com.wine.system.service.ITraceBottlingInfoService;

@RestController
@RequestMapping("/trace/bottling")
public class TraceBottlingInfoController extends BaseController
{
    @Autowired
    private ITraceBottlingInfoService bottlingInfoService;

    @PreAuthorize("@ss.hasPermi('trace:bottling:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceBottlingInfo bottlingInfo)
    {
        startPage();
        List<TraceBottlingInfo> list = bottlingInfoService.selectBottlingInfoList(bottlingInfo);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:bottling:list')")
    @GetMapping("/options")
    public AjaxResult options(@RequestParam(required = false) Long batchId)
    {
        return success(bottlingInfoService.selectBottlingOptions(batchId));
    }

    @PreAuthorize("@ss.hasPermi('trace:bottling:query')")
    @GetMapping("/{bottlingId}")
    public AjaxResult getInfo(@PathVariable Long bottlingId)
    {
        return success(bottlingInfoService.selectBottlingInfoById(bottlingId));
    }

    @PreAuthorize("@ss.hasPermi('trace:bottling:add')")
    @Log(title = "灌装信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceBottlingInfo bottlingInfo)
    {
        if (bottlingInfo.getTraceCodeEnd() < bottlingInfo.getTraceCodeStart())
        {
            return error("结束序号不能小于起始序号");
        }
        if (!bottlingInfoService.checkTraceCodeRangeUnique(bottlingInfo))
        {
            return error("新增灌装失败，溯源码区间与现有记录冲突");
        }
        bottlingInfo.setCreateBy(getUsername());
        return toAjax(bottlingInfoService.insertBottlingInfo(bottlingInfo));
    }

    @PreAuthorize("@ss.hasPermi('trace:bottling:edit')")
    @Log(title = "灌装信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceBottlingInfo bottlingInfo)
    {
        if (bottlingInfo.getTraceCodeEnd() < bottlingInfo.getTraceCodeStart())
        {
            return error("结束序号不能小于起始序号");
        }
        if (!bottlingInfoService.checkTraceCodeRangeUnique(bottlingInfo))
        {
            return error("修改灌装失败，溯源码区间与现有记录冲突");
        }
        bottlingInfo.setUpdateBy(getUsername());
        return toAjax(bottlingInfoService.updateBottlingInfo(bottlingInfo));
    }

    @PreAuthorize("@ss.hasPermi('trace:bottling:remove')")
    @Log(title = "灌装信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bottlingIds}")
    public AjaxResult remove(@PathVariable Long[] bottlingIds)
    {
        for (Long bottlingId : bottlingIds)
        {
            if (bottlingInfoService.hasWarehouseDetailByBottlingId(bottlingId))
            {
                return warn("该灌装记录已被仓储单据引用，不允许删除");
            }
        }
        return toAjax(bottlingInfoService.deleteBottlingInfoByIds(bottlingIds));
    }
}