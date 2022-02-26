package org.dbs24.tik.mobile.dao.impl;

import org.dbs24.tik.mobile.constant.reference.*;
import org.dbs24.tik.mobile.dao.ReferenceDao;
import org.dbs24.tik.mobile.entity.domain.*;
import org.dbs24.tik.mobile.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ReferenceDaoImpl implements ReferenceDao {
    private final CurrencyRepo currencyRepo;
    private final OrderActionTypeRepo orderActionTypeRepo;
    private final OrderStatusRepo orderStatusRepo;
    private final UserStatusRepo userStatusRepo;
    private final DeviceTypeRepo deviceTypeRepo;

    @Autowired
    public ReferenceDaoImpl(CurrencyRepo currencyRepo,
                            OrderActionTypeRepo orderActionTypeRepo,
                            OrderStatusRepo orderStatusRepo,
                            UserStatusRepo userStatusRepo,
                            DeviceTypeRepo deviceTypeRepo) {

        this.currencyRepo = currencyRepo;
        this.orderActionTypeRepo = orderActionTypeRepo;
        this.orderStatusRepo = orderStatusRepo;
        this.userStatusRepo = userStatusRepo;
        this.deviceTypeRepo = deviceTypeRepo;
    }

    @Override
    @Transactional
    public void saveAllReferences() {
        List<OrderActionType> actionTypes = ActionTypeDefine.getAll();
        List<Currency> currencies = CurrencyDefine.getAll();
        List<OrderStatus> orderStatuses = OrderStatusDefine.getAll();
        List<UserStatus> userStatuses = UserStatusDefine.getAll();
        List<DeviceType> deviceTypes = DeviceTypeDefine.getAll();

        currencyRepo.saveAll(currencies);
        orderActionTypeRepo.saveAll(actionTypes);
        orderStatusRepo.saveAll(orderStatuses);
        userStatusRepo.saveAll(userStatuses);
        deviceTypeRepo.saveAll(deviceTypes);
    }
}
