package com.wine.system.service.impl;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceProduct;
import com.wine.system.domain.TraceProductVariety;
import com.wine.system.mapper.TraceProductMapper;
import com.wine.system.service.ITraceProductService;

@Service
public class TraceProductServiceImpl implements ITraceProductService
{
    @Autowired
    private TraceProductMapper productMapper;

    @Override
    public TraceProduct selectProductById(Long productId)
    {
        TraceProduct product = productMapper.selectProductById(productId);
        if (StringUtils.isNotNull(product))
        {
            product.setVarietyList(productMapper.selectProductVarietyList(productId));
        }
        return product;
    }

    @Override
    public List<TraceProduct> selectProductList(TraceProduct product)
    {
        return productMapper.selectProductList(product);
    }

    @Override
    public List<TraceProduct> selectProductOptions(Long enterpriseId)
    {
        return productMapper.selectProductOptions(enterpriseId);
    }

    @Override
    public boolean checkProductNameUnique(TraceProduct product)
    {
        Long productId = StringUtils.isNull(product.getProductId()) ? -1L : product.getProductId();
        TraceProduct info = productMapper.checkProductNameUnique(product.getEnterpriseId(), product.getProductName());
        if (StringUtils.isNotNull(info) && info.getProductId().longValue() != productId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasBatchByProductId(Long productId)
    {
        return productMapper.hasBatchByProductId(productId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertProduct(TraceProduct product)
    {
        int rows = productMapper.insertProduct(product);
        insertProductVarieties(product);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProduct(TraceProduct product)
    {
        productMapper.deleteProductVarietiesByProductId(product.getProductId());
        insertProductVarieties(product);
        return productMapper.updateProduct(product);
    }

    private void insertProductVarieties(TraceProduct product)
    {
        List<TraceProductVariety> list = product.getVarietyList();
        if (list == null)
        {
            list = Collections.emptyList();
        }
        for (TraceProductVariety item : list)
        {
            item.setProductId(product.getProductId());
        }
        if (!list.isEmpty())
        {
            productMapper.insertProductVarieties(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProductByIds(Long[] productIds)
    {
        productMapper.deleteProductVarietiesByProductIds(productIds);
        return productMapper.deleteProductByIds(productIds);
    }
}
