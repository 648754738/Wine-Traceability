package com.wine.system.domain.vo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TraceWarehouseEventVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long billId;
    private String billType;
    private String billNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;
    private Integer quantity;
    private String startTraceCode;
    private String endTraceCode;
    private String carrier;
    private String trackingNo;
    private String consignee;
    private String destination;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shipDate;

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }
    public Date getOperateTime() { return operateTime; }
    public void setOperateTime(Date operateTime) { this.operateTime = operateTime; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getStartTraceCode() { return startTraceCode; }
    public void setStartTraceCode(String startTraceCode) { this.startTraceCode = startTraceCode; }
    public String getEndTraceCode() { return endTraceCode; }
    public void setEndTraceCode(String endTraceCode) { this.endTraceCode = endTraceCode; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }
    public String getConsignee() { return consignee; }
    public void setConsignee(String consignee) { this.consignee = consignee; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Date getShipDate() { return shipDate; }
    public void setShipDate(Date shipDate) { this.shipDate = shipDate; }
}