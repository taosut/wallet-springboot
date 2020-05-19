package org.blockchain.wallet.service;

import org.blockchain.wallet.entity.MonitorCoin;

import java.util.List;

public interface MonitorCoinService {

    List<MonitorCoin> selectBySelective(MonitorCoin monitorCoin);
}
