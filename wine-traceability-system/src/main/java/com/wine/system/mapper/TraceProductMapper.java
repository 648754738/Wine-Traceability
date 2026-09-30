package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.TraceProduct;
import com.wine.system.domain.TraceProductVariety;

public interface TraceProductMapper
{
    TraceProduct selectProductById(Long productId);

    List<TraceProduct> selectProductList(TraceProduct product);

    List<TraceProduct> selectProductOptions(@Param("enterpriseId") Long enterpriseId);

    TraceProduct checkProductNameUnique(@Param("enterpriseId") Long enterpriseId, @Param("productName") String productName);

    int insertProduct(TraceProduct product);

    int updateProduct(TraceProduct product);

    int deleteProductByIds(Long[] productIds);

    List<TraceProductVariety> selectProductVarietyList(Long productId);

    int insertProductVarieties(@Param("list") List<TraceProductVariety> list);

    int deleteProductVarietiesByProductIds(Long[] productIds);

    int deleteProductVarietiesByProductId(Long productId);

    int hasProductByEnterpriseId(Long enterpriseId);

    int hasBatchByProductId(Long productId);
}
