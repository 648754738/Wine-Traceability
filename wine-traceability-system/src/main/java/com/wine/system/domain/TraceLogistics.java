package com.wine.system.domain;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wine.common.core.domain.BaseEntity;

public class TraceLogistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long logisticsId;
    private Long outBillId;
    private String outBillNo;
    private String carrier;
    private String trackingNo;
    private String consignee;
    private String destination;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shipDate;
    private String delFlag;

    public Long getLogisticsId() { return logisticsId; }
    public void setLogisticsId(Long logisticsId) { this.logisticsId = logisticsId; }

    @NotNull(message = "出库单不能为空")
    public Long getOutBillId() { return outBillId; }
    public void setOutBillId(Long outBillId) { this.outBillId = outBillId; }

    public String getOutBillNo() { return outBillNo; }
    public void setOutBillNo(String outBillNo) { this.outBillNo = outBillNo; }

    @Size(max = 100, message = "承运商长度不能超过100个字符")
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    @Size(max = 100, message = "运单号长度不能超过100个字符")
    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }

    @Size(max = 100, message = "收货方长度不能超过100个字符")
    public String getConsignee() { return consignee; }
    public void setConsignee(String consignee) { this.consignee = consignee; }

    @Size(max = 255, message = "目的地长度不能超过255个字符")
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getShipDate() { return shipDate; }
    public void setShipDate(Date shipDate) { this.shipDate = shipDate; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("logisticsId", getLogisticsId())
            .append("outBillId", getOutBillId())
            .append("carrier", getCarrier())
            .append("trackingNo", getTrackingNo())
            .append("consignee", getConsignee())
            .append("destination", getDestination())
            .append("shipDate", getShipDate())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}