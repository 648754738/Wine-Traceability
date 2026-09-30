package com.wine.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wine.common.core.domain.BaseEntity;

public class TraceEnterprise extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long enterpriseId;
    private Long regionId;
    private String regionName;
    private String enterpriseName;
    private String licenseNo;
    private String contact;
    private String phone;
    private String address;
    private String status;
    private String delFlag;

    public Long getEnterpriseId()
    {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId)
    {
        this.enterpriseId = enterpriseId;
    }

    @NotNull(message = "所属产区不能为空")
    public Long getRegionId()
    {
        return regionId;
    }

    public void setRegionId(Long regionId)
    {
        this.regionId = regionId;
    }

    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }

    @NotBlank(message = "酒庄名称不能为空")
    @Size(max = 100, message = "酒庄名称长度不能超过100个字符")
    public String getEnterpriseName()
    {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName)
    {
        this.enterpriseName = enterpriseName;
    }

    @Size(max = 100, message = "营业执照号长度不能超过100个字符")
    public String getLicenseNo()
    {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo)
    {
        this.licenseNo = licenseNo;
    }

    @Size(max = 50, message = "联系人长度不能超过50个字符")
    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Size(max = 255, message = "地址长度不能超过255个字符")
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
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
            .append("enterpriseId", getEnterpriseId())
            .append("regionId", getRegionId())
            .append("regionName", getRegionName())
            .append("enterpriseName", getEnterpriseName())
            .append("licenseNo", getLicenseNo())
            .append("contact", getContact())
            .append("phone", getPhone())
            .append("address", getAddress())
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
