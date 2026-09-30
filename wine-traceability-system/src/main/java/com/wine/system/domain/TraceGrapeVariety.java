package com.wine.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wine.common.core.domain.BaseEntity;

public class TraceGrapeVariety extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long varietyId;
    private String varietyName;
    private String englishName;
    private String status;
    private String delFlag;

    public Long getVarietyId()
    {
        return varietyId;
    }

    public void setVarietyId(Long varietyId)
    {
        this.varietyId = varietyId;
    }

    @NotBlank(message = "品种名称不能为空")
    @Size(max = 100, message = "品种名称长度不能超过100个字符")
    public String getVarietyName()
    {
        return varietyName;
    }

    public void setVarietyName(String varietyName)
    {
        this.varietyName = varietyName;
    }

    @Size(max = 100, message = "英文名称长度不能超过100个字符")
    public String getEnglishName()
    {
        return englishName;
    }

    public void setEnglishName(String englishName)
    {
        this.englishName = englishName;
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
            .append("varietyId", getVarietyId())
            .append("varietyName", getVarietyName())
            .append("englishName", getEnglishName())
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
