package com.wine.system.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wine.common.core.domain.BaseEntity;

public class TraceWarehouseBillDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long detailId;
    private Long billId;
    private Long bottlingId;
    private String batchNo;
    private String traceCodePrefix;
    private Long traceCodeStart;
    private Long traceCodeEnd;
    private Long bottleCount;
    private Integer quantity;
    private String startTraceCode;
    private String endTraceCode;
    private String delFlag;

    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }

    @NotNull(message = "灌装记录不能为空")
    public Long getBottlingId() { return bottlingId; }
    public void setBottlingId(Long bottlingId) { this.bottlingId = bottlingId; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public String getTraceCodePrefix() { return traceCodePrefix; }
    public void setTraceCodePrefix(String traceCodePrefix) { this.traceCodePrefix = traceCodePrefix; }

    public Long getTraceCodeStart() { return traceCodeStart; }
    public void setTraceCodeStart(Long traceCodeStart) { this.traceCodeStart = traceCodeStart; }

    public Long getTraceCodeEnd() { return traceCodeEnd; }
    public void setTraceCodeEnd(Long traceCodeEnd) { this.traceCodeEnd = traceCodeEnd; }

    public Long getBottleCount() { return bottleCount; }
    public void setBottleCount(Long bottleCount) { this.bottleCount = bottleCount; }

    @NotNull(message = "数量不能为空")
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    @Size(max = 64, message = "起始溯源码长度不能超过64个字符")
    public String getStartTraceCode() { return startTraceCode; }
    public void setStartTraceCode(String startTraceCode) { this.startTraceCode = startTraceCode; }

    @Size(max = 64, message = "结束溯源码长度不能超过64个字符")
    public String getEndTraceCode() { return endTraceCode; }
    public void setEndTraceCode(String endTraceCode) { this.endTraceCode = endTraceCode; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("detailId", getDetailId())
            .append("billId", getBillId())
            .append("bottlingId", getBottlingId())
            .append("quantity", getQuantity())
            .append("startTraceCode", getStartTraceCode())
            .append("endTraceCode", getEndTraceCode())
            .toString();
    }
}