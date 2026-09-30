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
import com.wine.system.domain.TraceEnterprise;
import com.wine.system.service.ITraceEnterpriseService;

@RestController
@RequestMapping("/trace/enterprise")
public class TraceEnterpriseController extends BaseController
{
    @Autowired
    private ITraceEnterpriseService enterpriseService;

    @PreAuthorize("@ss.hasPermi('trace:enterprise:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceEnterprise enterprise)
    {
        startPage();
        List<TraceEnterprise> list = enterpriseService.selectEnterpriseList(enterprise);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:enterprise:list')")
    @GetMapping("/options")
    public AjaxResult options()
    {
        return success(enterpriseService.selectEnterpriseOptions());
    }

    @PreAuthorize("@ss.hasPermi('trace:enterprise:query')")
    @GetMapping("/{enterpriseId}")
    public AjaxResult getInfo(@PathVariable Long enterpriseId)
    {
        return success(enterpriseService.selectEnterpriseById(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('trace:enterprise:add')")
    @Log(title = "酒庄管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceEnterprise enterprise)
    {
        if (!enterpriseService.checkEnterpriseNameUnique(enterprise))
        {
            return error("新增酒庄'" + enterprise.getEnterpriseName() + "'失败，名称已存在");
        }
        if (!enterpriseService.checkLicenseNoUnique(enterprise))
        {
            return error("新增酒庄'" + enterprise.getEnterpriseName() + "'失败，营业执照号已存在");
        }
        enterprise.setCreateBy(getUsername());
        if (StringUtils.isEmpty(enterprise.getStatus()))
        {
            enterprise.setStatus(UserConstants.NORMAL);
        }
        return toAjax(enterpriseService.insertEnterprise(enterprise));
    }

    @PreAuthorize("@ss.hasPermi('trace:enterprise:edit')")
    @Log(title = "酒庄管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceEnterprise enterprise)
    {
        if (!enterpriseService.checkEnterpriseNameUnique(enterprise))
        {
            return error("修改酒庄'" + enterprise.getEnterpriseName() + "'失败，名称已存在");
        }
        if (!enterpriseService.checkLicenseNoUnique(enterprise))
        {
            return error("修改酒庄'" + enterprise.getEnterpriseName() + "'失败，营业执照号已存在");
        }
        enterprise.setUpdateBy(getUsername());
        return toAjax(enterpriseService.updateEnterprise(enterprise));
    }

    @PreAuthorize("@ss.hasPermi('trace:enterprise:remove')")
    @Log(title = "酒庄管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{enterpriseIds}")
    public AjaxResult remove(@PathVariable Long[] enterpriseIds)
    {
        for (Long enterpriseId : enterpriseIds)
        {
            if (enterpriseService.hasPlotByEnterpriseId(enterpriseId)
                || enterpriseService.hasProductByEnterpriseId(enterpriseId)
                || enterpriseService.hasWorkshopByEnterpriseId(enterpriseId)
                || enterpriseService.hasContainerByEnterpriseId(enterpriseId))
            {
                return warn("该酒庄存在关联的地块、产品、车间或容器数据，不允许删除");
            }
        }
        return toAjax(enterpriseService.deleteEnterpriseByIds(enterpriseIds));
    }
}
