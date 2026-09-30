package com.wine.system.service;

import com.wine.system.domain.vo.TraceTraceabilityInfoVo;

public interface ITraceTraceabilityQueryService
{
    TraceTraceabilityInfoVo queryByTraceCode(String traceCode, String querySource);
}