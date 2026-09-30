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
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceContainer;
import com.wine.system.service.ITraceContainerService;

@RestController
@RequestMapping("/trace/container")
public class TraceContainerController extends BaseController
{
    @Autowired
    private ITraceContainerService containerService;

    @PreAuthorize("@ss.hasPermi('trace:container:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceContainer container)
    {
        startPage();
        List<TraceContainer> list = containerService.selectContainerList(container);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:container:list')")
    @GetMapping("/options")
    public AjaxResult options(@RequestParam(required = false) Long enterpriseId, @RequestParam(required = false) Long workshopId)
    {
        return success(containerService.selectContainerOptions(enterpriseId, workshopId));
    }

    @PreAuthorize("@ss.hasPermi('trace:container:query')")
    @GetMapping("/{containerId}")
    public AjaxResult getInfo(@PathVariable Long containerId)
    {
        return success(containerService.selectContainerById(containerId));
    }

    @PreAuthorize("@ss.hasPermi('trace:container:add')")
    @Log(title = "容器管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceContainer container)
    {
        if (!containerService.checkContainerCodeUnique(container))
        {
            return error("新增容器'" + container.getContainerCode() + "'失败，编码已存在");
        }
        container.setCreateBy(getUsername());
        if (StringUtils.isEmpty(container.getStatus()))
        {
            container.setStatus("idle");
        }
        return toAjax(containerService.insertContainer(container));
    }

    @PreAuthorize("@ss.hasPermi('trace:container:edit')")
    @Log(title = "容器管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceContainer container)
    {
        if (!containerService.checkContainerCodeUnique(container))
        {
            return error("修改容器'" + container.getContainerCode() + "'失败，编码已存在");
        }
        container.setUpdateBy(getUsername());
        return toAjax(containerService.updateContainer(container));
    }

    @PreAuthorize("@ss.hasPermi('trace:container:remove')")
    @Log(title = "容器管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{containerIds}")
    public AjaxResult remove(@PathVariable Long[] containerIds)
    {
        for (Long containerId : containerIds)
        {
            if (containerService.hasUsageByContainerId(containerId) || containerService.hasBottlingByContainerId(containerId))
            {
                return warn("该容器已关联流转或灌装记录，不允许删除");
            }
        }
        return toAjax(containerService.deleteContainerByIds(containerIds));
    }
}
