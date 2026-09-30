package com.wine.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.system.domain.TraceProcessRecord;
import com.wine.system.mapper.TraceProcessRecordMapper;
import com.wine.system.service.ITraceProcessRecordService;

@Service
public class TraceProcessRecordServiceImpl implements ITraceProcessRecordService
{
    @Autowired
    private TraceProcessRecordMapper processRecordMapper;

    @Override
    public TraceProcessRecord selectProcessRecordById(Long processId)
    {
        return processRecordMapper.selectProcessRecordById(processId);
    }

    @Override
    public List<TraceProcessRecord> selectProcessRecordList(TraceProcessRecord processRecord)
    {
        return processRecordMapper.selectProcessRecordList(processRecord);
    }

    @Override
    public int insertProcessRecord(TraceProcessRecord processRecord)
    {
        return processRecordMapper.insertProcessRecord(processRecord);
    }

    @Override
    public int updateProcessRecord(TraceProcessRecord processRecord)
    {
        return processRecordMapper.updateProcessRecord(processRecord);
    }

    @Override
    public int deleteProcessRecordByIds(Long[] processIds)
    {
        return processRecordMapper.deleteProcessRecordByIds(processIds);
    }
}
