package com.wine.system.mapper;

import java.util.List;
import com.wine.system.domain.TraceFeedback;

public interface TraceFeedbackMapper
{
    TraceFeedback selectFeedbackById(Long feedbackId);

    List<TraceFeedback> selectFeedbackList(TraceFeedback feedback);

    int insertFeedback(TraceFeedback feedback);

    int updateFeedback(TraceFeedback feedback);

    int deleteFeedbackByIds(Long[] feedbackIds);
}