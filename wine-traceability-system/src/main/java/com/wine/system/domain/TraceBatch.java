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

public class TraceBatch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long batchId;
    private String batchNo;
    private Long productId;
    private String productName;
    private Long plotId;
    private String plotName;
    private Long workshopId;
    private String workshopName;
    private Long enterpriseId;
    private String enterpriseName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date entryWorkshopTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date harvestDate;
    private String harvestLocation;
    private BigDecimal totalYieldL;
    private String status;
    private String delFlag;

    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    @NotBlank(message = "批次号不能为空")
    @Size(max = 64, message = "批次号长度不能超过64个字符")
    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    @NotNull(message = "产品不能为空")
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    @NotNull(message = "地块不能为空")
    public Long getPlotId() { return plotId; }
    public void setPlotId(Long plotId) { this.plotId = plotId; }

    public String getPlotName() { return plotName; }
    public void setPlotName(String plotName) { this.plotName = plotName; }

    @NotNull(message = "车间不能为空")
    public Long getWorkshopId() { return workshopId; }
    public void setWorkshopId(Long workshopId) { this.workshopId = workshopId; }

    public String getWorkshopName() { return workshopName; }
    public void setWorkshopName(String workshopName) { this.workshopName = workshopName; }

    public Long getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(Long enterpriseId) { this.enterpriseId = enterpriseId; }

    public String getEnterpriseName() { return enterpriseName; }
    public void setEnterpriseName(String enterpriseName) { this.enterpriseName = enterpriseName; }

    public Date getEntryWorkshopTime() { return entryWorkshopTime; }
    public void setEntryWorkshopTime(Date entryWorkshopTime) { this.entryWorkshopTime = entryWorkshopTime; }

    public Date getHarvestDate() { return harvestDate; }
    public void setHarvestDate(Date harvestDate) { this.harvestDate = harvestDate; }

    @Size(max = 255, message = "采收地描述长度不能超过255个字符")
    public String getHarvestLocation() { return harvestLocation; }
    public void setHarvestLocation(String harvestLocation) { this.harvestLocation = harvestLocation; }

    public BigDecimal getTotalYieldL() { return totalYieldL; }
    public void setTotalYieldL(BigDecimal totalYieldL) { this.totalYieldL = totalYieldL; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("batchId", getBatchId())
            .append("batchNo", getBatchNo())
            .append("productId", getProductId())
            .append("plotId", getPlotId())
            .append("workshopId", getWorkshopId())
            .append("entryWorkshopTime", getEntryWorkshopTime())
            .append("harvestDate", getHarvestDate())
            .append("harvestLocation", getHarvestLocation())
            .append("totalYieldL", getTotalYieldL())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
