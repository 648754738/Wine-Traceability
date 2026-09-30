package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceBatch;

public interface ITraceBatchService
{
    TraceBatch selectBatchById(Long batchId);

    List<TraceBatch> selectBatchList(TraceBatch batch);

    List<TraceBatch> selectBatchOptions(Long enterpriseId);

    boolean checkBatchNoUnique(TraceBatch batch);

    boolean hasProcessByBatchId(Long batchId);

    boolean hasUsageByBatchId(Long batchId);

    boolean hasQualityByBatchId(Long batchId);

    boolean hasBottlingByBatchId(Long batchId);

    int insertBatch(TraceBatch batch);

    int updateBatch(TraceBatch batch);

    int deleteBatchByIds(Long[] batchIds);
}
