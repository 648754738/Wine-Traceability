package com.wine.system.service;

import java.util.List;
import com.wine.system.domain.TraceWarehouseBill;

public interface ITraceWarehouseBillService
{
    TraceWarehouseBill selectWarehouseBillById(Long billId);

    List<TraceWarehouseBill> selectWarehouseBillList(TraceWarehouseBill bill);

    List<TraceWarehouseBill> selectWarehouseBillOptions(String billType);

    boolean checkBillNoUnique(TraceWarehouseBill bill);

    boolean hasLogisticsByBillId(Long billId);

    int insertWarehouseBill(TraceWarehouseBill bill);

    int updateWarehouseBill(TraceWarehouseBill bill);

    int deleteWarehouseBillByIds(Long[] billIds);
}