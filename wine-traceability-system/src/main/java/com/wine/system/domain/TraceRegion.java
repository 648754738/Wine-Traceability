package com.wine.system.domain;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wine.common.core.domain.BaseEntity;

public class TraceRegion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long regionId;
    private Long parentId;
    private String ancestors;
    private String regionName;
    private Integer regionLevel;
    private Integer orderNum;
    private String status;
    private String delFlag;
    private String description;
    private String parentName;
    private List<TraceRegion> children = new ArrayList<>();

    public Long getRegionId()
    {
        return regionId;
    }

    public void setRegionId(Long regionId)
    {
        this.regionId = regionId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "产区名称不能为空")
    @Size(max = 100, message = "产区名称长度不能超过100个字符")
    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }

    public Integer getRegionLevel()
    {
        return regionLevel;
    }

    public void setRegionLevel(Integer regionLevel)
    {
        this.regionLevel = regionLevel;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
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

    @Size(max = 500, message = "描述长度不能超过500个字符")
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public List<TraceRegion> getChildren()
    {
        return children;
    }

    public void setChildren(List<TraceRegion> children)
    {
        this.children = children;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("regionId", getRegionId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("regionName", getRegionName())
            .append("regionLevel", getRegionLevel())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("description", getDescription())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
