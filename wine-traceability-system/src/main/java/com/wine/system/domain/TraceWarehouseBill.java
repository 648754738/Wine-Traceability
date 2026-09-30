package com.wine.system.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wine.common.core.domain.BaseEntity;

public class TraceWarehouseBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long billId;
    private String billType;
    private String billNo;
    private Long operatorId;
    private String operatorName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;
    private Long totalQuantity;
    private Integer detailCount;
    private String delFlag;
    private List<TraceWarehouseBillDetail> detailList = new ArrayList<>();

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }

    @NotBlank(message = "单据类型不能为空")
    @Size(max = 16, message = "单据类型长度不能超过16个字符")
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }

    @NotBlank(message = "单据编号不能为空")
    @Size(max = 64, message = "单据编号长度不能超过64个字符")
    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }

    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }

    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    public Date getOperateTime() { return operateTime; }
    public void setOperateTime(Date operateTime) { this.operateTime = operateTime; }

    public Long getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Long totalQuantity) { this.totalQuantity = totalQuantity; }

    public Integer getDetailCount() { return detailCount; }
    public void setDetailCount(Integer detailCount) { this.detailCount = detailCount; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    public List<TraceWarehouseBillDetail> getDetailList() { return detailList; }
    public void setDetailList(List<TraceWarehouseBillDetail> detailList) { this.detailList = detailList; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("billId", getBillId())
            .append("billType", getBillType())
            .append("billNo", getBillNo())
            .append("operatorId", getOperatorId())
            .append("operateTime", getOperateTime())
            .append("totalQuantity", getTotalQuantity())
            .append("detailCount", getDetailCount())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}