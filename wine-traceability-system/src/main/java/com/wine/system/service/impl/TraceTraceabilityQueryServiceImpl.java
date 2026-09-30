package com.wine.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.core.text.Convert;
import com.wine.common.exception.ServiceException;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceContainerUsage;
import com.wine.system.domain.TraceProcessRecord;
import com.wine.system.domain.TraceQueryLog;
import com.wine.system.domain.TraceRegion;
import com.wine.system.domain.vo.TraceTraceabilityInfoVo;
import com.wine.system.mapper.TraceQueryLogMapper;
import com.wine.system.mapper.TraceRegionMapper;
import com.wine.system.mapper.TraceTraceabilityQueryMapper;
import com.wine.system.service.ITraceContainerUsageService;
import com.wine.system.service.ITraceProcessRecordService;
import com.wine.system.service.ITraceTraceabilityQueryService;

@Service
public class TraceTraceabilityQueryServiceImpl implements ITraceTraceabilityQueryService
{
    private static final Pattern TRACE_CODE_PATTERN = Pattern.compile("^(.*?)(\\d+)$");

    @Autowired
    private TraceTraceabilityQueryMapper traceabilityQueryMapper;

    @Autowired
    private TraceQueryLogMapper queryLogMapper;

    @Autowired
    private ITraceProcessRecordService processRecordService;

    @Autowired
    private ITraceContainerUsageService containerUsageService;

    @Autowired
    private TraceRegionMapper regionMapper;

    @Override
    public TraceTraceabilityInfoVo queryByTraceCode(String traceCode, String querySource)
    {
        String normalizedTraceCode = traceCode == null ? null : traceCode.trim();
        try
        {
            if (StringUtils.isBlank(normalizedTraceCode))
            {
                throw new ServiceException("溯源码不能为空");
            }
            Matcher matcher = TRACE_CODE_PATTERN.matcher(normalizedTraceCode);
            if (!matcher.matches() || StringUtils.isBlank(matcher.group(1)))
            {
                throw new ServiceException("溯源码格式不正确");
            }

            String traceCodePrefix = matcher.group(1);
            Long traceCodeSerial = Long.valueOf(matcher.group(2));
            TraceTraceabilityInfoVo info = traceabilityQueryMapper.selectTraceabilityBase(traceCodePrefix, traceCodeSerial);
            if (StringUtils.isNull(info))
            {
                throw new ServiceException("未查询到对应的溯源信息");
            }

            info.setTraceCode(normalizedTraceCode);
            info.setRegionPath(buildRegionPath(info.getRegionAncestors(), info.getRegionId()));
            info.setProcessList(loadProcessList(info.getBatchId()));
            info.setUsageList(loadUsageList(info.getBatchId()));
            info.setWarehouseEvents(traceabilityQueryMapper.selectWarehouseEventsByBottlingId(info.getBottlingId()));
            insertQueryLog(normalizedTraceCode, querySource, "1", info);
            return info;
        }
        catch (RuntimeException ex)
        {
            insertQueryLog(normalizedTraceCode, querySource, "0", null);
            throw ex;
        }
    }

    private void insertQueryLog(String traceCode, String querySource, String queryStatus, TraceTraceabilityInfoVo info)
    {
        if (StringUtils.isBlank(traceCode))
        {
            return;
        }
        TraceQueryLog log = new TraceQueryLog();
        log.setTraceCode(traceCode);
        log.setQuerySource(StringUtils.isBlank(querySource) ? "PUBLIC" : querySource);
        log.setQueryStatus(queryStatus);
        log.setQueryTime(new Date());
        if (info != null)
        {
            log.setBottlingId(info.getBottlingId());
            log.setBatchId(info.getBatchId());
            log.setEnterpriseName(info.getEnterpriseName());
            log.setProductName(info.getProductName());
        }
        queryLogMapper.insertQueryLog(log);
    }

    private List<TraceProcessRecord> loadProcessList(Long batchId)
    {
        TraceProcessRecord query = new TraceProcessRecord();
        query.setBatchId(batchId);
        List<TraceProcessRecord> list = processRecordService.selectProcessRecordList(query);
        list.sort((left, right) -> {
            if (left.getStartTime() == null && right.getStartTime() == null)
            {
                return 0;
            }
            if (left.getStartTime() == null)
            {
                return 1;
            }
            if (right.getStartTime() == null)
            {
                return -1;
            }
            return left.getStartTime().compareTo(right.getStartTime());
        });
        return list;
    }

    private List<TraceContainerUsage> loadUsageList(Long batchId)
    {
        TraceContainerUsage query = new TraceContainerUsage();
        query.setBatchId(batchId);
        List<TraceContainerUsage> list = containerUsageService.selectContainerUsageList(query);
        list.sort((left, right) -> {
            if (left.getStartTime() == null && right.getStartTime() == null)
            {
                return 0;
            }
            if (left.getStartTime() == null)
            {
                return 1;
            }
            if (right.getStartTime() == null)
            {
                return -1;
            }
            return left.getStartTime().compareTo(right.getStartTime());
        });
        return list;
    }

    private String buildRegionPath(String ancestors, Long regionId)
    {
        List<Long> regionIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(ancestors))
        {
            regionIds.addAll(Arrays.stream(Convert.toLongArray(ancestors))
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toList()));
        }
        if (regionId != null && regionId > 0)
        {
            regionIds.add(regionId);
        }
        if (regionIds.isEmpty())
        {
            return null;
        }

        List<TraceRegion> regions = regionMapper.selectRegionByIds(regionIds.toArray(new Long[0]));
        Map<Long, String> regionNameMap = new HashMap<>();
        for (TraceRegion region : regions)
        {
            regionNameMap.put(region.getRegionId(), region.getRegionName());
        }
        return regionIds.stream()
            .distinct()
            .map(regionNameMap::get)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.joining(" / "));
    }
}