package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.TraceBatch;

public interface TraceBatchMapper
{
    TraceBatch selectBatchById(Long batchId);

    List<TraceBatch> selectBatchList(TraceBatch batch);

    List<TraceBatch> selectBatchOptions(@Param("enterpriseId") Long enterpriseId);

    TraceBatch checkBatchNoUnique(@Param("batchNo") String batchNo);

    int hasBatchByProductId(Long productId);

    int hasBatchByPlotId(Long plotId);

    int hasBatchByWorkshopId(Long workshopId);

    int insertBatch(TraceBatch batch);

    int updateBatch(TraceBatch batch);

    int deleteBatchByIds(Long[] batchIds);
}
