package com.wine.system.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wine.common.core.domain.BaseEntity;

public class TraceFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long feedbackId;
    private String traceCode;
    private Long bottlingId;
    private String batchNo;
    private String enterpriseName;
    private String productName;
    private Integer rating;
    private String content;
    private String isSuspicious;
    private String status;
    private String replyContent;
    private String delFlag;

    public Long getFeedbackId()
    {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId)
    {
        this.feedbackId = feedbackId;
    }

    @NotBlank(message = "Trace code cannot be empty")
    @Size(max = 64, message = "Trace code length cannot exceed 64 characters")
    public String getTraceCode()
    {
        return traceCode;
    }

    public void setTraceCode(String traceCode)
    {
        this.traceCode = traceCode;
    }

    public Long getBottlingId()
    {
        return bottlingId;
    }

    public void setBottlingId(Long bottlingId)
    {
        this.bottlingId = bottlingId;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public String getEnterpriseName()
    {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName)
    {
        this.enterpriseName = enterpriseName;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    @Min(value = 1, message = "Rating cannot be lower than 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    public Integer getRating()
    {
        return rating;
    }

    public void setRating(Integer rating)
    {
        this.rating = rating;
    }

    @Size(max = 500, message = "Feedback content cannot exceed 500 characters")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getIsSuspicious()
    {
        return isSuspicious;
    }

    public void setIsSuspicious(String isSuspicious)
    {
        this.isSuspicious = isSuspicious;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Size(max = 500, message = "Reply content cannot exceed 500 characters")
    public String getReplyContent()
    {
        return replyContent;
    }

    public void setReplyContent(String replyContent)
    {
        this.replyContent = replyContent;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("feedbackId", getFeedbackId())
            .append("traceCode", getTraceCode())
            .append("bottlingId", getBottlingId())
            .append("batchNo", getBatchNo())
            .append("enterpriseName", getEnterpriseName())
            .append("productName", getProductName())
            .append("rating", getRating())
            .append("content", getContent())
            .append("isSuspicious", getIsSuspicious())
            .append("status", getStatus())
            .append("replyContent", getReplyContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}