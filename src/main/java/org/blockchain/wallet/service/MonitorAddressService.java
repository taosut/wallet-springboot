package org.blockchain.wallet.service;

import org.blockchain.wallet.entity.MonitorAddress;

import java.util.List;


public interface MonitorAddressService {

    List<MonitorAddress> selectBySelective(MonitorAddress monitorAddress);

    MonitorAddress selectByPrimaryKey(int id);
}
