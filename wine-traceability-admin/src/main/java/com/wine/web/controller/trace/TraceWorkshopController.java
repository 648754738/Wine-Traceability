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
import com.wine.system.domain.TraceWorkshop;
import com.wine.system.service.ITraceWorkshopService;

@RestController
@RequestMapping("/trace/workshop")
public class TraceWorkshopController extends BaseController
{
    @Autowired
    private ITraceWorkshopService workshopService;

    @PreAuthorize("@ss.hasPermi('trace:workshop:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceWorkshop workshop)
    {
        startPage();
        List<TraceWorkshop> list = workshopService.selectWorkshopList(workshop);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:workshop:list')")
    @GetMapping("/options")
    public AjaxResult options(@RequestParam(required = false) Long enterpriseId)
    {
        return success(workshopService.selectWorkshopOptions(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('trace:workshop:query')")
    @GetMapping("/{workshopId}")
    public AjaxResult getInfo(@PathVariable Long workshopId)
    {
        return success(workshopService.selectWorkshopById(workshopId));
    }

    @PreAuthorize("@ss.hasPermi('trace:workshop:add')")
    @Log(title = "车间管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceWorkshop workshop)
    {
        if (!workshopService.checkWorkshopNameUnique(workshop))
        {
            return error("新增车间'" + workshop.getWorkshopName() + "'失败，名称已存在");
        }
        workshop.setCreateBy(getUsername());
        if (StringUtils.isEmpty(workshop.getStatus()))
        {
            workshop.setStatus(UserConstants.NORMAL);
        }
        return toAjax(workshopService.insertWorkshop(workshop));
    }

    @PreAuthorize("@ss.hasPermi('trace:workshop:edit')")
    @Log(title = "车间管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceWorkshop workshop)
    {
        if (!workshopService.checkWorkshopNameUnique(workshop))
        {
            return error("修改车间'" + workshop.getWorkshopName() + "'失败，名称已存在");
        }
        workshop.setUpdateBy(getUsername());
        return toAjax(workshopService.updateWorkshop(workshop));
    }

    @PreAuthorize("@ss.hasPermi('trace:workshop:remove')")
    @Log(title = "车间管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{workshopIds}")
    public AjaxResult remove(@PathVariable Long[] workshopIds)
    {
        for (Long workshopId : workshopIds)
        {
            if (workshopService.hasContainerByWorkshopId(workshopId) || workshopService.hasBatchByWorkshopId(workshopId))
            {
                return warn("该车间存在关联容器或生产批次，不允许删除");
            }
        }
        return toAjax(workshopService.deleteWorkshopByIds(workshopIds));
    }
}
