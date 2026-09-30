package com.wine.system.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.exception.ServiceException;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceFeedback;
import com.wine.system.domain.vo.TraceTraceabilityInfoVo;
import com.wine.system.mapper.TraceFeedbackMapper;
import com.wine.system.mapper.TraceTraceabilityQueryMapper;
import com.wine.system.service.ITraceFeedbackService;

@Service
public class TraceFeedbackServiceImpl implements ITraceFeedbackService
{
    private static final Pattern TRACE_CODE_PATTERN = Pattern.compile("^(.*?)(\\d+)$");

    @Autowired
    private TraceFeedbackMapper feedbackMapper;

    @Autowired
    private TraceTraceabilityQueryMapper traceabilityQueryMapper;

    @Override
    public TraceFeedback selectFeedbackById(Long feedbackId)
    {
        return feedbackMapper.selectFeedbackById(feedbackId);
    }

    @Override
    public List<TraceFeedback> selectFeedbackList(TraceFeedback feedback)
    {
        return feedbackMapper.selectFeedbackList(feedback);
    }

    @Override
    public int insertPublicFeedback(TraceFeedback feedback)
    {
        if (feedback == null)
        {
            throw new ServiceException("反馈内容不能为空");
        }
        if (StringUtils.isBlank(feedback.getTraceCode()))
        {
            throw new ServiceException("溯源码不能为空");
        }
        String traceCode = feedback.getTraceCode().trim();
        Matcher matcher = TRACE_CODE_PATTERN.matcher(traceCode);
        if (!matcher.matches() || StringUtils.isBlank(matcher.group(1)))
        {
            throw new ServiceException("溯源码格式不正确");
        }
        if (feedback.getRating() != null && (feedback.getRating() < 1 || feedback.getRating() > 5))
        {
            throw new ServiceException("评分必须在1到5之间");
        }
        TraceTraceabilityInfoVo traceInfo = traceabilityQueryMapper.selectTraceabilityBase(matcher.group(1), Long.valueOf(matcher.group(2)));
        if (StringUtils.isNull(traceInfo))
        {
            throw new ServiceException("未查询到对应的溯源信息");
        }
        feedback.setTraceCode(traceCode);
        feedback.setBottlingId(traceInfo.getBottlingId());
        if (StringUtils.isEmpty(feedback.getIsSuspicious()))
        {
            feedback.setIsSuspicious("0");
        }
        if (StringUtils.isEmpty(feedback.getStatus()))
        {
            feedback.setStatus("0");
        }
        if (StringUtils.isEmpty(feedback.getCreateBy()))
        {
            feedback.setCreateBy("消费者");
        }
        return feedbackMapper.insertFeedback(feedback);
    }

    @Override
    public int updateFeedback(TraceFeedback feedback)
    {
        if (feedback == null || feedback.getFeedbackId() == null)
        {
            throw new ServiceException("反馈记录不存在");
        }
        return feedbackMapper.updateFeedback(feedback);
    }

    @Override
    public int deleteFeedbackByIds(Long[] feedbackIds)
    {
        return feedbackMapper.deleteFeedbackByIds(feedbackIds);
    }
}