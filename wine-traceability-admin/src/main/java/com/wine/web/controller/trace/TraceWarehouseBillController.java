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
import com.wine.system.domain.TraceWarehouseBill;
import com.wine.system.service.ITraceWarehouseBillService;

@RestController
@RequestMapping("/trace/warehouse")
public class TraceWarehouseBillController extends BaseController
{
    @Autowired
    private ITraceWarehouseBillService warehouseBillService;

    @PreAuthorize("@ss.hasPermi('trace:warehouse:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceWarehouseBill bill)
    {
        startPage();
        List<TraceWarehouseBill> list = warehouseBillService.selectWarehouseBillList(bill);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:warehouse:list')")
    @GetMapping("/options")
    public AjaxResult options(@RequestParam(required = false) String billType)
    {
        return success(warehouseBillService.selectWarehouseBillOptions(billType));
    }

    @PreAuthorize("@ss.hasPermi('trace:warehouse:query')")
    @GetMapping("/{billId}")
    public AjaxResult getInfo(@PathVariable Long billId)
    {
        return success(warehouseBillService.selectWarehouseBillById(billId));
    }

    @PreAuthorize("@ss.hasPermi('trace:warehouse:add')")
    @Log(title = "仓储单据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceWarehouseBill bill)
    {
        if (!warehouseBillService.checkBillNoUnique(bill))
        {
            return error("新增单据失败，单据编号已存在");
        }
        if (bill.getDetailList() == null || bill.getDetailList().isEmpty())
        {
            return error("请至少维护一条单据明细");
        }
        bill.setCreateBy(getUsername());
        return toAjax(warehouseBillService.insertWarehouseBill(bill));
    }

    @PreAuthorize("@ss.hasPermi('trace:warehouse:edit')")
    @Log(title = "仓储单据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceWarehouseBill bill)
    {
        if (!warehouseBillService.checkBillNoUnique(bill))
        {
            return error("修改单据失败，单据编号已存在");
        }
        if (bill.getDetailList() == null || bill.getDetailList().isEmpty())
        {
            return error("请至少维护一条单据明细");
        }
        bill.setUpdateBy(getUsername());
        return toAjax(warehouseBillService.updateWarehouseBill(bill));
    }

    @PreAuthorize("@ss.hasPermi('trace:warehouse:remove')")
    @Log(title = "仓储单据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{billIds}")
    public AjaxResult remove(@PathVariable Long[] billIds)
    {
        for (Long billId : billIds)
        {
            if (warehouseBillService.hasLogisticsByBillId(billId))
            {
                return warn("该单据已关联物流信息，不允许删除");
            }
        }
        return toAjax(warehouseBillService.deleteWarehouseBillByIds(billIds));
    }
}