package com.wine.system.mapper;

import java.util.List;
import com.wine.system.domain.TraceProcessRecord;

public interface TraceProcessRecordMapper
{
    TraceProcessRecord selectProcessRecordById(Long processId);

    List<TraceProcessRecord> selectProcessRecordList(TraceProcessRecord processRecord);

    int hasProcessByBatchId(Long batchId);

    int insertProcessRecord(TraceProcessRecord processRecord);

    int updateProcessRecord(TraceProcessRecord processRecord);

    int deleteProcessRecordByIds(Long[] processIds);
}
