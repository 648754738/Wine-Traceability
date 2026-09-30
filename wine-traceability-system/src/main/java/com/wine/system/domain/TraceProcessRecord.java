package com.wine.system.domain;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wine.common.core.domain.BaseEntity;

public class TraceProcessRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long processId;
    private Long batchId;
    private String batchNo;
    private String processType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String tempControl;
    private String operatorName;
    private String remarks;
    private String delFlag;

    public Long getProcessId() { return processId; }
    public void setProcessId(Long processId) { this.processId = processId; }

    @NotNull(message = "生产批次不能为空")
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    @NotBlank(message = "工序类型不能为空")
    @Size(max = 50, message = "工序类型长度不能超过50个字符")
    public String getProcessType() { return processType; }
    public void setProcessType(String processType) { this.processType = processType; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    @Size(max = 100, message = "温控范围长度不能超过100个字符")
    public String getTempControl() { return tempControl; }
    public void setTempControl(String tempControl) { this.tempControl = tempControl; }

    @Size(max = 50, message = "操作人长度不能超过50个字符")
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    @Size(max = 500, message = "工序说明长度不能超过500个字符")
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("processId", getProcessId())
            .append("batchId", getBatchId())
            .append("processType", getProcessType())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("tempControl", getTempControl())
            .append("operatorName", getOperatorName())
            .append("remarks", getRemarks())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
