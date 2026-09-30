package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceProcessRecord;

public interface ITraceProcessRecordService
{
    TraceProcessRecord selectProcessRecordById(Long processId);

    List<TraceProcessRecord> selectProcessRecordList(TraceProcessRecord processRecord);

    int insertProcessRecord(TraceProcessRecord processRecord);

    int updateProcessRecord(TraceProcessRecord processRecord);

    int deleteProcessRecordByIds(Long[] processIds);
}
