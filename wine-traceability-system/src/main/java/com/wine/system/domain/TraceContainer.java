package com.wine.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wine.common.core.domain.BaseEntity;

public class TraceContainer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long containerId;
    private Long enterpriseId;
    private String enterpriseName;
    private Long workshopId;
    private String workshopName;
    private String containerType;
    private String containerCode;
    private BigDecimal capacityL;
    private String locationDetail;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastToasted;
    private String delFlag;

    public Long getContainerId()
    {
        return containerId;
    }

    public void setContainerId(Long containerId)
    {
        this.containerId = containerId;
    }

    @NotNull(message = "所属酒庄不能为空")
    public Long getEnterpriseId()
    {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId)
    {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName()
    {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName)
    {
        this.enterpriseName = enterpriseName;
    }

    public Long getWorkshopId()
    {
        return workshopId;
    }

    public void setWorkshopId(Long workshopId)
    {
        this.workshopId = workshopId;
    }

    public String getWorkshopName()
    {
        return workshopName;
    }

    public void setWorkshopName(String workshopName)
    {
        this.workshopName = workshopName;
    }

    @NotBlank(message = "容器类型不能为空")
    @Size(max = 30, message = "容器类型长度不能超过30个字符")
    public String getContainerType()
    {
        return containerType;
    }

    public void setContainerType(String containerType)
    {
        this.containerType = containerType;
    }

    @NotBlank(message = "容器编码不能为空")
    @Size(max = 100, message = "容器编码长度不能超过100个字符")
    public String getContainerCode()
    {
        return containerCode;
    }

    public void setContainerCode(String containerCode)
    {
        this.containerCode = containerCode;
    }

    public BigDecimal getCapacityL()
    {
        return capacityL;
    }

    public void setCapacityL(BigDecimal capacityL)
    {
        this.capacityL = capacityL;
    }

    @Size(max = 255, message = "详细位置长度不能超过255个字符")
    public String getLocationDetail()
    {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail)
    {
        this.locationDetail = locationDetail;
    }

    @Size(max = 30, message = "状态长度不能超过30个字符")
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getPurchaseDate()
    {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    public Date getLastToasted()
    {
        return lastToasted;
    }

    public void setLastToasted(Date lastToasted)
    {
        this.lastToasted = lastToasted;
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
            .append("containerId", getContainerId())
            .append("enterpriseId", getEnterpriseId())
            .append("enterpriseName", getEnterpriseName())
            .append("workshopId", getWorkshopId())
            .append("workshopName", getWorkshopName())
            .append("containerType", getContainerType())
            .append("containerCode", getContainerCode())
            .append("capacityL", getCapacityL())
            .append("locationDetail", getLocationDetail())
            .append("status", getStatus())
            .append("purchaseDate", getPurchaseDate())
            .append("lastToasted", getLastToasted())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
