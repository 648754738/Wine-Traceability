package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceBottlingInfo;

public interface ITraceBottlingInfoService
{
    TraceBottlingInfo selectBottlingInfoById(Long bottlingId);

    List<TraceBottlingInfo> selectBottlingInfoList(TraceBottlingInfo bottlingInfo);

    List<TraceBottlingInfo> selectBottlingOptions(Long batchId);

    boolean checkTraceCodeRangeUnique(TraceBottlingInfo bottlingInfo);

    boolean hasWarehouseDetailByBottlingId(Long bottlingId);

    int insertBottlingInfo(TraceBottlingInfo bottlingInfo);

    int updateBottlingInfo(TraceBottlingInfo bottlingInfo);

    int deleteBottlingInfoByIds(Long[] bottlingIds);
}