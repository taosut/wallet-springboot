package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.TxHistory;
import org.blockchain.wallet.mapper.TxHistoryMapper;
import org.blockchain.wallet.service.TxHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TxHistoryServiceImpl implements TxHistoryService {

    @Autowired
    TxHistoryMapper txHistoryMapper;


    @Override
    public int selectCount() {
        return txHistoryMapper.selectCount();
    }

    @Override
    public int deleteAll() {
        return txHistoryMapper.deleteAll();
    }

    @Override
    public int deleteByCreateTime() {
        return txHistoryMapper.deleteByCreateTime();
    }

    @Override
    public List<TxHistory> selectBySelective(TxHistory txHistory) {
        return txHistoryMapper.selectBySelective(txHistory);
    }

    @Override
    public int insertTxHistory(TxHistory txHistory) {

        return txHistoryMapper.insert(txHistory);
    }
}
