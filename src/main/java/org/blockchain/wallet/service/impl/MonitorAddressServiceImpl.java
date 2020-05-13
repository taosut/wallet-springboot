package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.MonitorAddress;
import org.blockchain.wallet.mapper.MonitorAddressMapper;
import org.blockchain.wallet.service.MonitorAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorAddressServiceImpl implements MonitorAddressService {

    @Autowired
    MonitorAddressMapper monitorAddressMapper;

    @Override
    public List<MonitorAddress> selectBySelective(MonitorAddress monitorAddress) {
        return monitorAddressMapper.selectBySelective(monitorAddress);
    }

    @Override
    public MonitorAddress selectByPrimaryKey(int id) {
        return monitorAddressMapper.selectByPrimaryKey(id);
    }
}
