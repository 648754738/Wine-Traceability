package com.wine.system.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TraceQueryLog
{
    private Long logId;
    private String traceCode;
    private Long bottlingId;
    private Long batchId;
    private String enterpriseName;
    private String productName;
    private String querySource;
    private String queryStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date queryTime;

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    public String getTraceCode() { return traceCode; }
    public void setTraceCode(String traceCode) { this.traceCode = traceCode; }
    public Long getBottlingId() { return bottlingId; }
    public void setBottlingId(Long bottlingId) { this.bottlingId = bottlingId; }
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }
    public String getEnterpriseName() { return enterpriseName; }
    public void setEnterpriseName(String enterpriseName) { this.enterpriseName = enterpriseName; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getQuerySource() { return querySource; }
    public void setQuerySource(String querySource) { this.querySource = querySource; }
    public String getQueryStatus() { return queryStatus; }
    public void setQueryStatus(String queryStatus) { this.queryStatus = queryStatus; }
    public Date getQueryTime() { return queryTime; }
    public void setQueryTime(Date queryTime) { this.queryTime = queryTime; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("traceCode", getTraceCode())
            .append("bottlingId", getBottlingId())
            .append("batchId", getBatchId())
            .append("enterpriseName", getEnterpriseName())
            .append("productName", getProductName())
            .append("querySource", getQuerySource())
            .append("queryStatus", getQueryStatus())
            .append("queryTime", getQueryTime())
            .toString();
    }
}