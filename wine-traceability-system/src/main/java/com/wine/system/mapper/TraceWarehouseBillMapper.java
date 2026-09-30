package com.wine.system.mapper;

import java.util.List;
import com.wine.system.domain.TraceWarehouseBill;
import com.wine.system.domain.TraceWarehouseBillDetail;

public interface TraceWarehouseBillMapper
{
    TraceWarehouseBill selectWarehouseBillById(Long billId);

    List<TraceWarehouseBill> selectWarehouseBillList(TraceWarehouseBill bill);

    List<TraceWarehouseBill> selectWarehouseBillOptions(String billType);

    TraceWarehouseBill checkBillNoUnique(String billNo);

    List<TraceWarehouseBillDetail> selectWarehouseBillDetailList(Long billId);

    int insertWarehouseBill(TraceWarehouseBill bill);

    int updateWarehouseBill(TraceWarehouseBill bill);

    int deleteWarehouseBillByIds(Long[] billIds);

    int insertWarehouseBillDetails(List<TraceWarehouseBillDetail> list);

    int deleteWarehouseBillDetailsByBillId(Long billId);

    int deleteWarehouseBillDetailsByBillIds(Long[] billIds);

    int hasLogisticsByBillId(Long billId);

    int hasWarehouseDetailByBottlingId(Long bottlingId);
}