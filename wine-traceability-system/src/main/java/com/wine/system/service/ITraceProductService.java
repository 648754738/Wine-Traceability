package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceProduct;

public interface ITraceProductService
{
    TraceProduct selectProductById(Long productId);

    List<TraceProduct> selectProductList(TraceProduct product);

    List<TraceProduct> selectProductOptions(Long enterpriseId);

    boolean checkProductNameUnique(TraceProduct product);

    boolean hasBatchByProductId(Long productId);

    int insertProduct(TraceProduct product);

    int updateProduct(TraceProduct product);

    int deleteProductByIds(Long[] productIds);
}
