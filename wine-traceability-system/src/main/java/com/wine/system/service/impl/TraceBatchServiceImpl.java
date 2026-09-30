package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceBatch;
import com.wine.system.mapper.TraceBottlingInfoMapper;
import com.wine.system.mapper.TraceBatchMapper;
import com.wine.system.mapper.TraceQualityReportMapper;
import com.wine.system.mapper.TraceContainerUsageMapper;
import com.wine.system.mapper.TraceProcessRecordMapper;
import com.wine.system.service.ITraceBatchService;

@Service
public class TraceBatchServiceImpl implements ITraceBatchService
{
    @Autowired
    private TraceBatchMapper batchMapper;

    @Autowired
    private TraceProcessRecordMapper processRecordMapper;

    @Autowired
    private TraceContainerUsageMapper containerUsageMapper;

    @Autowired
    private TraceQualityReportMapper qualityReportMapper;

    @Autowired
    private TraceBottlingInfoMapper bottlingInfoMapper;

    @Override
    public TraceBatch selectBatchById(Long batchId)
    {
        return batchMapper.selectBatchById(batchId);
    }

    @Override
    public List<TraceBatch> selectBatchList(TraceBatch batch)
    {
        return batchMapper.selectBatchList(batch);
    }

    @Override
    public List<TraceBatch> selectBatchOptions(Long enterpriseId)
    {
        return batchMapper.selectBatchOptions(enterpriseId);
    }

    @Override
    public boolean checkBatchNoUnique(TraceBatch batch)
    {
        Long batchId = StringUtils.isNull(batch.getBatchId()) ? -1L : batch.getBatchId();
        TraceBatch info = batchMapper.checkBatchNoUnique(batch.getBatchNo());
        if (StringUtils.isNotNull(info) && info.getBatchId().longValue() != batchId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasProcessByBatchId(Long batchId)
    {
        return processRecordMapper.hasProcessByBatchId(batchId) > 0;
    }

    @Override
    public boolean hasUsageByBatchId(Long batchId)
    {
        return containerUsageMapper.hasUsageByBatchId(batchId) > 0;
    }

    @Override
    public boolean hasQualityByBatchId(Long batchId)
    {
        return qualityReportMapper.hasQualityByBatchId(batchId) > 0;
    }

    @Override
    public boolean hasBottlingByBatchId(Long batchId)
    {
        return bottlingInfoMapper.hasBottlingByBatchId(batchId) > 0;
    }

    @Override
    public int insertBatch(TraceBatch batch)
    {
        return batchMapper.insertBatch(batch);
    }

    @Override
    public int updateBatch(TraceBatch batch)
    {
        return batchMapper.updateBatch(batch);
    }

    @Override
    public int deleteBatchByIds(Long[] batchIds)
    {
        return batchMapper.deleteBatchByIds(batchIds);
    }
}
