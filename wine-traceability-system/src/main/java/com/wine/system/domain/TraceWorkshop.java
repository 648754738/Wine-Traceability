package com.wine.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wine.common.core.domain.BaseEntity;

public class TraceWorkshop extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long workshopId;
    private Long enterpriseId;
    private String enterpriseName;
    private String workshopName;
    private String location;
    private String supervisor;
    private String status;
    private String delFlag;

    public Long getWorkshopId()
    {
        return workshopId;
    }

    public void setWorkshopId(Long workshopId)
    {
        this.workshopId = workshopId;
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

    @NotBlank(message = "车间名称不能为空")
    @Size(max = 100, message = "车间名称长度不能超过100个字符")
    public String getWorkshopName()
    {
        return workshopName;
    }

    public void setWorkshopName(String workshopName)
    {
        this.workshopName = workshopName;
    }

    @Size(max = 255, message = "位置长度不能超过255个字符")
    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    @Size(max = 50, message = "负责人长度不能超过50个字符")
    public String getSupervisor()
    {
        return supervisor;
    }

    public void setSupervisor(String supervisor)
    {
        this.supervisor = supervisor;
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
            .append("workshopId", getWorkshopId())
            .append("enterpriseId", getEnterpriseId())
            .append("enterpriseName", getEnterpriseName())
            .append("workshopName", getWorkshopName())
            .append("location", getLocation())
            .append("supervisor", getSupervisor())
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
