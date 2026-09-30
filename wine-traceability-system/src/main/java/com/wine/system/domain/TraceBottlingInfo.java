package com.wine.system.domain;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wine.common.core.domain.BaseEntity;

public class TraceBottlingInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long bottlingId;
    private Long batchId;
    private String batchNo;
    private Long sourceContainerId;
    private String sourceContainerCode;
    private String traceCodePrefix;
    private Long traceCodeStart;
    private Long traceCodeEnd;
    private Long bottleCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bottlingDate;
    private String bottleSpec;
    private String capCode;
    private String qrCodeUrl;
    private String remarks;
    private String delFlag;

    public Long getBottlingId() { return bottlingId; }
    public void setBottlingId(Long bottlingId) { this.bottlingId = bottlingId; }

    @NotNull(message = "生产批次不能为空")
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public Long getSourceContainerId() { return sourceContainerId; }
    public void setSourceContainerId(Long sourceContainerId) { this.sourceContainerId = sourceContainerId; }

    public String getSourceContainerCode() { return sourceContainerCode; }
    public void setSourceContainerCode(String sourceContainerCode) { this.sourceContainerCode = sourceContainerCode; }

    @NotBlank(message = "溯源码前缀不能为空")
    @Size(max = 32, message = "溯源码前缀长度不能超过32个字符")
    public String getTraceCodePrefix() { return traceCodePrefix; }
    public void setTraceCodePrefix(String traceCodePrefix) { this.traceCodePrefix = traceCodePrefix; }

    @NotNull(message = "起始序号不能为空")
    public Long getTraceCodeStart() { return traceCodeStart; }
    public void setTraceCodeStart(Long traceCodeStart) { this.traceCodeStart = traceCodeStart; }

    @NotNull(message = "结束序号不能为空")
    public Long getTraceCodeEnd() { return traceCodeEnd; }
    public void setTraceCodeEnd(Long traceCodeEnd) { this.traceCodeEnd = traceCodeEnd; }

    public Long getBottleCount() { return bottleCount; }
    public void setBottleCount(Long bottleCount) { this.bottleCount = bottleCount; }

    public Date getBottlingDate() { return bottlingDate; }
    public void setBottlingDate(Date bottlingDate) { this.bottlingDate = bottlingDate; }

    @Size(max = 100, message = "瓶型规格长度不能超过100个字符")
    public String getBottleSpec() { return bottleSpec; }
    public void setBottleSpec(String bottleSpec) { this.bottleSpec = bottleSpec; }

    @Size(max = 100, message = "瓶盖码长度不能超过100个字符")
    public String getCapCode() { return capCode; }
    public void setCapCode(String capCode) { this.capCode = capCode; }

    @Size(max = 255, message = "二维码地址长度不能超过255个字符")
    public String getQrCodeUrl() { return qrCodeUrl; }
    public void setQrCodeUrl(String qrCodeUrl) { this.qrCodeUrl = qrCodeUrl; }

    @Size(max = 500, message = "备注长度不能超过500个字符")
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("bottlingId", getBottlingId())
            .append("batchId", getBatchId())
            .append("sourceContainerId", getSourceContainerId())
            .append("traceCodePrefix", getTraceCodePrefix())
            .append("traceCodeStart", getTraceCodeStart())
            .append("traceCodeEnd", getTraceCodeEnd())
            .append("bottleCount", getBottleCount())
            .append("bottlingDate", getBottlingDate())
            .append("bottleSpec", getBottleSpec())
            .append("capCode", getCapCode())
            .append("qrCodeUrl", getQrCodeUrl())
            .append("remarks", getRemarks())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}