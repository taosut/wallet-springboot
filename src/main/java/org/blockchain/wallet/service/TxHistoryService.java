package org.blockchain.wallet.service;

import org.blockchain.wallet.entity.TxHistory;

import java.util.List;

public interface TxHistoryService {

    int selectCount();

    int deleteAll();

    int deleteByCreateTime();

    List<TxHistory> selectBySelective(TxHistory txHistory);

    int insertTxHistory(TxHistory txHistory);
}
