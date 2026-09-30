package com.wine.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TraceProductVariety
{
    private Long id;
    private Long productId;
    private Long varietyId;
    private String varietyName;
    private BigDecimal percentage;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    @NotNull(message = "葡萄品种不能为空")
    public Long getVarietyId()
    {
        return varietyId;
    }

    public void setVarietyId(Long varietyId)
    {
        this.varietyId = varietyId;
    }

    public String getVarietyName()
    {
        return varietyName;
    }

    public void setVarietyName(String varietyName)
    {
        this.varietyName = varietyName;
    }

    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage)
    {
        this.percentage = percentage;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productId", getProductId())
            .append("varietyId", getVarietyId())
            .append("varietyName", getVarietyName())
            .append("percentage", getPercentage())
            .toString();
    }
}
