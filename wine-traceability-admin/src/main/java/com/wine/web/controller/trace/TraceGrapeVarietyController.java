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
import com.wine.common.constant.UserConstants;
import com.wine.common.core.controller.BaseController;
import com.wine.common.core.domain.AjaxResult;
import com.wine.common.core.page.TableDataInfo;
import com.wine.common.enums.BusinessType;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceGrapeVariety;
import com.wine.system.service.ITraceGrapeVarietyService;

@RestController
@RequestMapping("/trace/variety")
public class TraceGrapeVarietyController extends BaseController
{
    @Autowired
    private ITraceGrapeVarietyService varietyService;

    @PreAuthorize("@ss.hasPermi('trace:variety:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceGrapeVariety variety)
    {
        startPage();
        List<TraceGrapeVariety> list = varietyService.selectVarietyList(variety);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:variety:list')")
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(varietyService.selectVarietyOptions());
    }

    @PreAuthorize("@ss.hasPermi('trace:variety:query')")
    @GetMapping("/{varietyId}")
    public AjaxResult getInfo(@PathVariable Long varietyId)
    {
        return success(varietyService.selectVarietyById(varietyId));
    }

    @PreAuthorize("@ss.hasPermi('trace:variety:add')")
    @Log(title = "葡萄品种", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceGrapeVariety variety)
    {
        if (!varietyService.checkVarietyNameUnique(variety))
        {
            return error("新增品种'" + variety.getVarietyName() + "'失败，名称已存在");
        }
        variety.setCreateBy(getUsername());
        if (StringUtils.isEmpty(variety.getStatus()))
        {
            variety.setStatus(UserConstants.NORMAL);
        }
        return toAjax(varietyService.insertVariety(variety));
    }

    @PreAuthorize("@ss.hasPermi('trace:variety:edit')")
    @Log(title = "葡萄品种", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceGrapeVariety variety)
    {
        if (!varietyService.checkVarietyNameUnique(variety))
        {
            return error("修改品种'" + variety.getVarietyName() + "'失败，名称已存在");
        }
        variety.setUpdateBy(getUsername());
        return toAjax(varietyService.updateVariety(variety));
    }

    @PreAuthorize("@ss.hasPermi('trace:variety:remove')")
    @Log(title = "葡萄品种", businessType = BusinessType.DELETE)
    @DeleteMapping("/{varietyIds}")
    public AjaxResult remove(@PathVariable Long[] varietyIds)
    {
        for (Long varietyId : varietyIds)
        {
            if (varietyService.checkVarietyUsed(varietyId))
            {
                return warn("存在被产品引用的品种，不允许删除");
            }
        }
        return toAjax(varietyService.deleteVarietyByIds(varietyIds));
    }
}
