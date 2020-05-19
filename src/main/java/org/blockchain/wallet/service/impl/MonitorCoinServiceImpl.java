package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.MonitorCoin;
import org.blockchain.wallet.mapper.MonitorCoinMapper;
import org.blockchain.wallet.service.MonitorCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorCoinServiceImpl implements MonitorCoinService {

    @Autowired
    MonitorCoinMapper monitorCoinMapper;

    @Override
    public List<MonitorCoin> selectBySelective(MonitorCoin monitorCoin) {
        return monitorCoinMapper.selectBySelective(monitorCoin);
    }
}
