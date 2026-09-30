package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceFeedback;

public interface ITraceFeedbackService
{
    TraceFeedback selectFeedbackById(Long feedbackId);

    List<TraceFeedback> selectFeedbackList(TraceFeedback feedback);

    int insertPublicFeedback(TraceFeedback feedback);

    int updateFeedback(TraceFeedback feedback);

    int deleteFeedbackByIds(Long[] feedbackIds);
}