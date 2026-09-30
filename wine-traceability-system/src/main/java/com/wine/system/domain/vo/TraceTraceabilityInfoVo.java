package com.wine.system.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wine.system.domain.TraceContainerUsage;
import com.wine.system.domain.TraceProcessRecord;

public class TraceTraceabilityInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String traceCode;
    private Long bottlingId;
    private Long batchId;
    private Long regionId;
    private String regionAncestors;
    private String regionName;
    private String regionPath;
    private String enterpriseName;
    private String productName;
    private String batchNo;
    private String plotName;
    private String gpsCoordinates;
    private String workshopName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date entryWorkshopTime;
    private String sourceContainerCode;
    private String sourceContainerType;
    private String sourceLocationDetail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bottlingDate;
    private String bottleSpec;
    private String capCode;
    private String qrCodeUrl;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inspectionDate;
    private String inspector;
    private String isQualified;
    private List<TraceProcessRecord> processList = new ArrayList<>();
    private List<TraceContainerUsage> usageList = new ArrayList<>();
    private List<TraceWarehouseEventVo> warehouseEvents = new ArrayList<>();

    public String getTraceCode() { return traceCode; }
    public void setTraceCode(String traceCode) { this.traceCode = traceCode; }
    public Long getBottlingId() { return bottlingId; }
    public void setBottlingId(Long bottlingId) { this.bottlingId = bottlingId; }
    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }
    public Long getRegionId() { return regionId; }
    public void setRegionId(Long regionId) { this.regionId = regionId; }
    public String getRegionAncestors() { return regionAncestors; }
    public void setRegionAncestors(String regionAncestors) { this.regionAncestors = regionAncestors; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public String getRegionPath() { return regionPath; }
    public void setRegionPath(String regionPath) { this.regionPath = regionPath; }
    public String getEnterpriseName() { return enterpriseName; }
    public void setEnterpriseName(String enterpriseName) { this.enterpriseName = enterpriseName; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }
    public String getPlotName() { return plotName; }
    public void setPlotName(String plotName) { this.plotName = plotName; }
    public String getGpsCoordinates() { return gpsCoordinates; }
    public void setGpsCoordinates(String gpsCoordinates) { this.gpsCoordinates = gpsCoordinates; }
    public String getWorkshopName() { return workshopName; }
    public void setWorkshopName(String workshopName) { this.workshopName = workshopName; }
    public Date getEntryWorkshopTime() { return entryWorkshopTime; }
    public void setEntryWorkshopTime(Date entryWorkshopTime) { this.entryWorkshopTime = entryWorkshopTime; }
    public String getSourceContainerCode() { return sourceContainerCode; }
    public void setSourceContainerCode(String sourceContainerCode) { this.sourceContainerCode = sourceContainerCode; }
    public String getSourceContainerType() { return sourceContainerType; }
    public void setSourceContainerType(String sourceContainerType) { this.sourceContainerType = sourceContainerType; }
    public String getSourceLocationDetail() { return sourceLocationDetail; }
    public void setSourceLocationDetail(String sourceLocationDetail) { this.sourceLocationDetail = sourceLocationDetail; }
    public Date getBottlingDate() { return bottlingDate; }
    public void setBottlingDate(Date bottlingDate) { this.bottlingDate = bottlingDate; }
    public String getBottleSpec() { return bottleSpec; }
    public void setBottleSpec(String bottleSpec) { this.bottleSpec = bottleSpec; }
    public String getCapCode() { return capCode; }
    public void setCapCode(String capCode) { this.capCode = capCode; }
    public String getQrCodeUrl() { return qrCodeUrl; }
    public void setQrCodeUrl(String qrCodeUrl) { this.qrCodeUrl = qrCodeUrl; }
    public Date getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(Date inspectionDate) { this.inspectionDate = inspectionDate; }
    public String getInspector() { return inspector; }
    public void setInspector(String inspector) { this.inspector = inspector; }
    public String getIsQualified() { return isQualified; }
    public void setIsQualified(String isQualified) { this.isQualified = isQualified; }
    public List<TraceProcessRecord> getProcessList() { return processList; }
    public void setProcessList(List<TraceProcessRecord> processList) { this.processList = processList; }
    public List<TraceContainerUsage> getUsageList() { return usageList; }
    public void setUsageList(List<TraceContainerUsage> usageList) { this.usageList = usageList; }
    public List<TraceWarehouseEventVo> getWarehouseEvents() { return warehouseEvents; }
    public void setWarehouseEvents(List<TraceWarehouseEventVo> warehouseEvents) { this.warehouseEvents = warehouseEvents; }
}