package com.wine.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wine.common.core.domain.BaseEntity;

public class TraceQualityReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long reportId;
    private Long batchId;
    private String batchNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inspectionDate;
    private String inspector;
    private BigDecimal alcohol;
    private BigDecimal totalSugar;
    private BigDecimal volatileAcid;
    private BigDecimal sulfite;
    private String isQualified;
    private String reportUrl;
    private String remarks;
    private String delFlag;

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }

    @NotNull(message = "生产批次不能为空")
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public Date getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(Date inspectionDate) { this.inspectionDate = inspectionDate; }

    @Size(max = 100, message = "检测机构长度不能超过100个字符")
    public String getInspector() { return inspector; }
    public void setInspector(String inspector) { this.inspector = inspector; }

    public BigDecimal getAlcohol() { return alcohol; }
    public void setAlcohol(BigDecimal alcohol) { this.alcohol = alcohol; }

    public BigDecimal getTotalSugar() { return totalSugar; }
    public void setTotalSugar(BigDecimal totalSugar) { this.totalSugar = totalSugar; }

    public BigDecimal getVolatileAcid() { return volatileAcid; }
    public void setVolatileAcid(BigDecimal volatileAcid) { this.volatileAcid = volatileAcid; }

    public BigDecimal getSulfite() { return sulfite; }
    public void setSulfite(BigDecimal sulfite) { this.sulfite = sulfite; }

    public String getIsQualified() { return isQualified; }
    public void setIsQualified(String isQualified) { this.isQualified = isQualified; }

    @Size(max = 255, message = "报告地址长度不能超过255个字符")
    public String getReportUrl() { return reportUrl; }
    public void setReportUrl(String reportUrl) { this.reportUrl = reportUrl; }

    @Size(max = 500, message = "备注长度不能超过500个字符")
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getDelFlag() { return delFlag; }
    public void setDelFlag(String delFlag) { this.delFlag = delFlag; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("reportId", getReportId())
            .append("batchId", getBatchId())
            .append("inspectionDate", getInspectionDate())
            .append("inspector", getInspector())
            .append("alcohol", getAlcohol())
            .append("totalSugar", getTotalSugar())
            .append("volatileAcid", getVolatileAcid())
            .append("sulfite", getSulfite())
            .append("isQualified", getIsQualified())
            .append("reportUrl", getReportUrl())
            .append("remarks", getRemarks())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}