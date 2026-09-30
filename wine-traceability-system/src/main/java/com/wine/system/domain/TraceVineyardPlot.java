package com.wine.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wine.common.core.domain.BaseEntity;

public class TraceVineyardPlot extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long plotId;
    private Long enterpriseId;
    private String enterpriseName;
    private String regionName;
    private String plotName;
    private String plotCode;
    private BigDecimal areaHa;
    private BigDecimal elevationM;
    private String soilType;
    private String gpsCoordinates;
    private String plantedVariety;
    private Integer vineAge;
    private String status;
    private String delFlag;

    public Long getPlotId()
    {
        return plotId;
    }

    public void setPlotId(Long plotId)
    {
        this.plotId = plotId;
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

    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }

    @NotBlank(message = "地块名称不能为空")
    @Size(max = 100, message = "地块名称长度不能超过100个字符")
    public String getPlotName()
    {
        return plotName;
    }

    public void setPlotName(String plotName)
    {
        this.plotName = plotName;
    }

    @NotBlank(message = "地块编码不能为空")
    @Size(max = 100, message = "地块编码长度不能超过100个字符")
    public String getPlotCode()
    {
        return plotCode;
    }

    public void setPlotCode(String plotCode)
    {
        this.plotCode = plotCode;
    }

    public BigDecimal getAreaHa()
    {
        return areaHa;
    }

    public void setAreaHa(BigDecimal areaHa)
    {
        this.areaHa = areaHa;
    }

    public BigDecimal getElevationM()
    {
        return elevationM;
    }

    public void setElevationM(BigDecimal elevationM)
    {
        this.elevationM = elevationM;
    }

    @Size(max = 100, message = "土壤类型长度不能超过100个字符")
    public String getSoilType()
    {
        return soilType;
    }

    public void setSoilType(String soilType)
    {
        this.soilType = soilType;
    }

    @Size(max = 255, message = "GPS坐标长度不能超过255个字符")
    public String getGpsCoordinates()
    {
        return gpsCoordinates;
    }

    public void setGpsCoordinates(String gpsCoordinates)
    {
        this.gpsCoordinates = gpsCoordinates;
    }

    @Size(max = 100, message = "主要品种长度不能超过100个字符")
    public String getPlantedVariety()
    {
        return plantedVariety;
    }

    public void setPlantedVariety(String plantedVariety)
    {
        this.plantedVariety = plantedVariety;
    }

    public Integer getVineAge()
    {
        return vineAge;
    }

    public void setVineAge(Integer vineAge)
    {
        this.vineAge = vineAge;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
            .append("plotId", getPlotId())
            .append("enterpriseId", getEnterpriseId())
            .append("enterpriseName", getEnterpriseName())
            .append("regionName", getRegionName())
            .append("plotName", getPlotName())
            .append("plotCode", getPlotCode())
            .append("areaHa", getAreaHa())
            .append("elevationM", getElevationM())
            .append("soilType", getSoilType())
            .append("gpsCoordinates", getGpsCoordinates())
            .append("plantedVariety", getPlantedVariety())
            .append("vineAge", getVineAge())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
