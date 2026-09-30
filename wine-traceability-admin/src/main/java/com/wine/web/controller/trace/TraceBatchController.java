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
import com.wine.system.domain.TraceBatch;
import com.wine.system.service.ITraceBatchService;

@RestController
@RequestMapping("/trace/batch")
public class TraceBatchController extends BaseController
{
    @Autowired
    private ITraceBatchService batchService;

    @PreAuthorize("@ss.hasPermi('trace:batch:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceBatch batch)
    {
        startPage();
        List<TraceBatch> list = batchService.selectBatchList(batch);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:batch:list')")
    @GetMapping("/options")
    public AjaxResult options(@RequestParam(required = false) Long enterpriseId)
    {
        return success(batchService.selectBatchOptions(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('trace:batch:query')")
    @GetMapping("/{batchId}")
    public AjaxResult getInfo(@PathVariable Long batchId)
    {
        return success(batchService.selectBatchById(batchId));
    }

    @PreAuthorize("@ss.hasPermi('trace:batch:add')")
    @Log(title = "生产批次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceBatch batch)
    {
        if (!batchService.checkBatchNoUnique(batch))
        {
            return error("新增批次'" + batch.getBatchNo() + "'失败，批次号已存在");
        }
        batch.setCreateBy(getUsername());
        if (StringUtils.isEmpty(batch.getStatus()))
        {
            batch.setStatus(UserConstants.NORMAL);
        }
        return toAjax(batchService.insertBatch(batch));
    }

    @PreAuthorize("@ss.hasPermi('trace:batch:edit')")
    @Log(title = "生产批次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceBatch batch)
    {
        if (!batchService.checkBatchNoUnique(batch))
        {
            return error("修改批次'" + batch.getBatchNo() + "'失败，批次号已存在");
        }
        batch.setUpdateBy(getUsername());
        return toAjax(batchService.updateBatch(batch));
    }

    @PreAuthorize("@ss.hasPermi('trace:batch:remove')")
    @Log(title = "生产批次", businessType = BusinessType.DELETE)
    @DeleteMapping("/{batchIds}")
    public AjaxResult remove(@PathVariable Long[] batchIds)
    {
        for (Long batchId : batchIds)
        {
            if (batchService.hasProcessByBatchId(batchId)
                || batchService.hasUsageByBatchId(batchId)
                || batchService.hasQualityByBatchId(batchId)
                || batchService.hasBottlingByBatchId(batchId))
            {
                return warn("该批次存在关联的工序、流转、质检或灌装记录，不允许删除");
            }
        }
        return toAjax(batchService.deleteBatchByIds(batchIds));
    }
}
