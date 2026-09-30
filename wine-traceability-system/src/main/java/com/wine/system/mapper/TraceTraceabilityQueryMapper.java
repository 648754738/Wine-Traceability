package com.wine.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.wine.system.domain.vo.TraceTraceabilityInfoVo;
import com.wine.system.domain.vo.TraceWarehouseEventVo;

public interface TraceTraceabilityQueryMapper
{
    TraceTraceabilityInfoVo selectTraceabilityBase(@Param("traceCodePrefix") String traceCodePrefix,
        @Param("traceCodeSerial") Long traceCodeSerial);

    List<TraceWarehouseEventVo> selectWarehouseEventsByBottlingId(Long bottlingId);
}