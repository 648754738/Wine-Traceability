package com.wine.system.service.impl;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wine.common.constant.UserConstants;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceWarehouseBill;
import com.wine.system.domain.TraceWarehouseBillDetail;
import com.wine.system.mapper.TraceWarehouseBillMapper;
import com.wine.system.service.ITraceWarehouseBillService;

@Service
public class TraceWarehouseBillServiceImpl implements ITraceWarehouseBillService
{
    @Autowired
    private TraceWarehouseBillMapper warehouseBillMapper;

    @Override
    public TraceWarehouseBill selectWarehouseBillById(Long billId)
    {
        TraceWarehouseBill bill = warehouseBillMapper.selectWarehouseBillById(billId);
        if (StringUtils.isNotNull(bill))
        {
            bill.setDetailList(warehouseBillMapper.selectWarehouseBillDetailList(billId));
        }
        return bill;
    }

    @Override
    public List<TraceWarehouseBill> selectWarehouseBillList(TraceWarehouseBill bill)
    {
        return warehouseBillMapper.selectWarehouseBillList(bill);
    }

    @Override
    public List<TraceWarehouseBill> selectWarehouseBillOptions(String billType)
    {
        return warehouseBillMapper.selectWarehouseBillOptions(billType);
    }

    @Override
    public boolean checkBillNoUnique(TraceWarehouseBill bill)
    {
        Long billId = StringUtils.isNull(bill.getBillId()) ? -1L : bill.getBillId();
        TraceWarehouseBill info = warehouseBillMapper.checkBillNoUnique(bill.getBillNo());
        if (StringUtils.isNotNull(info) && info.getBillId().longValue() != billId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasLogisticsByBillId(Long billId)
    {
        return warehouseBillMapper.hasLogisticsByBillId(billId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertWarehouseBill(TraceWarehouseBill bill)
    {
        int rows = warehouseBillMapper.insertWarehouseBill(bill);
        insertDetails(bill);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWarehouseBill(TraceWarehouseBill bill)
    {
        warehouseBillMapper.deleteWarehouseBillDetailsByBillId(bill.getBillId());
        insertDetails(bill);
        return warehouseBillMapper.updateWarehouseBill(bill);
    }

    private void insertDetails(TraceWarehouseBill bill)
    {
        List<TraceWarehouseBillDetail> detailList = bill.getDetailList();
        if (detailList == null)
        {
            detailList = Collections.emptyList();
        }
        for (TraceWarehouseBillDetail detail : detailList)
        {
            detail.setBillId(bill.getBillId());
            detail.setCreateBy(bill.getCreateBy());
            detail.setUpdateBy(bill.getUpdateBy());
        }
        if (!detailList.isEmpty())
        {
            warehouseBillMapper.insertWarehouseBillDetails(detailList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteWarehouseBillByIds(Long[] billIds)
    {
        warehouseBillMapper.deleteWarehouseBillDetailsByBillIds(billIds);
        return warehouseBillMapper.deleteWarehouseBillByIds(billIds);
    }
}