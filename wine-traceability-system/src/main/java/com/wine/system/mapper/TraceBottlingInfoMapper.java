package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.TraceBottlingInfo;

public interface TraceBottlingInfoMapper
{
    TraceBottlingInfo selectBottlingInfoById(Long bottlingId);

    List<TraceBottlingInfo> selectBottlingInfoList(TraceBottlingInfo bottlingInfo);

    List<TraceBottlingInfo> selectBottlingOptions(Long batchId);

    TraceBottlingInfo checkTraceCodeRangeConflict(@Param("traceCodePrefix") String traceCodePrefix,
        @Param("traceCodeStart") Long traceCodeStart, @Param("traceCodeEnd") Long traceCodeEnd);

    int hasBottlingByBatchId(Long batchId);

    int hasBottlingByContainerId(Long containerId);

    int hasWarehouseDetailByBottlingId(Long bottlingId);

    int insertBottlingInfo(TraceBottlingInfo bottlingInfo);

    int updateBottlingInfo(TraceBottlingInfo bottlingInfo);

    int deleteBottlingInfoByIds(Long[] bottlingIds);
}