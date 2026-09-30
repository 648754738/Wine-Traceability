package com.wine.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wine.common.core.domain.BaseEntity;

public class TraceContainerUsage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long usageId;
    private Long containerId;
    private String containerCode;
    private String containerType;
    private Long batchId;
    private String batchNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private BigDecimal fillQuantityL;
    private String remarks;
    private String delFlag;

    public Long getUsageId() { return usageId; }
    public void setUsageId(Long usageId) { this.usageId = usageId; }

    @NotNull(message = "容器不能为空")
    public Long getContainerId() { return containerId; }
    public void setContainerId(Long containerId) { this.containerId = containerId; }

    public String getContainerCode() { return containerCode; }
    public void setContainerCode(String containerCode) { this.containerCode = containerCode; }

    public String getContainerType() { return containerType; }
    public void setContainerType(String containerType) { this.containerType = containerType; }

    @NotNull(message = "生产批次不能为空")
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public BigDecimal getFillQuantityL() { return fillQuantityL; }
    public void setFillQuantityL(BigDecimal fillQuantityL) { this.fillQuantityL = fillQuantityL; }

    @Size(max = 500, message = "备注长度不能超过500个字符")
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("usageId", getUsageId())
            .append("containerId", getContainerId())
            .append("batchId", getBatchId())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("fillQuantityL", getFillQuantityL())
            .append("remarks", getRemarks())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
