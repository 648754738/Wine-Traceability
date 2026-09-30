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
import com.wine.system.domain.TraceProduct;
import com.wine.system.service.ITraceProductService;

@RestController
@RequestMapping("/trace/product")
public class TraceProductController extends BaseController
{
    @Autowired
    private ITraceProductService productService;

    @PreAuthorize("@ss.hasPermi('trace:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(TraceProduct product)
    {
        startPage();
        List<TraceProduct> list = productService.selectProductList(product);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('trace:product:list')")
    @GetMapping("/options")
    public AjaxResult options(@RequestParam(required = false) Long enterpriseId)
    {
        return success(productService.selectProductOptions(enterpriseId));
    }

    @PreAuthorize("@ss.hasPermi('trace:product:query')")
    @GetMapping("/{productId}")
    public AjaxResult getInfo(@PathVariable Long productId)
    {
        return success(productService.selectProductById(productId));
    }

    @PreAuthorize("@ss.hasPermi('trace:product:add')")
    @Log(title = "产品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TraceProduct product)
    {
        if (!productService.checkProductNameUnique(product))
        {
            return error("新增产品'" + product.getProductName() + "'失败，名称已存在");
        }
        product.setCreateBy(getUsername());
        if (StringUtils.isEmpty(product.getStatus()))
        {
            product.setStatus(UserConstants.NORMAL);
        }
        return toAjax(productService.insertProduct(product));
    }

    @PreAuthorize("@ss.hasPermi('trace:product:edit')")
    @Log(title = "产品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TraceProduct product)
    {
        if (!productService.checkProductNameUnique(product))
        {
            return error("修改产品'" + product.getProductName() + "'失败，名称已存在");
        }
        product.setUpdateBy(getUsername());
        return toAjax(productService.updateProduct(product));
    }

    @PreAuthorize("@ss.hasPermi('trace:product:remove')")
    @Log(title = "产品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        for (Long productId : productIds)
        {
            if (productService.hasBatchByProductId(productId))
            {
                return warn("该产品已关联生产批次，不允许删除");
            }
        }
        return toAjax(productService.deleteProductByIds(productIds));
    }
}
