package com.wine.system.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wine.common.core.domain.BaseEntity;

public class TraceProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long productId;
    private Long enterpriseId;
    private String enterpriseName;
    private String productName;
    private String category;
    private BigDecimal alcoholContent;
    private String netContent;
    private Integer shelfLife;
    private String status;
    private String delFlag;
    private String varietyNames;
    private List<TraceProductVariety> varietyList = new ArrayList<>();

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
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

    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100个字符")
    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    @Size(max = 50, message = "产品类别长度不能超过50个字符")
    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public BigDecimal getAlcoholContent()
    {
        return alcoholContent;
    }

    public void setAlcoholContent(BigDecimal alcoholContent)
    {
        this.alcoholContent = alcoholContent;
    }

    @Size(max = 50, message = "净含量长度不能超过50个字符")
    public String getNetContent()
    {
        return netContent;
    }

    public void setNetContent(String netContent)
    {
        this.netContent = netContent;
    }

    public Integer getShelfLife()
    {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife)
    {
        this.shelfLife = shelfLife;
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

    public String getVarietyNames()
    {
        return varietyNames;
    }

    public void setVarietyNames(String varietyNames)
    {
        this.varietyNames = varietyNames;
    }

    public List<TraceProductVariety> getVarietyList()
    {
        return varietyList;
    }

    public void setVarietyList(List<TraceProductVariety> varietyList)
    {
        this.varietyList = varietyList;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("enterpriseId", getEnterpriseId())
            .append("enterpriseName", getEnterpriseName())
            .append("productName", getProductName())
            .append("category", getCategory())
            .append("alcoholContent", getAlcoholContent())
            .append("netContent", getNetContent())
            .append("shelfLife", getShelfLife())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("varietyNames", getVarietyNames())
            .append("varietyList", getVarietyList())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
