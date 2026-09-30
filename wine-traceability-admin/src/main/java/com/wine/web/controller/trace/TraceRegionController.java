package com.wine.web.controller.trace;

import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
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
import com.wine.common.constant.UserConstants;
import com.wine.common.core.controller.BaseController;
import com.wine.common.core.domain.AjaxResult;
import com.wine.common.enums.BusinessType;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceRegion;
import com.wine.system.service.ITraceRegionService;

@RestController
@RequestMapping("/trace/region")
public class TraceRegionController extends BaseController
{
    @Autowired
    private ITraceRegionService regionService;

    @PreAuthorize("@ss.hasPermi('trace:region:list')")
    @GetMapping("/list")
    public AjaxResult list(TraceRegion region)
    {
        List<TraceRegion> regions = regionService.selectRegionList(region);
        return success(regionService.buildRegionTree(regions));
    }

    @PreAuthorize("@ss.hasPermi('trace:region:list')")
    @GetMapping("/list/exclude/{regionId}")
    public AjaxResult excludeChild(@PathVariable Long regionId)
    {
        List<TraceRegion> regions = regionService.selectRegionList(new TraceRegion());
        regions.removeIf(r -> r.getRegionId().intValue() == regionId
            || ArrayUtils.contains(StringUtils.split(r.getAncestors(), ","), regionId + ""));
        return success(regionService.buildRegionTree(regions));
    }

    @PreAuthorize("@ss.hasPermi('trace:region:query')")
    @GetMapping("/{regionId}")
    public AjaxResult getInfo(@PathVariable Long regionId)
    {
        return success(regionService.selectRegionById(regionId));
    }

    @PreAuthorize("@ss.hasPermi('trace:region:add')")
    @Log(title = "产区管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceRegion region)
    {
        if (!regionService.checkRegionNameUnique(region))
        {
            return error("新增产区'" + region.getRegionName() + "'失败，名称已存在");
        }
        region.setCreateBy(getUsername());
        if (StringUtils.isEmpty(region.getStatus()))
        {
            region.setStatus(UserConstants.NORMAL);
        }
        return toAjax(regionService.insertRegion(region));
    }

    @PreAuthorize("@ss.hasPermi('trace:region:edit')")
    @Log(title = "产区管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceRegion region)
    {
        if (!regionService.checkRegionNameUnique(region))
        {
            return error("修改产区'" + region.getRegionName() + "'失败，名称已存在");
        }
        if (region.getRegionId().equals(region.getParentId()))
        {
            return error("上级产区不能是自己");
        }
        if (StringUtils.equals(UserConstants.DEPT_DISABLE, region.getStatus())
            && regionService.selectNormalChildrenRegionById(region.getRegionId()) > 0)
        {
            return error("该产区包含未停用的子产区，不能直接停用");
        }
        region.setUpdateBy(getUsername());
        return toAjax(regionService.updateRegion(region));
    }

    @PreAuthorize("@ss.hasPermi('trace:region:remove')")
    @Log(title = "产区管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{regionId}")
    public AjaxResult remove(@PathVariable Long regionId)
    {
        if (regionService.hasChildByRegionId(regionId))
        {
            return warn("存在下级产区，不允许删除");
        }
        if (regionService.checkRegionExistEnterprise(regionId))
        {
            return warn("该产区下存在酒庄，不允许删除");
        }
        return toAjax(regionService.deleteRegionById(regionId));
    }
}
